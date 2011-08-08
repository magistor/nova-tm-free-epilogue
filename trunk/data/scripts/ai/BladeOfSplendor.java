package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2MonsterInstance;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 7:40
 * http://nova-tm.ru/
 */
public class BladeOfSplendor extends RndTeleportFighter
{
    private static final int[] CLONES = { 21525 };

    private boolean _firstTimeAttacked = true;

    public BladeOfSplendor(L2Character actor)
    {
        super(actor);
        AI_TASK_DELAY = 1000;
        AI_TASK_ACTIVE_DELAY = 100000;
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage)
    {
        L2NpcInstance actor = getActor();
        if(actor == null)
            return;
        if(!actor.isDead() && _firstTimeAttacked)
        {
            _firstTimeAttacked = false;
            Functions.npcSay(actor, "Now I Know Why You Wanna Hate Me");
            for(int bro : CLONES)
                try
                {
                    L2NpcInstance npc = NpcTable.getTemplate(bro).getNewInstance();
                    npc.setSpawnedLoc(((L2MonsterInstance) actor).getMinionPosition());
                    npc.setReflection(actor.getReflection());
                    npc.onSpawn();
                    npc.spawnMe(npc.getSpawnedLoc());
                    npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 1000));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer)
    {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}