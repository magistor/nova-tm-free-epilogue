package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.model.L2Player;
import l2p.util.Files;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 7:21
 * http://nova-tm.ru/
 */
public class Slave extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/slave/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Slave");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void Badges() {
        final L2Player p = (L2Player) getSelf();
        long badges = getItemCount(p, 9674);
        if (badges < 5) {
            show(Files.read(FilePatch + "incastle_native002a.htm", p), p);
        } else {
            removeItem(p, 9674, 5);
            if (p.getVar("badgesamount") == null)
                p.setVar("badgesamount", "1");
            else {
                int badgesamount = Integer.parseInt(p.getVar("badgesamount"));
                badgesamount++;
                if (badgesamount < 6)
                    p.setVar("badgesamount", String.valueOf(badgesamount));
                else {
                    HellboundManager.getInstance().changeLevel(10);
                    p.unsetVar("badgesamount");
                    show(Files.read(FilePatch + "incastle_native002.htm", p), p);
                }
            }
        }
    }
}
