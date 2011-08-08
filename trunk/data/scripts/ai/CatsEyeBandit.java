package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * mobId 27038
 *
 * @author Angy
 */
public class CatsEyeBandit extends DefaultAI {
    private static boolean _firstTimeAttacked = false;

    public CatsEyeBandit(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null || actor.isDead()) {
            return;
        }
        if (_firstTimeAttacked) {
            if (Rnd.get(100) < 40) {
                Functions.npcSay(actor, "You're fool, you think you can catch me? ");
            }
        } else {
            _firstTimeAttacked = true;
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (Rnd.get(100) < 80) {
            Functions.npcSay(actor, "I have to do something after this shameful incident ...");
            _firstTimeAttacked = false;
        }
        super.onEvtDead(killer);
    }
}