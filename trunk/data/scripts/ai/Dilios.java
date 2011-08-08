package ai;

import l2p.common.ThreadPoolManager;
import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.SocialAction;
import l2p.util.GArray;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 8:46
 * http://nova-tm.ru/
 *
 *  AI fo Dilios
 *  General Dilios (Генерал  Дилиос ) — NPC 70 уровня расы Humans и обитает в локации Keucereus Alliance Base.
 */
public class Dilios extends DefaultAI
{
  private long _wait_timeout = System.currentTimeMillis() + 60000L;
  private static GArray<L2NpcInstance> _arm;

  public Dilios(L2Character actor)
  {
    super(actor);
  }

  protected boolean thinkActive()
  {
    L2NpcInstance actor = getActor();
    if (actor == null) {
      return true;
    }
    if (this._wait_timeout < System.currentTimeMillis())
    {
      if (_arm == null) {
        _arm = L2ObjectsStorage.getAllByNpcId(32619, false);
      }
      this._wait_timeout = (System.currentTimeMillis() + Rnd.get(45, 90) * 1000);

      Functions.npcSay(actor, "Бейте трижды!");

      int time = 3000;
      for (int i = 0; i <= 2; ++i)
      {
        ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
        {
          public void run()
          {
            for (L2NpcInstance voin : Dilios._arm)
              voin.broadcastPacket(new SocialAction(voin.getObjectId(), 4));
          }
        }
        , time);

        time += 3000;
      }
    }
    return true;
  }

  public boolean isGlobalAI()
  {
    return true;
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
  }

  protected void onEvtAggression(L2Character target, int aggro)
  {
  }
}