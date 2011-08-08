package ai;

import java.util.concurrent.ScheduledFuture;
import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.*;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.*;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;
import l2p.util.Rnd;

public class CarrionScarab extends Fighter
{
  private ScheduledFuture<?> _SpawnTask;
  private static final int[] MOBS = { 21397 };
  public CarrionScarab(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    if (actor == null) {
      return;
    }
    if (actor.isDead())
    {
      if (Rnd.chance(40)) {
        this._SpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(attacker), 3000L);
      }
    }
    super.onEvtAttacked(attacker, damage);
  }

  private void Spawn(L2Character attacker)
  {
    L2NpcInstance actor = getActor();
    for (int i = 0; i < 6; i++) {
      try
      {
        Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 120, actor.getReflection().getGeoIndex());
        L2Spawn sp = new L2Spawn(NpcTable.getTemplate(MOBS[Rnd.get(MOBS.length)]));
        sp.setLoc(pos);
        L2NpcInstance npc = sp.doSpawn(true);
        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(Rnd.get(1, 100)));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    if (this._SpawnTask != null)
      this._SpawnTask.cancel(false);
    this._SpawnTask = null;
  }

  private class SpawnTask
    implements Runnable
  {
    private final L2Character _attacker;

    private SpawnTask(L2Character atacker)
    {
      this._attacker = atacker;
    }

    public void run()
    {
      CarrionScarab.this.Spawn(this._attacker);
    }
  }
}