package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Mystic;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.Reflection;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * AI для Anakim Executor в 5 эпик квесте
 * ID = 32721
 */
public class AnakimExecutor extends Mystic
{
	private L2NpcInstance lilith;
			
	public AnakimExecutor(L2Character actor)
	{
		super(actor);
		AI_TASK_DELAY = 1000;
		AI_TASK_ACTIVE_DELAY = 1000;
	}
	
	@Override
	protected boolean thinkActive()
	{
		L2NpcInstance actor = getActor();
		if(actor == null)
			return true;
			
		if(getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
		{
			if(lilith == null)
				for(L2NpcInstance npc : L2World.getAroundNpc(actor, 1000, 200))
					if(npc.getNpcId() == 32715)
					{
						npc.addDamageHate(actor, 0, 100);
						lilith = npc;
					}						
			if(lilith != null)
				setIntention(CtrlIntention.AI_INTENTION_ATTACK, lilith);
		}
	
		return true;
	}
}