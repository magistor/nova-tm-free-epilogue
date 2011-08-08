package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.*;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.instances.L2ReflectionBossInstance;
import l2p.gameserver.serverpackets.ExStartScenePlayer;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;

/**
 * AI Throne of Destruction Powerful Device (ID: 18778) в Seed of Destruction:
 * - при смерти всех в этом измерении открывает дверь
 * - спаунит боса Tiat (ID: 29163)
 * - показывает заставку
 *
 * @author SYS
 */
public class ThroneofDestruction extends DefaultAI {
    private static final int DOOR = 12240031;
    private static final int TIAT_NPC_ID = 29163;
    private static final Location TIAT_LOC = new Location(-250408, 207416, -11968, 16384);

    public ThroneofDestruction(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        Reflection ref = actor.getReflection();
        // Открываем дверь, спауним тиата
        if (checkAllDestroyed(actor.getNpcId(), ref.getId())) {
            ref.openDoor(DOOR);
            spawnTiat();
            // Показываем заставку спауна Тиата
            for (L2Player pl : L2World.getAroundPlayers(actor)) {
                if (pl != null) {
                    pl.showQuestMovie(ExStartScenePlayer.SCENE_TIAT_OPENING);
                }
            }
        }
        super.onEvtDead(killer);
    }

    /**
     * Проверяет, уничтожены ли все Throne of Destruction Powerful Device в текущем измерении
     *
     * @return true если все уничтожены
     */
    private static boolean checkAllDestroyed(int mobId, long refId) {
        for (L2NpcInstance npc : L2ObjectsStorage.getAllByNpcId(mobId, true)) {
            if (npc.getReflection().getId() == refId) {
                return false;
            }
        }
        return true;
    }

    /**
     * Спаунит боса
     */
    private void spawnTiat() {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        L2ReflectionBossInstance tiat = new L2ReflectionBossInstance(IdFactory.getInstance().getNextId(), NpcTable.getTemplate(TIAT_NPC_ID));
        tiat.setSpawnedLoc(TIAT_LOC);
        tiat.setReflection(actor.getReflection());
        tiat.onSpawn();
        tiat.spawnMe(tiat.getSpawnedLoc());
        actor.getReflection().addSpawn(tiat.getSpawn());
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}