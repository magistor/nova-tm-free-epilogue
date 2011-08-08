package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SkillTable;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 7:37
 * http://nova-tm.ru/
 */
public class AndreasRoyalGuard extends Fighter
{
  public AndreasRoyalGuard(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();

    if (actor.getCurrentHpPercents() <= 70.0)
    {
      actor.doCast(SkillTable.getInstance().getInfo(4612, 9), attacker, true);
      actor.doDie(actor);
    }
    super.onEvtAttacked(attacker, damage);
  }
}
