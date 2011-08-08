package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2MinionInstance;
import l2p.gameserver.model.instances.L2ReflectionBossInstance;
import l2p.util.GArray;
import l2p.util.Location;
import l2p.util.MinionList;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 9:48
 * http://nova-tm.ru/
 */
public class HorsesAI extends DefaultAI
{
  private Location[] points = new Location[4];
  private int current_point = -1;
  private long wait_timeout = 0;
  private boolean wait = false;
  private long _lastOrder = 0;

  public HorsesAI(L2Character actor)
  {
    super(actor);
    points[0] = new Location(82712, 148809, -3495);
    points[1] = new Location(82392, 148798, -3493);
    points[2] = new Location(82381, 148435, -3493);
    points[3] = new Location(82708, 148434, -3495);
  }

  public boolean isGlobalAI()
  {
    return true;
  }

  protected boolean thinkActive()
  {
    if (getActor().isDead()) {
      return true;
    }
    if (_def_think)
    {
      doTask();
      return true;
    }

    if ((System.currentTimeMillis() > wait_timeout) && (((current_point > -1) || (Rnd.chance(5)))))
    {
      if (!wait)
      {
        switch (current_point)
        {
        case 0:
          wait_timeout = (System.currentTimeMillis() + 1000);
          wait = true;
          return true;
        case 1:
          wait_timeout = (System.currentTimeMillis() + 1000);
          wait = true;
          return true;
        case 2:
          wait_timeout = (System.currentTimeMillis() + 1000);
          wait = true;
          return true;
        case 3:
          wait_timeout = (System.currentTimeMillis() + 1000);
          wait = true;
          return true;
        }
      }

      wait_timeout = 0;
      wait = false;

      if (current_point >= points.length - 1) {
        current_point = -1;
      }
      current_point += 1;

      DefaultAI.Task task = new DefaultAI.Task();
      task.type = DefaultAI.TaskType.MOVE;
      task.loc = points[current_point];
      _task_list.add(task);
      _def_think = true;
      return true;
    }

    return randomAnimation();
  }

  protected void thinkAttack()
  {
    L2Player target;
    if ((_lastOrder < System.currentTimeMillis()) && (getActor().isInCombat()))
    {
      _lastOrder = (System.currentTimeMillis() + 30000);
      MinionList ml = ((L2ReflectionBossInstance)getActor()).getMinionList();
      if ((ml == null) || (!ml.hasMinions()))
      {
        super.thinkAttack();
        return;
      }
      GArray pl = L2World.getAroundPlayers(getActor());
      if (pl.isEmpty())
      {
        super.thinkAttack();
        return;
      }
      target = (L2Player)pl.get(Rnd.get(pl.size()));
      Functions.npcShoutCustomMessage(getActor(), "Я убью тебя!", new String[]{target.getName()});
      for (L2MinionInstance m : ml.getSpawnedMinions())
      {
        m.clearAggroList(true);
        m.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, target, 10000000);
      }
    }
    super.thinkAttack();
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
  }

  protected void onEvtAggression(L2Character target, int aggro)
  {
  }
}