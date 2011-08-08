// by dELakPya 
package ai;

import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.serverpackets.SocialAction;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;
import l2p.util.Rnd;

public class BizarreCocoon extends DefaultAI {
    private static final int Growth_Accelerator = 2905;
    private static final int Stakato_Cheif = 25667;

    public BizarreCocoon(L2Character actor) {
        super(actor);
    }

    protected boolean randomAnimation() {
        return false;
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    @Override
    protected void onEvtSeeSpell(L2Skill skill, L2Character caster) {
        L2NpcInstance actor = getActor();
        if (actor == null || actor.isDead() || skill == null) {
            return;
        }
        if (skill.getId() == Growth_Accelerator) {
            if (Rnd.chance(92)) {
                try {
                    L2Spawn sp = new L2Spawn(NpcTable.getTemplate(Stakato_Cheif));
                    Location pos = GeoEngine.findPointToStay(actor.getX(), actor.getY(), actor.getZ(), 0, 0, actor.getReflection().getGeoIndex());
                    sp.setLoc(pos);
                    L2NpcInstance npc = sp.doSpawn(true);
                    actor.broadcastPacket(new SocialAction(actor.getObjectId(), 1));
                    npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, caster, Rnd.get(1, 100));
                    actor.doDie(caster);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                actor.broadcastPacket(new SocialAction(actor.getObjectId(), 1));
                actor.doDie(caster);
            }
        }
    }
}