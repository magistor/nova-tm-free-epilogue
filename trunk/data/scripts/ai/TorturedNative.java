package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * AI Tortured Native в городе-инстанте на Hellbound<br>
 * - периодически кричат
 *
 * @author SYS
 */
public class TorturedNative extends Fighter {
    public TorturedNative(L2Character actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        L2NpcInstance actor = getActor();
        if (actor == null || actor.isDead()) {
            return true;
        }
        if (Rnd.chance(1)) {
            if (Rnd.chance(10)) {
                Functions.npcSay(actor, "Я так плохо себя чувствую...");
            } else {
                Functions.npcSay(actor, "Это... убьет... всех...!");
            }
        }
        return super.thinkActive();
    }
}