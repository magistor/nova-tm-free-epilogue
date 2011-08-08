package ai.ImperialTomb;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;
import l2p.util.Rnd;

import java.util.concurrent.ScheduledFuture;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 13:05
 * http://nova-tm.ru/
 */
public class Mobs extends Fighter
{
    private ScheduledFuture<?> _SpawnTask;
    private static final int[] MOBS = {21427, 21392, 21431, 21425, 21420, 21656, 21653, 21415, 21397, 21423, 21411, 21429, 21401, 21417, 21419, 21403, 21407, 21657, 21654, 21409, 21399, 21397, 21413};
    private static final int MOBS_COUNT = 6;

    public Mobs(L2Character actor)
    {
        super(actor);
    }

    protected void onEvtAttacked(L2Character attacker, int damage)
    {
        L2NpcInstance actor = getActor();
        if (actor == null)
        {
            return;
        }
        if ((actor.isDead()) && (Rnd.chance(40)))
        {
            _SpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(attacker), 3000);
        }

        super.onEvtAttacked(attacker, damage);
    }

    private void Spawn(L2Character attacker) {
        L2NpcInstance actor = getActor();
        for (int i = 0; i < 6; ++i) {
            try {
                Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 120, actor.getReflection().getGeoIndex());
                L2Spawn sp = new L2Spawn(NpcTable.getTemplate(MOBS[Rnd.get(MOBS.length)]));
                sp.setLoc(pos);
                L2NpcInstance npc = sp.doSpawn(true);
                npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 100));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (_SpawnTask != null)
            _SpawnTask.cancel(false);
        _SpawnTask = null;
    }

    private class SpawnTask implements Runnable
    {
        private final L2Character _attacker;

        private SpawnTask(L2Character atacker)
        {
            _attacker = atacker;
        }

        public void run()
        {
            Spawn(_attacker);
        }
    }
}
