package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 8:54
 * http://nova-tm.ru/
 */
public class EvilRabbit extends Fighter
{
  public EvilRabbit(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtDead(L2Character killer)
  {
    try
    {
      L2NpcInstance actor = getActor();
      if (actor != null)
      {
        L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(actor.getNpcId()));
        int _spawned = 0;
        while (_spawned < 10)
        {
          if (Rnd.get(1, 5) == 1)
          {
            Location pos = Rnd.coordsRandomize(actor.getLoc(), 100, 150);
            spawn.setLoc(pos);
            L2NpcInstance npc = spawn.doSpawn(true);
            Functions.npcShout(npc, "Да я вас убью!!!");
          }
          ++_spawned;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onEvtDead(killer);
  }
}