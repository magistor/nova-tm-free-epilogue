package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.*;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.util.Rnd;

public class StarStone3 extends DefaultAI
{

    public StarStone3(L2Character actor)
    {
        super(actor);
    }

    protected void onEvtSeeSpell(L2Skill skill, L2Character caster)
    {
        L2NpcInstance actor = getActor();
        L2Player player = caster.getPlayer();
        QuestState CollectingInTheAir = player.getQuestState("_10274_CollectingInTheAir");
        if(CollectingInTheAir == null)
            return;
        if(skill.getId() == 932)
        {
            L2Player killer = null;
            L2Character MostHated = actor.getMostHated();
            if(MostHated != null && MostHated.isPlayable())
                killer = MostHated.getPlayer();
            if(Rnd.chance(33))
            {
                caster.sendPacket(new SystemMessage(2500));
                actor.dropItem(killer, 14011, Rnd.get(2, 2));
            } else
            if(skill.getLevel() == 1 && Rnd.chance(15) || skill.getLevel() == 2 && Rnd.chance(50) || skill.getLevel() == 3 && Rnd.chance(75))
            {
                caster.sendPacket(new SystemMessage(2500));
                actor.dropItem(killer, 14011, Rnd.get(1, 1));
            } else
            {
                caster.sendPacket(new SystemMessage(2424));
            }
            actor.getSpawn().decreaseCount(actor);
            actor.deleteMe();
        }
    }
}
