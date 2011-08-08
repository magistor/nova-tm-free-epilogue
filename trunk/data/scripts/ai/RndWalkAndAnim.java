package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Location;
import l2p.util.Rnd;

public class RndWalkAndAnim extends DefaultAI {
    protected static final int PET_WALK_RANGE = 100;

    public RndWalkAndAnim(L2Character actor) {
        super(actor);
    }

    @Override
    protected boolean thinkActive() {
        L2NpcInstance actor = getActor();
        if (actor == null || actor.isMoving) {
            return false;
        }
        int val = Rnd.get(100);
        if (val < 10) {
            randomWalk();
        } else if (val < 20) {
            actor.onRandomAnimation();
        }
        return false;
    }

    @Override
    protected boolean randomWalk() {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return false;
        }
        Location sloc = actor.getSpawnedLoc();
        int x = sloc.x + Rnd.get(2 * PET_WALK_RANGE) - PET_WALK_RANGE;
        int y = sloc.y + Rnd.get(2 * PET_WALK_RANGE) - PET_WALK_RANGE;
        int z = GeoEngine.getHeight(x, y, sloc.z, actor.getReflection().getGeoIndex());
        actor.setRunning();
        actor.moveToLocation(x, y, z, 0, true);
        return true;
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
    }

    @Override
    protected void onEvtAggression(L2Character target, int aggro) {
    }
}