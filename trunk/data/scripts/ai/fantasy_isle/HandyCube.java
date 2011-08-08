package ai.fantasy_isle;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.instancemanager.games.GameHandyManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.NpcInfo;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 21:40
 * http://nova-tm.ru/
 */
public class HandyCube extends DefaultAI
{
  int Red = 1;
  int Blue = 0;

  public HandyCube(L2Character actor)
  {
    super(actor);
    actor.setName(" ");
    actor.updateAbnormalEffect();
  }

  protected void onEvtSeeSpell(L2Skill skill, L2Character caster)
  {
    L2NpcInstance actor = getActor();
    int arenaId = GameHandyManager.getPlayerArena(caster.getPlayer());
    if(actor.getColorHandyCubik() == Blue || skill.getId() == 5853)
    {
      caster.getPlayer()._HandyGamePoints += 1;
      actor.setColorHandyCubik(Red);
      GameHandyManager.increaseKill(2, arenaId, caster.getPlayer());
    }
    else if(actor.getColorHandyCubik() == Red || skill.getId() == 5852)
    {
      caster.getPlayer()._HandyGamePoints += 1;
      actor.setColorHandyCubik(Blue);
      GameHandyManager.increaseKill(1, arenaId, caster.getPlayer());
    }
    actor.broadcastPacket(new NpcInfo(actor, caster.getPlayer()));
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    L2NpcInstance actor = getActor();
    actor.setIsInvul(false);
  }

  public static boolean contains(L2Object[] objects, L2NpcInstance npc)
  {
    for (L2Object obj : objects)
    {
      if (obj == npc)
        return true;
    }
    return false;
  }
}