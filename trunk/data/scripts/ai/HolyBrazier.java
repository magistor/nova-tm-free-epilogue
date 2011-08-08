package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SpawnTable;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 9:42
 * http://nova-tm.ru/
 *
 * AI for Holy Brazier (Священная Жаровня )
 * неагрессивный монстр 78 уровня Others расы. Обитает в локациях Monastery of Silence.
 */
public class HolyBrazier extends DefaultAI
{
  public HolyBrazier(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtDead()
  {
    L2NpcInstance actor = getActor();
    L2Spawn spawn = actor.getSpawn();
    if (spawn.getLocation() == 9142)
      return;
    SpawnTable.getInstance().deleteSpawn(spawn);
    spawn.setRespawnDelay(60);
  }

  protected boolean randomWalk()
  {
    return false;
  }

  protected boolean randomAnimation()
  {
    return false;
  }
}