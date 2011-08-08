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
 * AI для LilithElite в 5эпик квесте
 * ID = 32717
 */
public class LilithElite extends Mystic
{
	private L2NpcInstance anakimguard;
			
	public LilithElite(L2Character actor)
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
			if(anakimguard == null)
				for(L2NpcInstance npc : L2World.getAroundNpc(actor, 1000, 200))
					if(npc.getNpcId() == 32720)
					{
						npc.addDamageHate(actor, 0, 100);
						anakimguard = npc;
					}						
			if(anakimguard != null)
				setIntention(CtrlIntention.AI_INTENTION_ATTACK, anakimguard);
		}
						
		return true;
	}
}