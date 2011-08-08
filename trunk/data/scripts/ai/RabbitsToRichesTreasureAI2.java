package ai;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.tables.ItemTable;
import l2p.util.Rnd;

import java.util.concurrent.ScheduledFuture;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 11:49
 * http://nova-tm.ru/
 */
public class RabbitsToRichesTreasureAI2 extends DefaultAI
{
  private L2NpcInstance actor = getActor();
  private static ScheduledFuture _unSpawnTask;
  private static int[] AMOUNTS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static int[] DROPS = { 10254, 10255, 10256, 10257, 10258, 10259, 14104, 10273, 10178, 10179, 10260, 10261, 10262, 10263, 10264, 10265, 10266, 10267, 10268, 10269, 10270, 10271 };

  public RabbitsToRichesTreasureAI2(L2Character actor)
  {
    super(actor);
    startDespawnTimer();
  }

  public void onTimer(String event)
  {
    if (!event.equals("Unspawn"))
      return;
    actor.deleteMe();
    if (_unSpawnTask == null)
      return;
    _unSpawnTask.cancel(false);
    _unSpawnTask = null;
  }

  public void startDespawnTimer()
  {
    _unSpawnTask = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask("Unspawn", this), 18000);
  }

  protected void onEvtSeeSpell(L2Skill skill, L2Character caster)
  {
    if (skill.getId() != 630)
      return;
    if ((actor == null) || (caster.getTarget() != actor))
      return;
    if (Rnd.chance(90))
    {
      if (Rnd.chance(5))
        actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 2024, 1, 1, 0));
      dropItem(actor, 10272, AMOUNTS[Rnd.get(AMOUNTS.length)]);
    }
    else
    {
      if (Rnd.chance(5))
        actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 2024, 1, 1, 0));
      dropItem(actor, DROPS[Rnd.get(DROPS.length)], 1);
    }
    actor.decayMe();
    actor.doDie(actor);
  }

  private void dropItem(L2NpcInstance npc, int itemId, int count)
  {
    L2ItemInstance item = ItemTable.getInstance().createItem(itemId);
    item.setCount(count);
    item.dropMe(npc, npc.getLoc());
  }

  protected boolean randomWalk()
  {
    return false;
  }

  private class ScheduleTimerTask implements Runnable
  {
    private String _name;
    private RabbitsToRichesTreasureAI2 _caller;

    public ScheduleTimerTask(String name, RabbitsToRichesTreasureAI2 classPtr)
    {
      _name = name;
      _caller = classPtr;
    }

    public void run()
    {
      _caller.onTimer(_name);
    }
  }
}