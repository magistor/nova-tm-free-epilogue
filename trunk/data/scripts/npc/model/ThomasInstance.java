package npc.model;

import events.SavingSnowman.SavingSnowman;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2MonsterInstance;
import l2p.gameserver.templates.L2NpcTemplate;

/**
 * Данный инстанс используется мобом Thomas D. Turkey в эвенте Saving Snowman
 *
 * @author SYS
 */
public class ThomasInstance extends L2MonsterInstance
{
	public ThomasInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void reduceCurrentHp(double i, L2Character attacker, L2Skill skill, boolean awake, boolean standUp, boolean directHp, boolean canReflect)
	{
		i = 10;
		if(attacker.getActiveWeaponInstance() != null)
		{
			switch(attacker.getActiveWeaponInstance().getItemId())
			{
				// Хроно оружие наносит больший урон
				case 4202: // Chrono Cithara
				case 5133: // Chrono Unitus
				case 5817: // Chrono Campana
				case 7058: // Chrono Darbuka
				case 8350: // Chrono Maracas
					i = 100;
					break;
				default:
					i = 10;
			}
		}
		super.reduceCurrentHp(i, attacker, skill, awake, standUp, directHp, canReflect);
	}

	@Override
	public void doDie(L2Character killer)
	{
		L2Character topdam = getTopDamager(getAggroList());
		if(topdam == null)
		{
			topdam = killer;
		}
		SavingSnowman.freeSnowman(topdam);
		super.doDie(killer);
	}

	@Override
	public boolean canChampion()
	{
		return false;
	}
}