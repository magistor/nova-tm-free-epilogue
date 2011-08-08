package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2World;
import l2p.gameserver.model.instances.L2DoorInstance;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.modules.data.DoorTable;
import l2p.gameserver.serverpackets.Say2;
import l2p.util.Files;
import l2p.util.Location;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:15
 * http://nova-tm.ru/
 */
public class HellboundTraitor extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/Traitor/";

    private static final int Leodas = 22448;

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Traitor");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void MarksOfBetrayal() {
        final L2Player p = (L2Player) getSelf();
        final L2NpcInstance n = getNpc();
        L2NpcInstance LeodasRB = L2World.findNpcByNpcId(Leodas);
        int hellboundLevel = HellboundManager.getInstance().getLevel();
        if (LeodasRB != null) {
            show(Files.read(FilePatch + "32364-7.htm", p), p);
            return;
        }
        if (hellboundLevel >= 5 && hellboundLevel <= 6) {
            long marksCount = p.getInventory().getItemByItemId(9676).getCount();
            if (marksCount == 0) {
                show(Files.read(FilePatch + "32364-4.htm", p), p);
            } else if (marksCount >= 1 && marksCount < 10) {
                show(Files.read(FilePatch + "32364-6.htm", p), p);
            } else if (marksCount >= 10) {
                final L2DoorInstance Nativedoor3 = DoorTable.getInstance().getDoor(19250003);
                final L2DoorInstance Nativedoor4 = DoorTable.getInstance().getDoor(19250004);

                removeItem(p, 9676, 10); // Marks of Betrayal
                n.broadcastPacket(new Say2(n.getObjectId(), 1, n.getName(), "Братья! Незнакомец хочет убить нашего командира!"));
                Nativedoor3.openMe();
                Nativedoor3.onOpen();

                Nativedoor4.openMe();
                Nativedoor4.onOpen();
                Functions.spawn(new Location(-27807, 252740, -3520, 0), Leodas);
            }
        }
    }
}
