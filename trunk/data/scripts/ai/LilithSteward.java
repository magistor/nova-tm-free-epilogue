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
 * AI для LilithSteward в 5эпик квесте
 * ID = 32716
 */
public class LilithSteward extends Mystic
{
	private L2NpcInstance anakimguardian;
			
	public LilithSteward(L2Character actor)
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
			if(anakimguardian == null)
				for(L2NpcInstance npc : L2World.getAroundNpc(actor, 1000, 200))
					if(npc.getNpcId() == 32719)
					{
						npc.addDamageHate(actor, 0, 100);
						anakimguardian = npc;
					}						
			if(anakimguardian != null)
				setIntention(CtrlIntention.AI_INTENTION_ATTACK, anakimguardian);
		}
						
		return true;
	}
}