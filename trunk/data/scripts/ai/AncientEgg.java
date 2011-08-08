package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.gameserver.tables.SkillTable;
import l2p.util.Location;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 7:26
 * http://nova-tm.ru/
 */
public class AncientEgg extends DefaultAI
{
  private boolean _firstTimeAttacked = true;
  private static final int[] BROTHERS = { 22196, 22199, 22200, 22203 };

  public AncientEgg(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    if (_firstTimeAttacked)
    {
      _firstTimeAttacked = false;
      Functions.npcShout(actor, ":(");
      for (int bro : BROTHERS)
        try
        {
          Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 100, 120, actor.getReflection().getGeoIndex());
          L2Spawn spawn = new L2Spawn(NpcTable.getTemplate(bro));
          spawn.setLoc(pos);
          L2NpcInstance npc = spawn.doSpawn(true);
          npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Integer.valueOf(Rnd.get(1, 100)));
          actor.doCast(SkillTable.getInstance().getInfo(5088, 1), attacker, true);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
    }
    super.onEvtAttacked(attacker, damage);
  }

  protected boolean randomWalk()
  {
    return false;
  }

  protected boolean randomAnimation()
  {
    return false;
  }

  @Override
    protected void onEvtDead(L2Character killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}