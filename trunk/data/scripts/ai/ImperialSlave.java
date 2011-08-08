package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 11:28
 * http://nova-tm.ru/
 *
 * AI for Imperial Slave (Имперский Раб )
 * агрессивный монстр 56 уровня Undead расы.
 */
public class ImperialSlave extends Fighter
{
  private long _wait_timeout = 0;
  private boolean _wait = false;
  private static final int DESPAWN_TIME = 600000;

  public ImperialSlave(L2Character actor)
  {
    super(actor);
  }

  protected boolean thinkActive()
  {
    L2NpcInstance actor = getActor();
    if ((actor == null) || (actor.isDead())) {
      return true;
    }
    if (this._def_think)
    {
      doTask();
      this._wait = false;
      return true;
    }
    if (!_wait)
    {
      _wait = true;
      _wait_timeout = (System.currentTimeMillis() + DESPAWN_TIME);
    }
    if ((_wait_timeout != 0) && (_wait) && (_wait_timeout < System.currentTimeMillis()))
    {
      actor.deleteMe();
      return true;
    }
    return super.thinkActive();
  }

  protected boolean randomWalk()
  {
    return false;
  }
}