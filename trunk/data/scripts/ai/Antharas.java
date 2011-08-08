package ai;

import bosses.AntharasManager;
import l2p.gameserver.ai.Priest;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

public class Antharas extends Priest {
    public Antharas(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        AntharasManager.setLastAttackTime();
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected boolean maybeMoveToHome() {
        L2NpcInstance actor = getActor();
        if (actor != null && !AntharasManager.getZone().checkIfInZone(actor.getX(), actor.getY())) {
            teleportHome(true);
        }
        return false;
    }
}