package ai.Pailaka;

import l2p.common.ThreadPoolManager;
import l2p.extensions.listeners.L2ZoneEnterLeaveListener;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.ZoneManager;
import l2p.gameserver.model.*;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.serverpackets.SocialAction;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 12:28
 * http://nova-tm.ru/
 *
 * AI for Latana (Латана )
 * неагрессивный монстр 75 уровня Dragons расы.
 */
public class Latana extends Fighter
{
  private L2NpcInstance actor = getActor();
  private static boolean _pokazrolika = false;
  private static L2Zone _zone;
  private ZoneListener _zoneListener = new ZoneListener();

  public Latana(L2Character actor)
  {
    super(actor);
    _zone = ZoneManager.getInstance().getZoneById(L2Zone.ZoneType.dummy, 702118, false);
    _zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
    actor.setImobilised(true);
  }

  public void rolik(L2Player pc)
  {
    if (pc == null)
      return;
    _pokazrolika = true;
    pc.specialCamera(actor, 400, 38, 0, 6000, 4000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask1(pc), 3000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask2(pc), 8000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask3(pc), 15000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask4(pc), 19000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask5(pc), 23000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask6(pc), 25000);
    ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask7(pc), 33000);
  }

  public static L2Zone getZone()
  {
    return _zone;
  }

  protected boolean randomAnimation()
  {
    return false;
  }

  protected void onEvtDead()
  {
    L2Player killer = null;
    L2Character MostHated = actor.getMostHated();
    if ((MostHated == null) || (!MostHated.isPlayable()))
      return;
    killer = MostHated.getPlayer();
    killer.specialCamera(getActor(), 400, 38, 0, 6000, 5000);
  }

  public class ZoneListener extends L2ZoneEnterLeaveListener
  {
    public ZoneListener()
    {
    }

    public void objectEntered(L2Zone zone, L2Object object)
    {
      if ((getActor() != null) && (!getActor().isDead()) && (!Latana._pokazrolika))
        rolik((L2Player)object);
    }

    public void objectLeaved(L2Zone zone, L2Object object)
    {
    }
  }

  private class ScheduleTimerTask7 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask7(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 5716, 1, 8000, 0));
      _pc.specialCamera(Latana.this.actor, 100, 0, -10, 6000, 14000);
    }
  }

  private class ScheduleTimerTask6 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask6(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      _pc.specialCamera(actor, 50, 1, 0, 6000, 9000);
    }
  }

  private class ScheduleTimerTask5 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask5(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      SocialAction sa = null;
      sa = new SocialAction(actor.getObjectId(), 2);
      Latana.this.actor.broadcastPacket(sa);
      _pc.specialCamera(actor, 50, 10, -10, 6000, 3000);
    }
  }

  private class ScheduleTimerTask4 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask4(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      _pc.specialCamera(actor, 50, 0, -10, 6000, 5000);
    }
  }

  private class ScheduleTimerTask3 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask3(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      _pc.specialCamera(actor, 1, 0, -10, 0, 5000);
    }
  }

  private class ScheduleTimerTask2 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask2(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      _pc.specialCamera(actor, 100, 25, -7, 6000, 8000);
    }
  }

  private class ScheduleTimerTask1 implements Runnable
  {
    private L2Player _pc;

    public ScheduleTimerTask1(L2Player pc)
    {
      _pc = pc;
    }

    public void run()
    {
      _pc.specialCamera(actor, 150, 38, 0, 6000, 6000);
    }
  }
}