package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 11:47
 * http://nova-tm.ru/
 */
public class RabbitsToRichesTreasureAI extends Fighter
{
  private static final int TREASURE = 13098;
  private static final int TREASURE_COUNT = 3;

  public RabbitsToRichesTreasureAI(L2Character actor)
  {
    super(actor);
  }

  public void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    if (actor == null)
      return;
    for (int i = 0; i < TREASURE_COUNT; ++i)
      try
      {
        Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 120, actor.getReflection().getGeoIndex());
        L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(TREASURE));
        spawn.setLoc(pos);
        spawn.doSpawn(true);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    actor.doDie(actor);
    actor.deleteMe();
  }
}