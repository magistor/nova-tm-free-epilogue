package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Ranger;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * AI для Delu Lizardman Commander Agent ID: 21107
 *
 * @author Magister
 */
public class DeluLizardmanSpecialCommander extends Ranger {
    private boolean _firstTimeAttacked = true;

    public DeluLizardmanSpecialCommander(L2Character actor) {
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
            if (Rnd.chance(40)) {
                Functions.npcSay(actor, "Давайте, я найму Вас!!");
            }
        } else if (Rnd.chance(15)) {
            Functions.npcSay(actor, "Как вы посмели прервать священный поединок? Придеться преподать вам урок!");
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}