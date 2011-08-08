package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 11:57
 * http://nova-tm.ru/
 */
public class Santa extends DefaultAI
{
  static final Location[] points =
          { new Location(109208, -152968, -1768),
                  new Location(107464, -153016, -2190),
                  new Location(105624, -151656, -2310),
                  new Location(104632, -146312, -2803),
                  new Location(96392, -139096, -2918),
                  new Location(94712, -138376, -2924),
                  new Location(91352, -136120, -2717),
                  new Location(80024, -139192, -2434),
                  new Location(75928, -139016, -2869),
                  new Location(76328, -145192, -1283),
                  new Location(75112, -148248, -811),
                  new Location(76328, -145192, -1283),
                  new Location(75928, -139016, -2869),
                  new Location(80024, -139192, -2434),
                  new Location(91352, -136120, -2717),
                  new Location(94712, -138376, -2924),
                  new Location(96392, -139096, -2918),
                  new Location(104632, -146312, -2803),
                  new Location(105624, -151656, -2310),
                  new Location(107464, -153016, -2190) };

  private int current_point = -1;
  private long wait_timeout = 0;
  private boolean wait = false;

  public Santa(L2Character actor)
  {
    super(actor);
  }

  public boolean isGlobalAI()
  {
    return true;
  }

  protected boolean thinkActive()
  {
    L2NpcInstance actor = getActor();
    if ((actor == null) || (actor.isDead())) {
      return true;
    }
    if (_def_think)
    {
      doTask();
      return true;
    }

    if ((System.currentTimeMillis() > wait_timeout) && current_point > -1 || (Rnd.chance(5)))
    {
      if (!wait) {
        switch (current_point)
        {
        case 10:
          wait_timeout = (System.currentTimeMillis() + 60000);
          wait = true;
          return true;
        }
      }
      wait_timeout = 0;
      wait = false;
      current_point += 1;

      if (current_point >= points.length) {
        current_point = 0;
      }
      addTaskMove(points[current_point], true);
      return true;
    }

    return randomAnimation();
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
  }

  protected void onEvtAggression(L2Character target, int aggro)
  {
  }
}