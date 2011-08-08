package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Effect;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.tables.SkillTable;
import l2p.util.GArray;
import l2p.util.Rnd;

public class TarBeetle extends DefaultAI
{
	private long _spawnTime = 0;
	private boolean _spawned = false;
	private static final int DESPAWN_TIME = 300000;

	public TarBeetle(L2NpcInstance actor)
	{
		super(actor);
	}

	@Override
	protected boolean thinkActive()
	{
		L2NpcInstance actor = getActor();
		if(actor == null)
		{
			return true;
		}

		for(L2Player player : L2World.getAroundPlayers(actor, 300, 200))
		{
			if(Rnd.chance(5))
			{
				addEffect(actor, player);
			}
		}

		if(_def_think)
		{
			doTask();
			_spawned = false;
			return true;
		}

		if(!_spawned)
		{
			_spawned = true;
			_spawnTime = System.currentTimeMillis() + DESPAWN_TIME;
		}

		if(_spawnTime != 0 && _spawned && _spawnTime < System.currentTimeMillis())
		{
			actor.onDecay();
			return true;
		}
		return true;
	}

	private void addEffect(L2NpcInstance actor, L2Player player)
	{
		GArray<L2Effect> effect = player.getEffectList().getEffectsBySkillId(6142);
		if(effect != null)
		{
			int level = effect.get(0).getSkill().getLevel();
			if(level < 3)
			{
				effect.get(0).exit();
				L2Skill skill = SkillTable.getInstance().getInfo(6142, level + 1);
				skill.getEffects(actor, player, false, false);
				actor.broadcastPacket(new MagicSkillUse(actor, player, skill.getId(), level, skill.getHitTime(), 0));
			}
		}
		else
		{
			L2Skill skill = SkillTable.getInstance().getInfo(6142, 1);
			if(skill != null)
			{
				skill.getEffects(actor, player, false, false);
				actor.broadcastPacket(new MagicSkillUse(actor, player, skill.getId(), 1, skill.getHitTime(), 0));
			}
		}
	}

	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}