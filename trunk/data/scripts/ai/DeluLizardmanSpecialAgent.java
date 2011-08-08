package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Ranger;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * AI для Delu Lizardman Special Agent ID: 21105
 *
 * @author Diamond
 */
public class DeluLizardmanSpecialAgent extends Ranger {
    private boolean _firstTimeAttacked = true;

    public DeluLizardmanSpecialAgent(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (_firstTimeAttacked) {
            _firstTimeAttacked = false;
            if (Rnd.chance(25)) {
                Functions.npcSay(actor, "Как вы смеете прерывать нашу дуэль?! Эй парни, напомощь!");
            }
        } else if (Rnd.chance(10)) {
            Functions.npcSay(actor, "Эй! У нас тут поединок!");
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}