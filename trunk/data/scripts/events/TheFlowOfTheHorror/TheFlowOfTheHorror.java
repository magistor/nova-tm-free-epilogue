package events.TheFlowOfTheHorror;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2MonsterInstance;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.gameserver.templates.L2NpcTemplate;
import l2p.util.Files;
import l2p.util.GArray;
import l2p.util.Location;

public class TheFlowOfTheHorror extends Functions implements ScriptFile
{
	private static int Gilmore = 30754;
	private static int Shackle = 20235;
	private static long oldGilmore;
	private static int _stage = 1;
	private static GArray<L2MonsterInstance> _spawns = new GArray<L2MonsterInstance>();
	private static GArray<Location> points11 = new GArray<Location>();
	private static GArray<Location> points12 = new GArray<Location>();
	private static GArray<Location> points13 = new GArray<Location>();
	private static GArray<Location> points21 = new GArray<Location>();
	private static GArray<Location> points22 = new GArray<Location>();
	private static GArray<Location> points23 = new GArray<Location>();
	private static GArray<Location> points31 = new GArray<Location>();
	private static GArray<Location> points32 = new GArray<Location>();
	private static GArray<Location> points33 = new GArray<Location>();

	public void onLoad()
	{
		//Рукав 1, линия 1
		points11.add(new Location(84211, 117965, -3020));
		points11.add(new Location(83389, 117590, -3036));
		points11.add(new Location(82226, 117051, -3150));
		points11.add(new Location(80902, 116155, -3533));
		points11.add(new Location(79832, 115784, -3733));
		points11.add(new Location(78442, 116510, -3823));
		points11.add(new Location(76299, 117355, -3786));
		points11.add(new Location(74244, 117674, -3785));
		//Рукав 1, линия 2
		points12.add(new Location(84231, 117597, -3020));
		points12.add(new Location(82536, 116986, -3093));
		points12.add(new Location(79428, 116341, -3749));
		points12.add(new Location(76970, 117362, -3771));
		points12.add(new Location(74322, 117845, -3767));
		//Рукав 1, линия 3
		points13.add(new Location(83962, 118387, -3022));
		points13.add(new Location(81960, 116925, -3216));
		points13.add(new Location(80223, 116059, -3665));
		points13.add(new Location(78214, 116783, -3854));
		points13.add(new Location(76208, 117462, -3791));
		points13.add(new Location(74278, 117454, -3804));
		//Рукав 2, линия 1
		points21.add(new Location(79192, 111481, -3011));
		points21.add(new Location(79014, 112396, -3090));
		points21.add(new Location(79309, 113692, -3437));
		points21.add(new Location(79350, 115337, -3758));
		points21.add(new Location(78390, 116309, -3772));
		points21.add(new Location(76794, 117092, -3821));
		points21.add(new Location(74451, 117623, -3797));
		//Рукав 2, линия 2
		points22.add(new Location(79297, 111456, -3017));
		points22.add(new Location(79020, 112217, -3087));
		points22.add(new Location(79167, 113236, -3289));
		points22.add(new Location(79513, 115408, -3752));
		points22.add(new Location(78555, 116816, -3812));
		points22.add(new Location(76932, 117277, -3781));
		points22.add(new Location(75422, 117788, -3755));
		points22.add(new Location(74223, 117898, -3753));
		//Рукав 2, линия 3
		points23.add(new Location(79635, 110741, -3003));
		points23.add(new Location(78994, 111858, -3061));
		points23.add(new Location(79088, 112949, -3226));
		points23.add(new Location(79424, 114499, -3674));
		points23.add(new Location(78913, 116266, -3779));
		points23.add(new Location(76930, 117137, -3819));
		points23.add(new Location(75533, 117569, -3781));
		points23.add(new Location(74255, 117398, -3804));
		//Рукав 3, линия 1
		points31.add(new Location(83128, 111358, -3663));
		points31.add(new Location(81538, 111896, -3631));
		points31.add(new Location(80312, 113837, -3752));
		points31.add(new Location(79012, 115998, -3772));
		points31.add(new Location(77377, 117052, -3812));
		points31.add(new Location(75394, 117608, -3772));
		points31.add(new Location(73998, 117647, -3784));
		//Рукав 3, линия 2
		points32.add(new Location(83245, 110790, -3772));
		points32.add(new Location(81832, 111379, -3641));
		points32.add(new Location(81405, 112403, -3648));
		points32.add(new Location(79827, 114496, -3752));
		points32.add(new Location(78174, 116968, -3821));
		points32.add(new Location(75944, 117653, -3777));
		points32.add(new Location(74379, 117939, -3755));
		//Рукав 3, линия 3
		points33.add(new Location(82584, 111930, -3568));
		points33.add(new Location(81389, 111989, -3647));
		points33.add(new Location(80129, 114044, -3748));
		points33.add(new Location(79190, 115579, -3743));
		points33.add(new Location(77989, 116811, -3849));
		points33.add(new Location(76009, 117405, -3800));
		points33.add(new Location(74113, 117441, -3797));
		if(isActive())
		{
			activateAI();
			System.out.println("Loaded Event: The Flow Of The Horror [state: activated]");
		}
		else
		{
			System.out.println("Loaded Event: The Flow Of The Horror [state: deactivated]");
		}
	}

	public static void spawnNewWave()
	{
		spawn(Shackle, points11);
		spawn(Shackle, points12);
		spawn(Shackle, points13);
		spawn(Shackle, points21);
		spawn(Shackle, points22);
		spawn(Shackle, points23);
		spawn(Shackle, points31);
		spawn(Shackle, points32);
		spawn(Shackle, points33);
		_stage = 2;
	}

	private static void spawn(int id, GArray<Location> points)
	{
		L2NpcTemplate template = NpcTable.getTemplate(id);
		L2MonsterInstance monster = new L2MonsterInstance(IdFactory.getInstance().getNextId(), template);
		monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
		monster.setXYZ(points.get(0).x, points.get(0).y, points.get(0).z);
		MonstersAI ai = new MonstersAI(monster);
		monster.setAI(ai);
		monster.spawnMe();
		ai.setPoints(points);
		ai.startAITask();
		_spawns.add(monster);
	}

	private void activateAI()
	{
		L2NpcInstance target = L2ObjectsStorage.getByNpcId(Gilmore);
		if(target != null)
		{
			oldGilmore = target.getStoredId();
			target.decayMe();
			L2NpcTemplate template = NpcTable.getTemplate(Gilmore);
			L2MonsterInstance monster = new L2MonsterInstance(IdFactory.getInstance().getNextId(), template);
			monster.setCurrentHpMp(monster.getMaxHp(), monster.getMaxMp(), true);
			monster.setXYZ(73329, 117705, -3741);
			GilmoreAI ai = new GilmoreAI(monster);
			monster.setAI(ai);
			monster.spawnMe();
			ai.startAITask();
			_spawns.add(monster);
		}
	}

	private void deactivateAI()
	{
		for(L2MonsterInstance monster : _spawns)
		{
			if(monster != null)
			{
				monster.getAI().stopAITask();
				monster.deleteMe();
			}
		}
		L2NpcInstance GilmoreInstance = L2ObjectsStorage.getAsNpc(oldGilmore);
		if(GilmoreInstance != null)
		{
			GilmoreInstance.spawnMe();
		}
	}

	/**
	 * Читает статус эвента из базы.
	 *
	 * @return
	 */
	private static boolean isActive()
	{
		return ServerVariables.getString("TheFlowOfTheHorror", "off").equalsIgnoreCase("on");
	}

	/**
	 * Запускает эвент
	 */
	public void startEvent()
	{
		L2Player player = (L2Player) getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if(!isActive())
		{
			ServerVariables.set("TheFlowOfTheHorror", "on");
			activateAI();
			System.out.println("Event 'The Flow Of The Horror' started.");
			//Announcements.getInstance().announceByCustomMessage("scripts.events.CofferofShadows.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'The Flow Of The Horror' already started.");
		}
		show(Files.read("data/html/admin/events.htm", player), player);
	}

	/**
	 * Останавливает эвент
	 */
	public void stopEvent()
	{
		L2Player player = (L2Player) getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if(isActive())
		{
			ServerVariables.unset("TheFlowOfTheHorror");
			deactivateAI();
			System.out.println("Event 'The Flow Of The Horror' stopped.");
			//Announcements.getInstance().announceByCustomMessage("scripts.events.CofferofShadows.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'The Flow Of The Horror' not started.");
		}
		show(Files.read("data/html/admin/events.htm", player), player);
	}

	public void onReload()
	{
		deactivateAI();
	}

	public void onShutdown()
	{
		deactivateAI();
	}

	public static int getStage()
	{
		return _stage;
	}

	public static void setStage(int stage)
	{
		_stage = stage;
	}
}