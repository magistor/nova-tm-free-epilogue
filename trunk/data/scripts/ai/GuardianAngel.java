package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 *  AI for Guardian Angel
 * Guardian Angel (Ангел Хранитель ) — агрессивный монстр 72 уровня Angels расы.
 * Обитает в локациях Pirate Tunnel. Также на этом монстре можно прокачать кристаллы (SA) до 10 уровня.
 */
public class GuardianAngel extends DefaultAI {
    static final String[] flood = {"Аааааахх! Отойдите от проклятой коробки! Я возьму её сам!",
            "Кто вы и почему вы остановили меня?", "Я был поражен..."};

    public GuardianAngel(L2Character actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        L2NpcInstance actor = getActor();
        if (actor != null) {
            Functions.npcSay(actor, flood[Rnd.get(2)]);
        }
        return super.thinkActive();
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        L2NpcInstance actor = getActor();
        if (actor != null) {
            Functions.npcSay(actor, flood[2]);
        }
        super.onEvtDead(killer);
    }
}