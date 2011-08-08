package ai;

import l2p.extensions.listeners.DayNightChangeListener;
import l2p.extensions.listeners.PropertyCollection;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 8:21
 * http://nova-tm.ru/
 *
 *  NPC AI
 *  В ночное время исчезает.
 *  В дневное время спавнится.
 *
 */
public class DayAndNightNpc extends DefaultAI implements PropertyCollection {
    private L2NpcInstance actor = getActor();

    public DayAndNightNpc(L2Character actor) {
        super(actor);
        AI_TASK_DELAY = 1000;
        AI_TASK_ACTIVE_DELAY = 1000;
        GameTimeController.getInstance().getListenerEngine().addPropertyChangeListener("GameTimeController.DayNightChange", new NightAndDayNightListener());
    }

    private class NightAndDayNightListener extends DayNightChangeListener {
        private NightAndDayNightListener() {
            if (GameTimeController.getInstance().isNowNight())
                switchToNight();
            else
                switchToDay();
        }

        public void switchToNight() {
            if (actor != null)
                actor.decayMe();
        }

        public void switchToDay() {
            if (actor == null)
                actor.spawnMe();
        }
    }
}