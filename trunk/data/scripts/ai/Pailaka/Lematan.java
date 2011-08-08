package ai.Pailaka;

import l2p.extensions.listeners.L2ZoneEnterLeaveListener;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.instancemanager.ZoneManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.L2Zone;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.tables.NpcTable;
import l2p.util.Location;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 6:50
 * http://nova-tm.ru/
 */
public class Lematan extends Fighter {
    private static int LEMATAN_FOLLOWER = 18634;
    private boolean _teleported;
    private static L2Zone _zone;
    private static L2Zone _zone1;
    private static ZoneListener _zoneListener = new ZoneListener();

    public Lematan(L2Character actor) {
        super(actor);
        _zone = ZoneManager.getInstance().getZoneById(L2Zone.ZoneType.dummy, 702111, false);
        _zone.getListenerEngine().addMethodInvokedListener(_zoneListener);
        _zone1 = ZoneManager.getInstance().getZoneById(L2Zone.ZoneType.dummy, 702112, false);
        _zone1.getListenerEngine().addMethodInvokedListener(_zoneListener);
        _teleported = false;
    }

    protected boolean maybeMoveToHome() {
        L2NpcInstance actor = getActor();
        Location loc = actor.getSpawnedLoc();

        return actor.isInRange(loc, 10000);
    }

    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();

        boolean part = actor.getCurrentHp() < actor.getMaxHp() / 2;
        if (((!getZone().checkIfInZone(actor)) && (!getZone1().checkIfInZone(actor))) || (!part)) {
            if ((part) && (!_teleported)) {
                Location loc = new Location(84968, -208728, -3367);
                actor.setSpawnedLoc(loc);
                actor.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE, null, null);
                actor.teleToLocation(loc);

                _teleported = true;
                try {
                    L2Spawn spawn1 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn2 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn3 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn4 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn5 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn6 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn7 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    L2Spawn spawn8 = new L2Spawn(NpcTable.getTemplate(LEMATAN_FOLLOWER));
                    Location pos1 = GeoEngine.findPointToStay(84712, -208728, -3365, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos2 = GeoEngine.findPointToStay(85240, -208744, -3364, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos3 = GeoEngine.findPointToStay(84584, -208936, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos4 = GeoEngine.findPointToStay(84552, -208536, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos5 = GeoEngine.findPointToStay(84552, -208728, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos6 = GeoEngine.findPointToStay(85400, -208936, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos7 = GeoEngine.findPointToStay(85400, -208536, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    Location pos8 = GeoEngine.findPointToStay(85416, -208776, -3361, 0, 0, actor.getReflection().getGeoIndex());
                    spawn1.setReflection(actor.getReflection().getId());
                    spawn2.setReflection(actor.getReflection().getId());
                    spawn3.setReflection(actor.getReflection().getId());
                    spawn4.setReflection(actor.getReflection().getId());
                    spawn5.setReflection(actor.getReflection().getId());
                    spawn6.setReflection(actor.getReflection().getId());
                    spawn7.setReflection(actor.getReflection().getId());
                    spawn8.setReflection(actor.getReflection().getId());
                    spawn1.setLoc(pos1);
                    spawn2.setLoc(pos2);
                    spawn3.setLoc(pos3);
                    spawn4.setLoc(pos4);
                    spawn5.setLoc(pos5);
                    spawn6.setLoc(pos6);
                    spawn7.setLoc(pos7);
                    spawn8.setLoc(pos8);
                    spawn1.doSpawn(true);
                    spawn2.doSpawn(true);
                    spawn3.doSpawn(true);
                    spawn4.doSpawn(true);
                    spawn5.doSpawn(true);
                    spawn6.doSpawn(true);
                    spawn7.doSpawn(true);
                    spawn8.doSpawn(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onEvtAttacked(attacker, damage);
    }

    public static L2Zone getZone() {
        return _zone;
    }

    public static L2Zone getZone1() {
        return _zone1;
    }

    public static class ZoneListener extends L2ZoneEnterLeaveListener {
        public void objectEntered(L2Zone zone, L2Object object) {
        }

        public void objectLeaved(L2Zone zone, L2Object object) {
        }
    }
}