package ai;

import javolution.util.FastList;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 10:41
 * http://nova-tm.ru/
 *
 * AI for Imperial Gravekeeper (Имперский Хранитель Могил )
 * неагрессивный монстр 60 уровня Undead расы.
 */
public class ImperialGravekeeper extends Fighter
{
  public ImperialGravekeeper(L2Character actor)
  {
    super(actor);
  }

  protected void onEvtAttacked(L2Character attacker, int damage)
  {
    if ((attacker == null) || (attacker.getPlayer() == null) || (attacker.getPlayer().isGM()))
      return;
    FastList clanMembers;
    if (Rnd.chance(70))
    {
      int count = 0;
      for (L2NpcInstance npc : getActor().getKnownNpc(500))
      {
        if (npc.getNpcId() == 27181)
          ++count;
      }
      if ((count < 4) || (getActor().getMaxHp() / 3 > getActor().getCurrentHp()))
      {
        clanMembers = new FastList();
        for (L2Player player : getActor().getAroundPlayers(900))
        {
          if (player.isClanLeader())
          {
            QuestState qs530 = player.getQuestState("_503_PursuitClanAmbition");
            if ((qs530 != null) && (qs530.isStarted()))
            {
              for (L2Player member : getActor().getAroundPlayers(900))
              {
                if (member.getClan() == player.getClan())
                  clanMembers.add(member);
              }
              L2Player tpMember = (L2Player)clanMembers.get(Rnd.get(clanMembers.size()));
              if (tpMember != null)
              {
                int rndX = 30 + Rnd.get(100) * ((Rnd.chance(50)) ? 1 : -1);
                int rndY = 30 + Rnd.get(100) * ((Rnd.chance(50)) ? 1 : -1);
                tpMember.broadcastPacketToOthers(new MagicSkillUse(player, player, 4671, 1, 500, 0));
                tpMember.teleToLocation(171100 + rndX, 6510 + rndY, -2700);
              }
            }
          }
        }
      }
    }
    super.onEvtAttacked(attacker, damage);
  }
}