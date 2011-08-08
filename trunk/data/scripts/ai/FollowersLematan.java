package ai;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.SkillTable;

import java.util.concurrent.ScheduledFuture;

public class FollowersLematan extends Fighter {
    private static int LEMATAN = 18633;
    private ScheduledFuture<?> _isSkill;

    public FollowersLematan(L2Character actor) {
        super(actor);
    }

    public void onTimer(String event) {
        L2NpcInstance actor = getActor();
        if (event.equals("Skill")) {
            if (actor.getNpcId() == LEMATAN) {
                actor.setTarget(actor);
                actor.doCast(SkillTable.getInstance().getInfo(5712, 1), actor, true);
            } else {
                return;
            }
            if (_isSkill != null) {
                _isSkill.cancel(false);
                _isSkill = null;
            }
        }
    }

    public void startSkillTimer() {
        _isSkill = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask("Skill", this), 20000);
    }

    private class ScheduleTimerTask implements Runnable {
        private String _name;
        private FollowersLematan _caller;

        public ScheduleTimerTask(String name, FollowersLematan classPtr) {
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
        if (_isSkill != null) {
            _isSkill.cancel(false);
            _isSkill = null;
        }
        super.onEvtDead(killer);
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}