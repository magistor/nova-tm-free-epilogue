package ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;

public class PolimorphingShaman extends Fighter
{
	private static final int TIGER_ID = 21259;

	public PolimorphingShaman(L2Character actor)
	{
		super(actor);
	}

	public void onEvtAttacked(L2Character attacker, long damage)
	{
		L2NpcInstance actor = getActor();
		if(actor == null)
			return;
		try
		{
			L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(TIGER_ID));
			spawn.setLoc(actor.getLoc());
			L2NpcInstance npc = spawn.doSpawn(true);
			npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		actor.decayMe();
		actor.doDie(actor);
	}
}