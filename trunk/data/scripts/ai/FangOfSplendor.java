package ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2MonsterInstance;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 9:11
 * http://nova-tm.ru/
 *
 * AI for Fang of Splendor (Клыкастый Флинд ) — неагрессивный монстр 67 уровня Angels расы.
 */
public class FangOfSplendor extends Fighter
{
  public FangOfSplendor(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    if (actor == null)
      return;
    try
    {
      if ((((L2MonsterInstance)actor).getChampion() <= 0) && (actor.getCurrentHpPercents() > 50.0) && (Rnd.chance(5)))
      {
        L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(21538));
        spawn.setLoc(actor.getLoc());
        L2NpcInstance npc = spawn.doSpawn(true);
        npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(100));
        actor.decayMe();
        actor.doDie(actor);
        return;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onEvtAttacked(attacker, damage);
  }
}