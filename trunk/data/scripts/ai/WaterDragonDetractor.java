package ai;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;

import java.util.concurrent.ScheduledFuture;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 12:15
 * http://nova-tm.ru/
 */
public class WaterDragonDetractor extends Fighter
{
  private long _wait_timeout = 0;
  private boolean _wait = false;
  private static int DESPAWN_TIME = 180000;
  private static final int RESPAWN_DELAY = 40000;
  public boolean _isSpawned = false;
  private ScheduledFuture _SpawnTask;

  public WaterDragonDetractor(L2Character actor)
  {
    super(actor);
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
      _wait = false;
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

  protected void onEvtDead(L2Character killer)
  {
    if (!_isSpawned)
      _SpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new SpawnTask(), RESPAWN_DELAY);
    super.onEvtDead(killer);
  }

  protected void onEvtReadyToAct()
  {
    _isSpawned = true;
    super.onEvtReadyToAct();
  }

  private void Spawn()
  {
    L2NpcInstance actor = getActor();
    try
    {
      Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 120, actor.getReflection().getGeoIndex());
      L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(getActor().getNpcId()));
      spawn.setLoc(pos);
      L2NpcInstance npc = spawn.doSpawn(true);
      npc.getAI().notifyEvent(CtrlEvent.EVT_READY_TO_ACT);
      spawn.stopRespawn();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    if (this._SpawnTask != null)
      this._SpawnTask.cancel(false);
    this._SpawnTask = null;
  }

  private class SpawnTask implements Runnable
  {
    private SpawnTask()
    {
    }

    public void run()
    {
      Spawn();
    }
  }
}