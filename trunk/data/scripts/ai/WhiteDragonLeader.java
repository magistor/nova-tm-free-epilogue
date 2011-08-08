package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.ExShowScreenMessage;
import l2p.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;

public class WhiteDragonLeader extends Fighter {
    public WhiteDragonLeader(L2Character actor) {
        super(actor);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor != null)
        // ru: Враг зашел! Приготовьтесь защищаться!!
        {
            actor.broadcastPacket(new ExShowScreenMessage("Враг напал! Приготовьтесь защищаться!!!", 3000, ScreenMessageAlign.MIDDLE_CENTER, false));
        }
        super.onEvtDead(killer);
    }
}