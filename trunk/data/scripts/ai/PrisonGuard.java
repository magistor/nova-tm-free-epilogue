package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SkillTable;

/**
 * AI мобов Prison Guard на Isle of Prayer.<br>
 * - Не используют функцию Random Walk<br>
 * - Ругаются на атаковавших чаров без эффекта Event Timer<br>
 * - Ставят в петрификацию атаковавших чаров без эффекта Event Timer<br>
 * - Не могут быть убиты чарами без эффекта Event Timer<br>
 * - Не проявляют агресии к чарам без эффекта Event Timer<br>
 * ID: 18367, 18368
 *
 * @author SYS
 */
public class PrisonGuard extends Fighter {
    private static final int RACE_STAMP = 10013;

    public PrisonGuard(L2Character actor) {
        super(actor);
    }

    @Override
    public void checkAggression(L2Character target) {
        if (target.getEffectList().getEffectsBySkillId(L2Skill.SKILL_EVENT_TIMER) == null) {
            return;
        }
        // 18367 не агрятся
        L2NpcInstance actor = getActor();
        if (actor == null || actor.getNpcId() == 18367) {
            return;
        }
        super.checkAggression(target);
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (attacker.isSummon() || attacker.isPet()) {
            attacker = attacker.getPlayer();
        }
        if (attacker.getEffectList().getEffectsBySkillId(L2Skill.SKILL_EVENT_TIMER) == null) {
            if (actor.getNpcId() == 18367) {
                Functions.npcSay(actor, "Это не так просто заполучить.");
            } else if (actor.getNpcId() == 18368) {
                Functions.npcSay(actor, "Убирайтесь прочь...");
            }
            L2Skill petrification = SkillTable.getInstance().getInfo(4578, 1); // Petrification
            actor.doCast(petrification, attacker, true);
            if (attacker.getPet() != null) {
                actor.doCast(petrification, attacker.getPet(), true);
            }
            return;
        }
        // 18367 не отвечают на атаку, но зовут друзей
        if (actor.getNpcId() == 18367) {
            actor.callFriends(attacker, damage);
            return;
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (actor.getNpcId() == 18367 && killer.getPlayer().getEffectList().getEffectsBySkillId(L2Skill.SKILL_EVENT_TIMER) != null) {
            Functions.addItem(killer.getPlayer(), RACE_STAMP, 1);
        }
        super.onEvtDead(killer);
    }
}