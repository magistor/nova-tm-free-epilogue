package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 05.02.11
 * Time: 4:35
 * http://nova-tm.ru/
 */
public class Barakiel extends Fighter
{
    private static final int x1 = 89800;
    private static final int x2 = 93200;
    private static final int y1 = -87038;

    public Barakiel(L2Character actor)
    {
        super(actor);
        AI_TASK_DELAY = 1000;
        AI_TASK_ACTIVE_DELAY = 1000;
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage)
    {
        L2NpcInstance actor = getActor();
        int x = actor.getX();
        int y = actor.getY();
        if(x < x1 || x > x2 || y < y1)
        {
            actor.teleToLocation(91008, -85904, -2736);
            actor.setCurrentHp(actor.getMaxHp(), false);
        }
        super.onEvtAttacked(attacker, damage);
    }
}