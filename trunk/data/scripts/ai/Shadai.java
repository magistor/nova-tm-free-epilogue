package ai;

import l2p.Config;
import l2p.extensions.listeners.DayNightChangeListener;
import l2p.extensions.listeners.PropertyCollection;
import l2p.gameserver.Announcements;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 5:06
 * http://nova-tm.ru/
 * <p/>
 * AI Shadai для Hellbound.
 * Раз в сутки по ночам спавнится на определённом месте на острове с шансом 40%.
 * На оффе некоторые ждут по 2 недели.
 */
public class Shadai extends DefaultAI implements PropertyCollection {
    private boolean _tmp1 = false;

    public Shadai(L2Character actor) {
        super(actor);
        GameTimeController.getInstance().getListenerEngine().addPropertyChangeListener(GameTimeControllerDayNightChange, new NightInvulDayNightListener());
        actor.decayMe();
    }

    private class NightInvulDayNightListener extends DayNightChangeListener {
        private NightInvulDayNightListener() {
            if (GameTimeController.getInstance().isNowNight())
                switchToNight();
            else
                switchToDay();
        }

        public void switchToNight() {
            spawn_despawn(getActor(), true);
        }

        public void switchToDay() {
            spawn_despawn(getActor(), false);
        }
    }

    private void spawn_despawn(L2NpcInstance actor, boolean Night) {
        if (Night) {
            if (!actor.isVisible() && Rnd.chance(Config.CHANCE_SPAWN_SHADAI)) {
                actor.spawnMe();
                if (!Config.ANNOUNCE_SHADAI_SPAWN) {
                    Announcements.getInstance().announceByCustomMessage("scripts.ai.Shadai.announce", null);
                }
            }
        } else {
            if (actor.isVisible())
                actor.decayMe();
        }
    }

    public boolean isGlobalAI() {
        return true;
    }

    protected boolean randomWalk() {
        return false;
    }
}
