package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2RemnantInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 11:53
 * http://nova-tm.ru/
 */
public class Remnant extends Fighter
{
  public Remnant(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtSeeSpell(L2Skill skill, L2Character caster)
  {
    if (skill.getId() != 2358) {
      return;
    }
    L2RemnantInstance actor = (L2RemnantInstance)getActor();
    if ((actor == null) || (actor.getCurrentHp() == 0.5)) {
      return;
    }
    actor.setBlessed(true);
    actor.doDie(actor);
  }
}