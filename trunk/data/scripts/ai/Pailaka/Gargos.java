package ai.Pailaka;

import l2p.common.ThreadPoolManager;
import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SkillTable;

import java.util.concurrent.ScheduledFuture;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 5:06
 * http://nova-tm.ru/
 */

public class Gargos extends Fighter {
    private L2NpcInstance actor = getActor();
    private ScheduledFuture<?> _isFire;

    public Gargos(L2Character actor) {
        super(actor);
        startFireTimer();
    }

    public void onTimer(String event) {
        if (event.equals("Fire")) {
            Functions.npcSayCustomMessage(actor, "scripts.ai.Pailaka.Gargos.attack");
            actor.doCast(SkillTable.getInstance().getInfo(5705, 1), actor, true);
            startFireTimer();
        }
    }

    public void startFireTimer() {
        _isFire = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask("Fire", this), 60000);
    }

    private class ScheduleTimerTask implements Runnable {
        private String _name;
        private Gargos _caller;

        public ScheduleTimerTask(String name, Gargos classPtr) {
            _name = name;
            _caller = classPtr;
        }

        @Override
        public void run() {
            _caller.onTimer(_name);
        }
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        // stop timers if any
        if (_isFire != null) {
            _isFire.cancel(false);
            _isFire = null;
        }
        super.onEvtDead(killer);
    }
}