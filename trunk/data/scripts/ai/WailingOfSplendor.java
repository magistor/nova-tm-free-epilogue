package ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 12:20
 * http://nova-tm.ru/
 *
 * AI for Wailing of Splendor (Ревущий Флинд )
 * неагрессивный монстр 67 уровня Angels расы. Обитает в локациях Valley of Saints.
 */
public class WailingOfSplendor extends RndTeleportFighter
{
  private static final int WailingofSplendor = 21540;
  private boolean _spawned;

  public WailingOfSplendor(L2Character actor)
  {
    super(actor);
  }

  @Override
  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    if (actor == null)
      return;
    try
    {
      if ((!_spawned) && (Rnd.chance(25)))
      {
        _spawned = true;
        L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(WailingofSplendor));
        spawn.setLoc(GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 150, actor.getReflection().getGeoIndex()));
        L2NpcInstance npc = spawn.doSpawn(true);
        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 100);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onEvtAttacked(attacker, damage);
  }

  protected void onEvtDead(L2Character killer)
  {
    _spawned = false;
    super.onEvtDead(killer);
  }
}