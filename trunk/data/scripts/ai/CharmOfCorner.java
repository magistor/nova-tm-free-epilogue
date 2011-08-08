package ai;

import l2p.gameserver.ai.Mystic;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 8:18
 * http://nova-tm.ru/
 */
public class CharmOfCorner extends Mystic
{
  public CharmOfCorner(L2Character actor)
  {
    super(actor);
    actor.setImobilised(true);
  }

  protected boolean checkTarget(L2Character target, boolean canSelf, int range)
  {
    L2NpcInstance actor = getActor();
    if ((actor != null) && (target != null) && (!actor.isInRange(target, actor.getAggroRange())))
    {
      target.removeFromHatelist(actor, true);
      return false;
    }
    return super.checkTarget(target, canSelf, range);
  }

  protected boolean randomWalk()
  {
    return false;
  }
}