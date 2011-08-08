package bosses;

import bosses.EpicBossState.State;
import l2p.common.ThreadPoolManager;
import l2p.extensions.listeners.CurrentHpChangeListener;
import l2p.extensions.listeners.L2ZoneEnterLeaveListener;
import l2p.extensions.listeners.PropertyCollection;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.instancemanager.ZoneManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.L2Zone;
import l2p.gameserver.model.L2Zone.ZoneType;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.Earthquake;
import l2p.gameserver.serverpackets.ExShowScreenMessage;
import l2p.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2p.gameserver.serverpackets.MagicSkillCanceled;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.serverpackets.NpcInfo;
import l2p.gameserver.serverpackets.SocialAction;
import l2p.gameserver.tables.NpcTable;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.L2NpcTemplate;
import l2p.util.GArray;
import l2p.util.Location;
import l2p.util.Log;
import l2p.util.Rnd;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class FrintezzaManager extends Functions implements ScriptFile
{
	// The boss of all bosses ofcourse
	private static Location frintezzaSpawn = new Location(174240, -89805, -5022, 16048, 29045);
	// weak Scarlet Van Halisha.
	private static Location scarletSpawnWeak = new Location(174232, -88020, -5112, 16384, 29046);
	// Portrait spawns - 4 portraits = 4 spawns
	private static Location[] portraitSpawns = {new Location(175832, -87176, -5104, 35048, 29048),
		new Location(175880, -88696, -5104, 28205, 29049), new Location(172600, -88696, -5104, 64817, 29048),
		new Location(172632, -87176, -5104, 57730, 29049)};
	// Demon spawns - 4 portraits = 4 demons
	private static Location[] demonSpawns = {new Location(175832, -87176, -5104, 35048, 29050),
		new Location(175880, -88696, -5104, 28205, 29051), new Location(172600, -88696, -5104, 64817, 29051),
		new Location(172632, -87176, -5104, 57730, 29050)};
	private static Location cubeSpawn = new Location(174232, -88020, -5114, 16384, 29061);
	private static L2NpcInstance frintezza, weakScarlet, strongScarlet, cube;
	private static L2NpcInstance[] portraits = new L2NpcInstance[4];
	private static L2NpcInstance[] demons = new L2NpcInstance[4];
	private static int _intervalOfFrintezzaSongs = 30000;
	private static int _scarletMorph = 0;
	private static ScheduledFuture<?> _monsterSpawnTask = null, _activityTimeEndTask = null, _intervalEndTask = null,
		_dieTask = null;
	private static EpicBossState _state;
	private static L2Zone _zone;
	private static ZoneListener _zoneListener = new ZoneListener();
	private static CurrentHpListener _currentHpListener = new CurrentHpListener();
	private static final int FWF_FIXINTERVALOFFRINTEZZA = 2 * 1440 * 60000;
	private static final int FWF_RANDOMINTERVALOFFRINTEZZA = 1440 * 60000;
	private static final int FWF_INTERVALOFNEXTMONSTER = 1 * 30000;
	private static final int FWF_ACTIVITYTIMEOFFRINTEZZA = 120 * 60000;
	private static final int _frintezzaId = 29045;
	private static final int _strongScarletId = 29047;
	private static final int _frintezzasSwordId = 7903;
	private static L2NpcInstance _frintezzaDummy, _overheadDummy;

	public static class ZoneListener extends L2ZoneEnterLeaveListener
	{
		@Override
		public void objectEntered(L2Zone zone, L2Object object)
		{
			if(object != null && object.isPlayer() && !object.inObserverMode() && !((L2Player) object).isGM())
			{
				setScarletSpawnTask(false);
			}
		}

		@Override
		public void objectLeaved(L2Zone zone, L2Object object)
		{
		}
	}

	public static class CurrentHpListener extends CurrentHpChangeListener
	{
		@Override
		public void onCurrentHpChange(L2Character actor, double oldHp, double newHp)
		{
			if(actor == null || actor.isDead() || actor != weakScarlet)
			{
				return;
			}
			double maxHp = actor.getMaxHp();
			switch(_scarletMorph)
			{
				case 1:
					if(newHp < maxHp * 2 / 3)
					{
						_scarletMorph = 2;
						ThreadPoolManager.getInstance().scheduleGeneral(new SecondMorph(1), 1100);
					}
					break;
				case 2:
					if(newHp < maxHp * 1 / 3)
					{
						_scarletMorph = 3;
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(1), 2000);
					}
					break;
			}
		}
	}

	public static void init()
	{
		_state = new EpicBossState(_frintezzaId);
		_zone = ZoneManager.getInstance().getZoneById(ZoneType.epic, 702120, false);
		_zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
		System.out.println("FrintezzaManager: State of Frintezza is " + _state.getState() + ".");
		if(!_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			setIntervalEndTask();
		}
		Date dt = new Date(_state.getRespawnDate());
		System.out.println("FrintezzaManager: Next spawn date of Frintezza is " + dt + ".");
	}

	private static L2NpcInstance spawn(Location loc)
	{
		L2NpcTemplate template = NpcTable.getTemplate(loc.id);
		L2NpcInstance npc = template.getNewInstance();
		npc.setSpawnedLoc(loc);
		npc.onSpawn();
		npc.setHeading(loc.h);
		npc.setXYZInvisible(loc);
		npc.spawnMe();
		return npc;
	}

	public static void setScarletSpawnTask(boolean forced)
	{
		if(forced || _state.getState().equals(EpicBossState.State.NOTSPAWN) && _monsterSpawnTask == null)
		{
			Log.add("Frintezza activated", "bosses");
			LastImperialTombManager.setReachToHall();
			if(forced && _monsterSpawnTask != null)
			{
				_monsterSpawnTask.cancel(true);
			}
			_monsterSpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(1), forced ? 1000 : 300000);
		}
	}

	/**
	 * Shows a movie to the players in the lair.
	 *
	 * @param target       - L2NpcInstance target is the center of this movie
	 * @param dist         - int distance from target
	 * @param yaw          - angle of movie (north = 90, south = 270, east = 0 , west = 180)
	 * @param pitch        - pitch > 0 looks up / pitch < 0 looks down
	 * @param time         - fast ++ or slow -- depends on the value
	 * @param duration     - How long to watch the movie
	 * @param socialAction - 1,2,3 social actions / other values do nothing
	 */
	private static void showSocialActionMovie(L2NpcInstance target, int dist, int yaw, int pitch, int time, int duration, int socialAction)
	{
		if(target == null)
		{
			return;
		}
		for(L2Player pc : getPlayersInside())
		{
			if(pc.getDistance(target) <= 2550)
			{
				pc.enterMovieMode();
				pc.specialCamera(target, dist, yaw, pitch, time, duration);
			}
			else
			{
				pc.leaveMovieMode();
			}
		}
		if(socialAction > 0 && socialAction < 5)
		{
			target.broadcastPacket(new SocialAction(target.getObjectId(), socialAction));
		}
	}

	private static class Spawn implements Runnable
	{
		private int _taskId = 0;

		public Spawn(int taskId)
		{
			_taskId = taskId;
		}

		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1: // spawn.
						LastImperialTombManager.cleanUpTomb(false);
						_state.setRespawnDate(Rnd.get(FWF_FIXINTERVALOFFRINTEZZA, FWF_FIXINTERVALOFFRINTEZZA + FWF_RANDOMINTERVALOFFRINTEZZA));
						_state.setState(EpicBossState.State.ALIVE);
						_state.update();
						_frintezzaDummy = spawn(new Location(174240, -89805, -5080, 16048, 29059));
						_overheadDummy = spawn(new Location(174232, -88020, -4000, 16384, 29059));
						_overheadDummy.setCollisionHeight(800);
						_overheadDummy.broadcastPacket(new NpcInfo(_overheadDummy, null));
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(2), 1000);
						break;
					case 2:
						showSocialActionMovie(_overheadDummy, 0, 0, -75, 0, 0, 0);
						showSocialActionMovie(_overheadDummy, 1000, 90, -10, 6500, 8000, 0);
						frintezza = spawn(frintezzaSpawn);
						for(int i = 0; i < 4; i++)
						{
							portraits[i] = spawn(portraitSpawns[i]);
							portraits[i].setImobilised(true);
							demons[i] = spawn(demonSpawns[i]);
						}
						blockAll(true);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(3), 6500);
						break;
					case 3:
						showSocialActionMovie(_frintezzaDummy, 1800, 90, 8, 6500, 7000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(4), 900);
						break;
					case 4:
						showSocialActionMovie(_frintezzaDummy, 140, 90, 10, 2500, 4500, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(5), 4000);
						break;
					case 5:
						showSocialActionMovie(frintezza, 40, 75, -10, 0, 1000, 0);
						showSocialActionMovie(frintezza, 40, 75, -10, 0, 12000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(6), 1350);
						break;
					case 6:
						frintezza.broadcastPacket(new SocialAction(frintezza.getObjectId(), 2));
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(7), 7000);
						break;
					case 7:
						_overheadDummy.deleteMe();
						_overheadDummy = null;
						_frintezzaDummy.deleteMe();
						_frintezzaDummy = null;
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(8), 1000);
						break;
					case 8:
						showSocialActionMovie(demons[0], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(9), 2800);
						break;
					case 9:
						showSocialActionMovie(demons[1], 140, 0, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(10), 2800);
						break;
					case 10:
						showSocialActionMovie(demons[2], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(11), 2800);
						break;
					case 11:
						showSocialActionMovie(demons[3], 140, 180, 3, 22000, 3000, 1);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(12), 3000);
						break;
					case 12:
						showSocialActionMovie(frintezza, 240, 90, 0, 0, 1000, 0);
						showSocialActionMovie(frintezza, 240, 90, 25, 5500, 10000, 3);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(13), 3000);
						break;
					case 13:
						showSocialActionMovie(frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(14), 700);
						break;
					case 14:
						showSocialActionMovie(frintezza, 100, 195, 35, 0, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(15), 1300);
						break;
					case 15:
						showSocialActionMovie(frintezza, 120, 180, 45, 1500, 10000, 0);
						frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5006, 1, 34000, 0));
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(16), 1500);
						break;
					case 16:
						showSocialActionMovie(frintezza, 520, 135, 45, 8000, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(17), 7500);
						break;
					case 17:
						showSocialActionMovie(frintezza, 1500, 110, 25, 10000, 13000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(18), 9500);
						break;
					case 18:
						weakScarlet = spawn(scarletSpawnWeak);
						block(weakScarlet, true);
						weakScarlet.getListenerEngine().addPropertyChangeListener(PropertyCollection.HitPoints, _currentHpListener);
						weakScarlet.doCast(SkillTable.getInstance().getInfo(5004, 1), null, false);
						Earthquake eq = new Earthquake(weakScarlet.getLoc(), 50, 6);
						for(L2Player pc : getPlayersInside())
						{
							pc.broadcastPacket(eq);
						}
						showSocialActionMovie(weakScarlet, 1000, 160, 20, 6000, 6000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(19), 5500);
						break;
					case 19:
						showSocialActionMovie(weakScarlet, 800, 160, 5, 1000, 10000, 2);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(20), 2100);
						break;
					case 20:
						showSocialActionMovie(weakScarlet, 300, 60, 8, 0, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(21), 2000);
						break;
					case 21:
						showSocialActionMovie(weakScarlet, 1000, 90, 10, 3000, 5000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(22), 3000);
						break;
					case 22:
						for(L2Player pc : getPlayersInside())
						{
							pc.leaveMovieMode();
						}
						ThreadPoolManager.getInstance().scheduleGeneral(new Spawn(23), 2000);
						break;
					case 23:
						blockAll(false);
						_scarletMorph = 1;
						_activityTimeEndTask = ThreadPoolManager.getInstance().scheduleGeneral(new ActivityTimeEnd(), FWF_ACTIVITYTIMEOFFRINTEZZA);
						for(int i = 0; i < 4; i++)
						{
							ThreadPoolManager.getInstance().scheduleGeneral(new doSkill(demons[i], _intervalOfFrintezzaSongs, 1000), 4000);
						}
						ThreadPoolManager.getInstance().scheduleGeneral(new respawnDemons(), FWF_INTERVALOFNEXTMONSTER);
						ThreadPoolManager.getInstance().scheduleGeneral(new Music(), Rnd.get(_intervalOfFrintezzaSongs));
						break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static class Music implements Runnable
	{
		public void run()
		{
			if(frintezza == null)
			{
				return;
			}
			int song = Math.max(1, Math.min(5, getSong()));
			String song_name = "";
			switch(song)
			{
				case 1:
					song_name = "Concert Hall Melody";
					break;
				case 2:
					song_name = "Rampaging Opus en masse";
					break;
				case 3:
					song_name = "Power Encore";
					break;
				case 4:
					song_name = "Mournful Chorale Prelude";
					break;
				case 5:
					song_name = "Hypnotic Mazurka";
					break;
			}
			if(!frintezza.isBlocked())
			{
				frintezza.broadcastPacket(new ExShowScreenMessage(song_name, 3000, ScreenMessageAlign.TOP_CENTER, true, 1, -1, true));
				frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5007, song, _intervalOfFrintezzaSongs, 0));
				// Launch the song's effects (they start about 10 seconds after he starts to play)
				ThreadPoolManager.getInstance().scheduleGeneral(new SongEffectLaunched(getSongTargets(song), song, 10000), 10000);
			}
			// Schedule a new song to be played in 30-40 seconds...
			ThreadPoolManager.getInstance().scheduleGeneral(new Music(), _intervalOfFrintezzaSongs + Rnd.get(10000));
		}

		/**
		 * Depending on the song, returns the song's targets (either mobs or players)
		 */
		private GArray<L2Character> getSongTargets(int songId)
		{
			GArray<L2Character> targets = new GArray<L2Character>();
			if(songId < 4) // Target is the minions
			{
				if(weakScarlet != null && !weakScarlet.isDead())
				{
					targets.add(weakScarlet);
				}
				if(strongScarlet != null && !strongScarlet.isDead())
				{
					targets.add(strongScarlet);
				}
				for(int i = 0; i < 4; i++)
				{
					if(portraits[i] != null && !portraits[i].isDead())
					{
						targets.add(portraits[i]);
					}
					if(demons[i] != null && !demons[i].isDead())
					{
						targets.add(demons[i]);
					}
				}
			}
			else
			// Target is the players
			{
				for(L2Player pc : getPlayersInside())
				{
					if(!pc.isDead())
					{
						targets.add(pc);
					}
				}
			}
			return targets;
		}

		/**
		 * returns the chosen symphony for Frintezza to play
		 * If the minions are injured he has 40% to play a healing song
		 * If they are all dead, he will only play harmful player symphonies
		 */
		private static int getSong()
		{
			if(minionsNeedHeal())
			{
				return 1;
			}
			return Rnd.get(2, 6);
		}

		/**
		 * Checks if Frintezza's minions need heal (only major minions are checked) Return a "need heal" = true only 40% of the time
		 */
		private static boolean minionsNeedHeal()
		{
			if(Rnd.get(100) > 40)
			{
				return false;
			}
			if(weakScarlet != null && !weakScarlet.isAlikeDead() && weakScarlet.getCurrentHp() < weakScarlet.getMaxHp() * 2 / 3)
			{
				return true;
			}
			if(strongScarlet != null && !strongScarlet.isAlikeDead() && strongScarlet.getCurrentHp() < strongScarlet.getMaxHp() * 2 / 3)
			{
				return true;
			}
			for(int i = 0; i < 4; i++)
			{
				if(portraits[i] != null && !portraits[i].isDead() && portraits[i].getCurrentHp() < portraits[i].getMaxHp() / 3)
				{
					return true;
				}
				if(demons[i] != null && !demons[i].isDead() && demons[i].getCurrentHp() < demons[i].getMaxHp() / 3)
				{
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * The song was played, this class checks it's affects (if any)
	 */
	private static class SongEffectLaunched implements Runnable
	{
		private final GArray<L2Character> _targets;
		private final int _song, _currentTime;

		/**
		 * @param targets           - song's targets
		 * @param song              - song id 1-5
		 * @param currentTimeOfSong - skills during music play are consecutive, repeating
		 */
		public SongEffectLaunched(GArray<L2Character> targets, int song, int currentTimeOfSong)
		{
			_targets = targets;
			_song = song;
			_currentTime = currentTimeOfSong;
		}

		public void run()
		{
			if(frintezza == null)
			{
				return;
			}
			// If the song time is over stop this loop
			if(_currentTime > _intervalOfFrintezzaSongs)
			{
				return;
			}
			// Skills are consecutive, so call them again
			SongEffectLaunched songLaunched = new SongEffectLaunched(_targets, _song, _currentTime + _intervalOfFrintezzaSongs / 10);
			ThreadPoolManager.getInstance().scheduleGeneral(songLaunched, _intervalOfFrintezzaSongs / 10);
			frintezza.callSkill(SkillTable.getInstance().getInfo(5008, _song), _targets, false);
		}
	}

	/**
	 * If the dead boss is a Portrait, we delete it from the world, and it's demon as well.
	 * If the dead boss is Scarlet or Frintezza, we do a bossesAreDead() check to see if both Frintezza and Scarlet are dead.
	 */
	public static void OnDie(L2Character self, L2Character killer)
	{
		if(self == null || _state == null || _state.getState() != State.ALIVE)
		{
			return;
		}
		if(self == strongScarlet && _dieTask == null)
		{
			_dieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Die(1), 50);
		}
	}

	/**
	 * Class<?>  respawns a demon if it's portrait is not dead.
	 */
	private static class respawnDemons implements Runnable
	{
		public void run()
		{
			boolean isAllDead = true;
			for(int i = 0; i < 4; i++)
			{
				if(portraits[i] != null && !portraits[i].isDead())
				{
					isAllDead = false;
					if(demons[i] == null || demons[i].isDead())
					{
						demons[i] = spawn(demonSpawns[i]);
						L2Character target = getRandomPlayer();
						if(target != null)
						{
							demons[i].getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 10000);
						}
					}
				}
			}
			if(!isAllDead)
			{
				ThreadPoolManager.getInstance().scheduleGeneral(new respawnDemons(), FWF_INTERVALOFNEXTMONSTER);
			}
		}
	}

	private static class ThirdMorph implements Runnable
	{
		private int _taskId = 0;
		private static int _angle = 0;
		private static Location loc = null;

		public ThirdMorph(int taskId)
		{
			_taskId = taskId;
		}

		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
						_angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for(L2Player pc : getPlayersInside())
						{
							pc.enterMovieMode();
						}
						blockAll(true);
						frintezza.broadcastPacket(new MagicSkillCanceled(frintezza.getObjectId()));
						frintezza.broadcastPacket(new SocialAction(frintezza.getObjectId(), 4));
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(2), 100);
						break;
					case 2:
						showSocialActionMovie(frintezza, 250, 120, 15, 0, 1000, 0);
						showSocialActionMovie(frintezza, 250, 120, 15, 0, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(3), 6500);
						break;
					case 3:
						frintezza.broadcastPacket(new MagicSkillUse(frintezza, frintezza, 5006, 1, 34000, 0));
						showSocialActionMovie(frintezza, 500, 70, 15, 3000, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(4), 3000);
						break;
					case 4:
						showSocialActionMovie(frintezza, 2500, 90, 12, 6000, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(5), 3000);
						break;
					case 5:
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 1000, 0);
						showSocialActionMovie(weakScarlet, 250, _angle, 12, 0, 10000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(6), 500);
						break;
					case 6:
						weakScarlet.doDie(weakScarlet);
						showSocialActionMovie(weakScarlet, 450, _angle, 14, 8000, 8000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(7), 6250);
						break;
					case 7:
						loc = weakScarlet.getLoc();
						weakScarlet.deleteMe();
						weakScarlet = null;
						loc.setId(_strongScarletId);
						strongScarlet = spawn(loc);
						block(strongScarlet, true);
						showSocialActionMovie(strongScarlet, 450, _angle, 12, 500, 14000, 2);
						ThreadPoolManager.getInstance().scheduleGeneral(new ThirdMorph(9), 5000);
						break;
					case 9:
						blockAll(false);
						for(L2Player pc : getPlayersInside())
						{
							pc.leaveMovieMode();
						}
						L2Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(strongScarlet, strongScarlet, false, false);
						break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static class SecondMorph implements Runnable
	{
		private int _taskId = 0;

		public SecondMorph(int taskId)
		{
			_taskId = taskId;
		}

		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
						int angle = Math.abs((weakScarlet.getHeading() < 32768 ? 180 : 540) - (int) (weakScarlet.getHeading() / 182.044444444));
						for(L2Player pc : getPlayersInside())
						{
							pc.enterMovieMode();
						}
						blockAll(true);
						showSocialActionMovie(weakScarlet, 500, angle, 5, 500, 15000, 0);
						ThreadPoolManager.getInstance().scheduleGeneral(new SecondMorph(2), 2000);
						break;
					case 2:
						weakScarlet.broadcastPacket(new SocialAction(weakScarlet.getObjectId(), 1));
						weakScarlet.setCurrentHp(weakScarlet.getMaxHp() * 3 / 4, false);
						weakScarlet.setRHandId(_frintezzasSwordId);
						for(L2Player pc : getPlayersInside())
						{
							pc.sendPacket(new NpcInfo(weakScarlet, pc));
						}
						ThreadPoolManager.getInstance().scheduleGeneral(new SecondMorph(3), 5500);
						break;
					case 3:
						weakScarlet.broadcastPacket(new SocialAction(weakScarlet.getObjectId(), 4));
						blockAll(false);
						L2Skill skill = SkillTable.getInstance().getInfo(5017, 1);
						skill.getEffects(weakScarlet, weakScarlet, false, false);
						for(L2Player pc : getPlayersInside())
						{
							pc.leaveMovieMode();
						}
						break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	//TODO вынести в AI демонов, либо сделать скиллом

	private static class doSkill implements Runnable
	{
		private final L2Character _caster;
		private final int _interval, _range;

		public doSkill(L2Character caster, int interval, int range)
		{
			_caster = caster;
			_interval = interval;
			_range = range;
		}

		public void run()
		{
			if(_caster == null || _caster.isDead())
			{
				return;
			}
			try
			{
				L2Object tempTarget = _caster.getTarget();
				if(tempTarget == null || !(tempTarget instanceof L2Character))
				{
					tempTarget = _caster;
				}
				int x = tempTarget.getX() + Rnd.get(_range) - _range / 2, y = tempTarget.getY() + Rnd.get(_range) - _range / 2, z = tempTarget.getZ();
				if(_caster.getDistance(x, y) > _range && getZone().checkIfInZone(tempTarget))
				{
					_caster.broadcastPacket(new MagicSkillUse(_caster, (L2Character) tempTarget, 1086, 1, 0, 0));
					_caster.decayMe();
					_caster.setXYZ(x, y, z);
					_caster.spawnMe();
					_caster.setTarget(tempTarget);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			ThreadPoolManager.getInstance().scheduleGeneral(new doSkill(_caster, _interval, _range), _interval + Rnd.get(500));
		}
	}

	private static class Die implements Runnable
	{
		private int _taskId = 0;

		public Die(int taskId)
		{
			_taskId = taskId;
		}

		public void run()
		{
			try
			{
				switch(_taskId)
				{
					case 1:
						blockAll(true);
						deletePortrait();
						int _angle = Math.abs((strongScarlet.getHeading() < 32768 ? 180 : 540) - (int) (strongScarlet.getHeading() / 182.044444444));
						showSocialActionMovie(strongScarlet, 300, _angle - 180, 5, 0, 7000, 0);
						showSocialActionMovie(strongScarlet, 200, _angle, 85, 4000, 10000, 0);
						_dieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Die(2), 7500);
						break;
					case 2:
						showSocialActionMovie(frintezza, 100, 120, 5, 0, 7000, 0);
						showSocialActionMovie(frintezza, 100, 90, 5, 5000, 15000, 0);
						_dieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Die(3), 6000);
						break;
					case 3:
						showSocialActionMovie(frintezza, 900, 90, 25, 7000, 10000, 0);
						//frintezza.broadcastPacket(new MagicSkillCanceled(frintezza.getObjectId()));
						frintezza.doDie(frintezza);
						frintezza = null;
						cube = spawn(cubeSpawn);
						_dieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Die(4), 7000);
						break;
					case 4:
						for(L2Player pc : getPlayersInside())
						{
							pc.leaveMovieMode();
						}
						_dieTask = ThreadPoolManager.getInstance().scheduleGeneral(new Die(5), 600000);
						break;
					case 5:
						setUnspawn();
						break;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Class<?>  ends the activity of the Bosses after a interval of time Exits the battle field in any way...
	 */
	private static class ActivityTimeEnd implements Runnable
	{
		public void run()
		{
			setUnspawn();
		}
	}

	/**
	 * Clean Frintezza's lair.
	 */
	public static void setUnspawn()
	{
		banishForeigners();
		_state.setState(EpicBossState.State.DEAD);
		_state.update();
		Log.add("Frintezza died", "bosses");
		if(frintezza != null)
		{
			frintezza.deleteMe();
		}
		if(weakScarlet != null)
		{
			weakScarlet.deleteMe();
		}
		if(strongScarlet != null)
		{
			strongScarlet.deleteMe();
		}
		if(cube != null)
		{
			cube.deleteMe();
		}
		frintezza = weakScarlet = strongScarlet = cube = null;
		deletePortrait();
		if(_monsterSpawnTask != null)
		{
			_monsterSpawnTask.cancel(true);
			_monsterSpawnTask = null;
		}
		if(_intervalEndTask != null)
		{
			_intervalEndTask.cancel(true);
			_intervalEndTask = null;
		}
		if(_activityTimeEndTask != null)
		{
			_activityTimeEndTask.cancel(true);
			_activityTimeEndTask = null;
		}
		if(_dieTask != null)
		{
			_dieTask.cancel(false);
			_dieTask = null;
		}
		setIntervalEndTask();
	}

	private static void deletePortrait()
	{
		for(int i = 0; i < 4; i++)
		{
			if(portraits[i] != null)
			{
				portraits[i].deleteMe();
				portraits[i] = null;
			}
			if(demons[i] != null)
			{
				demons[i].deleteMe();
				demons[i] = null;
			}
		}
	}

	private static void blockAll(boolean flag)
	{
		block(frintezza, flag);
		block(weakScarlet, flag);
		block(strongScarlet, flag);
		for(int i = 0; i < 4; i++)
		{
			block(portraits[i], flag);
			block(demons[i], flag);
		}
	}

	private static void block(L2NpcInstance npc, boolean flag)
	{
		if(npc == null || npc.isDead())
		{
			return;
		}
		if(flag)
		{
			npc.abortAttack(true, false);
			npc.abortCast(true);
			npc.setTarget(null);
			if(npc.isMoving)
			{
				npc.stopMove();
			}
			npc.block();
		}
		else
		{
			npc.unblock();
		}
		npc.setIsInvul(flag);
	}

	/**
	 * Creates a thread to initialize Frintezza again... until this loops ends, no one can enter the lair.
	 */
	public static void setIntervalEndTask()
	{
		if(!_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			_state.setRespawnDate(Rnd.get(FWF_FIXINTERVALOFFRINTEZZA, FWF_FIXINTERVALOFFRINTEZZA + FWF_RANDOMINTERVALOFFRINTEZZA));
			_state.setState(EpicBossState.State.INTERVAL);
			_state.update();
		}
		_intervalEndTask = ThreadPoolManager.getInstance().scheduleGeneral(new IntervalEnd(), _state.getInterval());
	}

	/**
	 * Calls for a re-initialization when time comes, only then can players enter the lair.
	 */
	private static class IntervalEnd implements Runnable
	{
		public void run()
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}

	private static void banishForeigners()
	{
		for(L2Player player : getPlayersInside())
		{
			if(!player.isGM())
			{
				player.teleToClosestTown();
			}
		}
	}

	private static L2Player getRandomPlayer()
	{
		GArray<L2Player> list = getZone().getInsidePlayers();
		if(list.isEmpty())
		{
			return null;
		}
		return list.get(Rnd.get(list.size()));
	}

	private static GArray<L2Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}

	public static L2Zone getZone()
	{
		return _zone;
	}

	public static boolean isEnableEnterToLair()
	{
		return _state.getState() == EpicBossState.State.NOTSPAWN;
	}

	public void onLoad()
	{
		init();
	}

	public void onReload()
	{
		getZone().getListenerEngine().removeMethodInvokedListener(_zoneListener);
		if(weakScarlet != null)
		{
			weakScarlet.getListenerEngine().removePropertyChangeListener(_currentHpListener);
		}
	}

	public void onShutdown()
	{
	}
}