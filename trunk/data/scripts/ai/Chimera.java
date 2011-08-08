package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

public class Chimera extends Fighter {
    public Chimera(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtSeeSpell(L2Skill skill, L2Character caster) {
        if (skill.getId() != 2359) {
            return;
        }
        L2NpcInstance actor = getActor();
        if (actor == null || actor.getCurrentHpPercents() > 10) // 10% ХП для использования бутылки
        {
            return;
        }
        // 10% шанс получения Life Force
        L2Player killer = null;
        L2Character mostHated = actor.getMostHated();
        if (mostHated != null && mostHated.isPlayable()) {
            killer = mostHated.getPlayer();
        }
        actor.dropItem(killer, actor.getNpcId() == 22353 ? 9682 : Rnd.chance(10) ? 9681 : 9680, 1);
        actor.decayMe();
        actor.doDie(actor);
    }
}