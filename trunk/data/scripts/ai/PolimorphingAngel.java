package ai;

import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.*;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;

public class PolimorphingAngel extends Fighter
{
    private class SpawnTask
        implements Runnable
    {

        public void run()
        {
            Spawn(_attacker);
        }

        private final L2Character _attacker;
        private SpawnTask(L2Character atacker)
        {
            _attacker = atacker;
        }

    }


    public PolimorphingAngel(L2Character actor)
    {
        super(actor);
    }

    protected void onEvtAttacked(L2Character attacker, int damage)
    {
        L2NpcInstance actor = getActor();
        if(actor == null)
            return;
        if(actor.isDead())
            _SpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(attacker), 3000L);
        super.onEvtAttacked(attacker, damage);
    }

    private void Spawn(L2Character attacker)
    {
        L2NpcInstance actor = getActor();
        try
        {
            L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(((Integer)polymorphing.get(Integer.valueOf(actor.getNpcId()))).intValue()));
            spawn.setLoc(actor.getLoc());
            L2NpcInstance npc = spawn.doSpawn(true);
            npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(100));
            actor.doDie(actor);
            return;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(_SpawnTask != null)
            _SpawnTask.cancel(false);
        _SpawnTask = null;
    }

    private ScheduledFuture<?> _SpawnTask;
    private static final HashMap<Integer, Integer> polymorphing;

    static 
    {
        polymorphing = new HashMap<Integer, Integer>();
        polymorphing.put(Integer.valueOf(20830), Integer.valueOf(20859));
        polymorphing.put(Integer.valueOf(21067), Integer.valueOf(21068));
        polymorphing.put(Integer.valueOf(21062), Integer.valueOf(21063));
        polymorphing.put(Integer.valueOf(20831), Integer.valueOf(20860));
        polymorphing.put(Integer.valueOf(21070), Integer.valueOf(21071));
    }

}
