package ai;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 9:28
 * http://nova-tm.ru/
 *
 * AI fo Gremory
 * Квестовый NPC, стоит в МОСе.
 */
public class Gremory extends DefaultAI
{
  static final Location[] points = { new Location(114629, -70818, -544), new Location(110456, -82232, -1615) };
  private static final long TELEPORT_PERIOD = 1800000;
  private long _lastTeleport = System.currentTimeMillis();

  public Gremory(L2Character actor)
  {
    super(actor);
  }

  protected boolean thinkActive()
  {
    L2NpcInstance actor = getActor();
    if ((actor == null) || (System.currentTimeMillis() - _lastTeleport < 1800000)) {
      return false;
    }
    for (int i = 0; i < points.length; ++i)
    {
      Location loc = points[Rnd.get(points.length)];
      if (actor.getLoc().equals(loc)) {
        continue;
      }
      actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 4671, 1, 500, 0));
      ThreadPoolManager.getInstance().scheduleAi(new DefaultAI.Teleport(loc), 500, false);
      _lastTeleport = System.currentTimeMillis();
      break;
    }
    return true;
  }

  public boolean isGlobalAI()
  {
    return true;
  }
}