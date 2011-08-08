package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.modules.data.DoorTable;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 4:20
 * http://nova-tm.ru/
 */
public class Leodas extends Fighter {
    private static final int[] doors = {19250003, 19250004};

    public Leodas(L2Character actor) {
        super(actor);
    }

    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null)
            return;
        for (int i : doors)
            DoorTable.getInstance().getDoor(i).closeMe();
        super.onEvtAttacked(attacker, damage);
    }

    protected void onEvtDead(L2Character killer) {
        for (int i : doors) {
            DoorTable.getInstance().getDoor(i).openMe();
            DoorTable.getInstance().getDoor(i).onOpen();
        }

        HellboundManager.getInstance().updatePoints(-1000);
        super.onEvtDead(killer);
    }
}
