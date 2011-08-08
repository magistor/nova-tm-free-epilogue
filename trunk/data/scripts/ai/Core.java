package ai;

import l2p.common.ThreadPoolManager;
import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.PlaySound;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * AI боса Core:
 * <br> - Бубнит при атаке и смерти.
 * <br> - При смерти играет музыку и спаунит обратные порталы, которые удаляются через 15 минут
 *
 * @author SYS
 */
public class Core extends Fighter {
    private boolean _firstTimeAttacked = true;
    private static final int TELEPORTATION_CUBIC_ID = 31842;
    private static final Location CUBIC_1_POSITION = new Location(16502, 110165, -6394, 0);
    private static final Location CUBIC_2_POSITION = new Location(18948, 110165, -6394, 0);
    private static final int CUBIC_DESPAWN_TIME = 15 * 60 * 1000; // 15 min

    public Core(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (_firstTimeAttacked) {
            Functions.npcSay(actor, "Внимание! Несанкционированный доступ!");
            Functions.npcSay(actor, "Активирована охранная система.");
            _firstTimeAttacked = false;
        } else if (Rnd.chance(1)) {
            Functions.npcSay(actor, "Уничтожение нарушителей.");
        }
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor != null) {
            actor.broadcastPacket(new PlaySound(1, "BS02_D", 1, 0, actor.getLoc()));
            Functions.npcSay(actor, "Произошла фатальная ошибка");
            Functions.npcSay(actor, "Система выключается...");
            Functions.npcSay(actor, "......");
        }
        try {
            L2Spawn spawn1 = new L2Spawn(NpcTable.getTemplate(TELEPORTATION_CUBIC_ID));
            spawn1.setLoc(CUBIC_1_POSITION);
            spawn1.doSpawn(true);
            spawn1.stopRespawn();
            L2Spawn spawn2 = new L2Spawn(NpcTable.getTemplate(TELEPORTATION_CUBIC_ID));
            spawn2.setLoc(CUBIC_2_POSITION);
            spawn2.doSpawn(true);
            spawn2.stopRespawn();
            ThreadPoolManager.getInstance().scheduleAi(new DeSpawnScheduleTimerTask(spawn1, spawn2), CUBIC_DESPAWN_TIME, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }

    public class DeSpawnScheduleTimerTask implements Runnable {
        L2Spawn _spawn1 = null;
        L2Spawn _spawn2 = null;

        public DeSpawnScheduleTimerTask(L2Spawn spawn1, L2Spawn spawn2) {
            _spawn1 = spawn1;
            _spawn2 = spawn2;
        }

        public void run() {
            try {
                _spawn1.getLastSpawn().decayMe();
                _spawn2.getLastSpawn().decayMe();
                _spawn1.getLastSpawn().deleteMe();
                _spawn2.getLastSpawn().deleteMe();
            } catch (Throwable t) {
            }
        }
    }
}