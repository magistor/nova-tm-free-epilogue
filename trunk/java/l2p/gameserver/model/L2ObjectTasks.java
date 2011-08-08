package l2p.gameserver.model;

import l2p.Config;
import l2p.common.ThreadPoolManager;
import l2p.extensions.multilang.CustomMessage;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Zone.ZoneType;
import l2p.gameserver.model.entity.siege.territory.TerritorySiege;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.instances.L2TerritoryFlagInstance;
import l2p.gameserver.model.instances.L2TrapInstance;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.ExBrExtraUserInfo;
import l2p.gameserver.serverpackets.ExBrPremiumState;
import l2p.gameserver.serverpackets.ExShowScreenMessage;
import l2p.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2p.gameserver.serverpackets.MagicSkillLaunched;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.serverpackets.UserInfo;
import l2p.util.GArray;
import l2p.util.Location;
import l2p.util.Log;
import l2p.util.Rnd;

import java.util.logging.Level;
import java.util.logging.Logger;

public class L2ObjectTasks
{
	static final Logger _log = Logger.getLogger(L2ObjectTasks.class.getName());

	public static class SoulConsumeTask implements Runnable
	{
		private final long playerStoreId;

		public SoulConsumeTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.setConsumedSouls(player.getConsumedSouls() + 1, null);
		}
	}

	public static class ReturnTerritoryFlagTask implements Runnable
	{
		private final long playerStoreId;

		public ReturnTerritoryFlagTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			if(player.isTerritoryFlagEquipped())
			{
				L2ItemInstance flag = player.getActiveWeaponInstance();
				if(flag != null && flag.getCustomType1() != 77) // 77 это эвентовый флаг
				{
					L2TerritoryFlagInstance flagNpc = TerritorySiege.getNpcFlagByItemId(flag.getItemId());
					flagNpc.returnToCastle(player);
				}
			}
		}
	}

	public static class PvPFlagTask implements Runnable
	{
		private final long playerStoreId;

		public PvPFlagTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			try
			{
				long diff = Math.abs(System.currentTimeMillis() - player.getlastPvpAttack());
				if(diff > Config.PVP_TIME)
				{
					player.stopPvPFlag();
				}
				else if(diff > Config.PVP_TIME - 20000)
				{
					player.updatePvPFlag(2);
				}
				else
				{
					player.updatePvPFlag(1);
				}
			}
			catch(Exception e)
			{
				_log.log(Level.WARNING, "error in pvp flag task:", e);
			}
		}
	}

	public static class LookingForFishTask implements Runnable
	{
		boolean _isNoob, _isUpperGrade;
		int _fishType;
		int _fishGutsCheck;
		long playerStoreId, _endTaskTime;

		protected LookingForFishTask(L2Player player, int fishWaitTime, int fishGutsCheck, int fishType, boolean isNoob, boolean isUpperGrade)
		{
			_fishGutsCheck = fishGutsCheck;
			_endTaskTime = System.currentTimeMillis() + fishWaitTime + 10000;
			_fishType = fishType;
			_isNoob = isNoob;
			_isUpperGrade = isUpperGrade;
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			if(System.currentTimeMillis() >= _endTaskTime)
			{
				player.endFishing(false);
				return;
			}
			if(_fishType == -1)
			{
				return;
			}
			int check = Rnd.get(1000);
			if(_fishGutsCheck > check)
			{
				player.stopLookingForFishTask();
				player.startFishCombat(_isNoob, _isUpperGrade);
			}
		}
	}

	public static class BonusTask implements Runnable
	{
		private final long playerStoreId;

		public BonusTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.getNetConnection().setBonus(1);
			player.restoreBonus();
			if(player.getParty() != null)
			{
				player.getParty().recalculatePartyData();
			}
			String msg = new CustomMessage("scripts.services.RateBonus.LuckEnded", player).toString();
			player.sendPacket(new ExShowScreenMessage(msg, 10000, ScreenMessageAlign.TOP_CENTER, true), new ExBrPremiumState(player, 0));
			player.sendMessage(msg);
		}
	}

	public static class WaterTask implements Runnable
	{
		private final long playerStoreId;

		public WaterTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			if(player.isDead() || !player.isInZone(ZoneType.water))
			{
				player.stopWaterTask();
				return;
			}
			double reduceHp = player.getMaxHp() < 100 ? 1 : player.getMaxHp() / 100;
			player.reduceCurrentHp(reduceHp, player, null, false, false, true, false);
			player.sendPacket(new SystemMessage(SystemMessage.YOU_RECEIVED_S1_DAMAGE_BECAUSE_YOU_WERE_UNABLE_TO_BREATHE).addNumber((long) reduceHp));
		}
	}

	public static class KickTask implements Runnable
	{
		private final long playerStoreId;

		public KickTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.setOfflineMode(false);
			player.logout(false, false, true, false);
		}
	}

	public static class TeleportTask implements Runnable
	{
		private final long playerStoreId;
		private Location _loc;
                int _reflection;

		public TeleportTask(L2Player player, Location p, int reflection)
		{
			playerStoreId = player.getStoredId();
			_loc = p;
                        _reflection = reflection;
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
				
			player.unsetVar("jailedFrom");
			player.unsetVar("jailed");				
			player.teleToLocation(_loc);
		}
	}

	public static class UserInfoTask implements Runnable
	{
		private final long playerStoreId;

		public UserInfoTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.sendPacket(new UserInfo(player), new ExBrExtraUserInfo(player));
			player._userInfoTask = null;
		}
	}

	public static class BroadcastCharInfoTask implements Runnable
	{
		private final long playerStoreId;

		public BroadcastCharInfoTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.broadcastCharInfo();
			player._broadcastCharInfoTask = null;
		}
	}

	public static class EndSitDownTask implements Runnable
	{
		private final long playerStoreId;

		public EndSitDownTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.sittingTaskLaunched = false;
			player.getAI().clearNextAction();
		}
	}

	public static class EndStandUpTask implements Runnable
	{
		private final long playerStoreId;

		public EndStandUpTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player.sittingTaskLaunched = false;
			player._isSitting = false;
			if(!player.getAI().setNextIntention())
			{
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
			}
		}
	}

	public static class InventoryEnableTask implements Runnable
	{
		private final long playerStoreId;

		public InventoryEnableTask(L2Player player)
		{
			playerStoreId = player.getStoredId();
		}

		public void run()
		{
			L2Player player = L2ObjectsStorage.getAsPlayer(playerStoreId);
			if(player == null)
			{
				return;
			}
			player._inventoryDisable = false;
		}
	}

	public static class AltMagicUseTask implements Runnable
	{
		public final L2Skill _skill;
		private final long chaSId, targetSId;

		public AltMagicUseTask(L2Character character, L2Character target, L2Skill skill)
		{
			chaSId = character.getStoredId();
			targetSId = target.getStoredId();
			_skill = skill;
		}

		public void run()
		{
			L2Character cha, target;
			if((cha = L2ObjectsStorage.getAsCharacter(chaSId)) == null || (target = L2ObjectsStorage.getAsCharacter(targetSId)) == null)
			{
				return;
			}
			cha.altOnMagicUseTimer(target, _skill);
		}
	}

	public static class CancelAttackStanceTask implements Runnable
	{
		private final long charStoreId;

		public CancelAttackStanceTask(L2Character character)
		{
			charStoreId = character.getStoredId();
		}

		public void run()
		{
			L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null)
			{
				return;
			}
			character.stopAttackStanceTask();
		}
	}

	public static class EnableSkillTask implements Runnable
	{
		private final int _skillId;
		private final long charStoreId;

		public EnableSkillTask(L2Character character, int skillId)
		{
			charStoreId = character.getStoredId();
			_skillId = skillId;
		}

		public void run()
		{
			try
			{
				L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
				if(character == null)
				{
					return;
				}
				character.enableSkill(_skillId);
			}
			catch(Throwable e)
			{
				_log.log(Level.SEVERE, "", e);
			}
		}
	}

	public static class CastEndTimeTask implements Runnable
	{
		private final long charStoreId;

		public CastEndTimeTask(L2Character character)
		{
			charStoreId = character.getStoredId();
		}

		public void run()
		{
			L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null)
			{
				return;
			}
			character.onCastEndTime();
		}
	}

	public static class HitTask implements Runnable
	{
		boolean _crit, _miss, _shld, _soulshot, _unchargeSS, _notify;
		int _damage;
		private final long charStoreId, targetStoreId;

		public HitTask(L2Character cha, L2Character target, int damage, boolean crit, boolean miss, boolean soulshot, boolean shld, boolean unchargeSS, boolean notify)
		{
			charStoreId = cha.getStoredId();
			targetStoreId = target.getStoredId();
			_damage = damage;
			_crit = crit;
			_shld = shld;
			_miss = miss;
			_soulshot = soulshot;
			_unchargeSS = unchargeSS;
			_notify = notify;
		}

		public void run()
		{
			L2Character character, target;
			if((character = L2ObjectsStorage.getAsCharacter(charStoreId)) == null || (target = L2ObjectsStorage.getAsCharacter(targetStoreId)) == null)
			{
				return;
			}
			try
			{
				if(character.isAttackAborted())
				{
					character._attackEndTime = 0;
					return;
				}
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			try
			{
				character.onHitTimer(target, _damage, _crit, _miss, _soulshot, _shld, _unchargeSS);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			try
			{
				if(_notify)
				{
					character._attackEndTime = 0;
					character.getAI().notifyEvent(CtrlEvent.EVT_READY_TO_ACT);
				}
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public static class MagicUseTask implements Runnable
	{
		public boolean _forceUse;
		private final long charStoreId;

		public MagicUseTask(L2Character cha, boolean forceUse)
		{
			charStoreId = cha.getStoredId();
			_forceUse = forceUse;
		}

		public void run()
		{
			L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null)
			{
				return;
			}
			if((character.isPet() || character.isSummon()) && character.getPlayer() == null)
			{
				character.clearCastVars();
				return;
			}
			character.onMagicUseTimer(character.getCastingTarget(), character.getCastingSkill(), _forceUse);
		}
	}

	public static class MagicLaunchedTask implements Runnable
	{
		public boolean _forceUse;
		private final long charStoreId;

		public MagicLaunchedTask(L2Character cha, boolean forceUse)
		{
			charStoreId = cha.getStoredId();
			_forceUse = forceUse;
		}

		public void run()
		{
			L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null)
			{
				return;
			}
			L2Skill castingSkill = character.getCastingSkill();
			if(castingSkill == null || (character.isPet() || character.isSummon()) && character.getPlayer() == null)
			{
				character.clearCastVars();
				return;
			}
			GArray<L2Character> targets = castingSkill.getTargets(character, character.getCastingTarget(), _forceUse);
			character.broadcastPacket(new MagicSkillLaunched(character.getObjectId(), castingSkill.getDisplayId(), castingSkill.getDisplayLevel(), targets, castingSkill.isOffensive()));
		}
	}

	public static class NotifyAITask implements Runnable
	{
		private final CtrlEvent _evt;
		private final Object _agr0;
		private final Object _agr1;
		private final long charStoreId;

		public NotifyAITask(L2Character cha, CtrlEvent evt, Object agr0, Object agr1)
		{
			charStoreId = cha.getStoredId();
			_evt = evt;
			_agr0 = agr0;
			_agr1 = agr1;
		}

		public void run()
		{
			L2Character character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null || (character.isPet() || character.isSummon()) && character.getPlayer() == null)
			{
				return;
			}
			try
			{
				character.getAI().notifyEvent(_evt, _agr0, _agr1);
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
	}

	public static class MoveNextTask implements Runnable
	{
		private float alldist, donedist;
		private long charStoreId;

		public MoveNextTask(L2Character _character)
		{
			charStoreId = _character.getStoredId();
		}

		public void updateStoreId(long l)
		{
			charStoreId = l;
		}

		public MoveNextTask setDist(double dist)
		{
			alldist = (float) dist;
			donedist = 0;
			return this;
		}

		public void run()
		{
			L2Character follow_target = null, character = L2ObjectsStorage.getAsCharacter(charStoreId);
			if(character == null || !character.isMoving)
			{
				return;
			}
			synchronized(character._targetRecorder)
			{
				float speed = character.getMoveSpeed();
				if(speed <= 0)
				{
					character.stopMove();
					return;
				}
				long now = System.currentTimeMillis();
				if(character.isFollow)
				{
					follow_target = character.getFollowTarget();
					if(follow_target == null)
					{
						character.stopMove();
						return;
					}
					if(character.isInRangeZ(follow_target, character._offset) && GeoEngine.canSeeTarget(character, follow_target, false))
					{
						character.stopMove();
						ThreadPoolManager.getInstance().executeAi(new NotifyAITask(character, CtrlEvent.EVT_ARRIVED_TARGET, null, null), character.isPlayable());
						if(!character.isPlayer())
						{
							character.validateLocation(1);
						}
						return;
					}
				}
				if(alldist == 0)
				{
					character.moveNext(false);
					return;
				}
				donedist += (now - character._startMoveTime) * character._previousSpeed / 1000f;
				double done = donedist / alldist;
				if(done < 0)
				{
					done = 0;
				}
				if(done >= 1)
				{
					/*if(character.isPlayer())
					{
						_log.info("done " + done + " moveList.size() = " + character.moveList.size());
						_log.info("loc " + character.getLoc() + " moveList " + character.moveList.get(character.moveList.size() - 1).geo2world());
					}*/
					character.moveNext(false);
					return;
				}
				Location loc = null;
				try
				{
					int index = (int) (character.moveList.size() * done);
					if(index >= character.moveList.size())
					{
						index = character.moveList.size() - 1;
					}
					if(index < 0)
					{
						index = 0;
					}
					/*if(character.isPlayer())
					{
						_log.info("index " + index + " moveList.size() = " + character.moveList.size());
					}*/
					loc = character.moveList.get(index).clone().geo2world();
					if(!character.isFlying() && !character.isInVehicle() && !character.isSwimming() && !character.isVehicle())
					{
						if(loc.z - character.getZ() > 256)
						{
							String bug_text = "geo bug 1 at: " + character.getLoc() + " => " + loc.x + "," + loc.y + "," + loc.z + "\tAll path: " + character.moveList.get(0) + " => " + character.moveList.get(character.moveList.size() - 1);
							Log.add(bug_text, "geo");
							if(character.isPlayer() && character.getAccessLevel() >= 100)
							{
								character.sendMessage(bug_text);
							}
							character.stopMove();
							return;
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				// Проверяем, на всякий случай
				if(!character.isMoving || loc == null)
				{
					return;
				}
				character.setLoc(loc, true);
				// В процессе изменения координат, мы остановились
				if(!character.isMoving)
				{
					return;
				}
				if(character.isFollow && now - character._followTimestamp > (character._forestalling ? 500 : 1000) && follow_target != null && !follow_target.isInRange(character.movingDestTempPos, Math.max(100, character._offset)))
				{
					if(Math.abs(character.getZ() - loc.z) > 1000 && !character.isFlying())
					{
						character.sendPacket(Msg.CANNOT_SEE_TARGET);
						character.stopMove();
						return;
					}
					if(character.buildPathTo(follow_target.getX(), follow_target.getY(), follow_target.getZ(), character._offset, true, true))
					{
						character.movingDestTempPos.set(follow_target.getX(), follow_target.getY(), follow_target.getZ());
					}
					else
					{
						character.stopMove();
						return;
					}
					character.moveNext(true);
					return;
				}
				/*if(character.isPlayer())
				{
					_log.info("character.getMoveTickInterval() = " + character.getMoveTickInterval());
				}*/
				character._previousSpeed = speed;
				character._startMoveTime = now;
				character._moveTask = ThreadPoolManager.getInstance().scheduleMove(character._moveTaskRunnable, character.getMoveTickInterval());
			}
		}
	}

	public static class NotifyFactionTask implements Runnable
	{
		private final long npcStoreId, attackerStoreId;
		private final int _damage;

		public NotifyFactionTask(L2NpcInstance npc, L2Character attacker, int damage)
		{
			npcStoreId = npc.getStoredId();
			attackerStoreId = attacker.getStoredId();
			_damage = damage;
		}

		public void run()
		{
			L2NpcInstance npc;
			L2Character attacker;
			if((npc = L2ObjectsStorage.getAsNpc(npcStoreId)) == null || (attacker = L2ObjectsStorage.getAsCharacter(attackerStoreId)) == null)
			{
				return;
			}
			try
			{
				String faction_id = npc.getFactionId();
				for(L2NpcInstance friend : npc.ActiveFriendTargets(false))
				{
					if(friend != null && faction_id.equalsIgnoreCase(friend.getFactionId()))
					{
						friend.onClanAttacked(npc, attacker, _damage);
					}
				}
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
	}

	public static class RandomAnimationTask implements Runnable
	{
		private final long npcStoreId;
		private final int interval;

		public RandomAnimationTask(L2NpcInstance npc)
		{
			npcStoreId = npc.getStoredId();
			interval = 1000 * Rnd.get(Config.MIN_NPC_ANIMATION, Config.MAX_NPC_ANIMATION);
			ThreadPoolManager.getInstance().scheduleAi(this, interval, false);
		}

		public void run()
		{
			L2NpcInstance npc = L2ObjectsStorage.getAsNpc(npcStoreId);
			if(npc == null)
			{
				return;
			}
			if(!npc.isDead() && !npc.isMoving)
			{
				npc.onRandomAnimation();
			}
			ThreadPoolManager.getInstance().scheduleAi(this, interval, false);
		}
	}

	public static class TrapDestroyTask implements Runnable
	{
		private final long trapStoreId;

		public TrapDestroyTask(L2TrapInstance trap)
		{
			trapStoreId = trap.getStoredId();
		}

		public void run()
		{
			L2TrapInstance trap = (L2TrapInstance) L2ObjectsStorage.get(trapStoreId);
			if(trap != null)
			{
				trap.destroy();
			}
		}
	}
}