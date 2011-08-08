package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 12:06
 * http://nova-tm.ru/
 */
public class Tiberias extends Fighter
{
  public Tiberias(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtDead(L2Character killer)
  {
    L2NpcInstance actor = getActor();
    Functions.npcShoutCustomMessage(actor, "scripts.ai.Tiberias.kill", null);
    super.onEvtDead(killer);
  }
}