package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

public class CaughtFighter extends Fighter
{
    private static final int TIME_TO_LIVE = 60000;
    private final long TIME_TO_DIE = System.currentTimeMillis() + TIME_TO_LIVE;

    public CaughtFighter(L2Character actor)
    {
        super(actor);
        AI_TASK_DELAY = 1000;
        AI_TASK_ACTIVE_DELAY = 1000;
    }

    @Override
    public boolean isGlobalAI()
    {
        return true;
    }

    @Override
    protected boolean thinkActive()
    {
        L2NpcInstance actor = getActor();
        if(actor != null && System.currentTimeMillis() >= TIME_TO_DIE)
        {
            actor.deleteMe();
            return false;
        }
        return super.thinkActive();
    }
}