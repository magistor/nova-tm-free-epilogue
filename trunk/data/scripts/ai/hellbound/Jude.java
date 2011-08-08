package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.model.L2Player;
import l2p.util.Files;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:48
 * http://nova-tm.ru/
 */
public class Jude extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/jude/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Jude");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void Treasure() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9684) < 40) {
            p.setVar("jude", "true");
            show(Files.read(FilePatch + "jude002a.htm", p), p);
        } else {
            removeItem(p, 9684, 40);
            show(Files.read(FilePatch + "jude002.htm", p), p);
        }

        boolean condition = Boolean.parseBoolean(p.getVar("bernarde"));
        boolean condition2 = Boolean.parseBoolean(p.getVar("jude"));
        int hLevel = HellboundManager.getInstance().getLevel();

        if (condition && condition2 && HellboundManager.getInstance().getPoints() >= 1000000) {
            if (hLevel == 3) {
                ServerVariables.set("HellboundCanChangeLevel", true);
                HellboundManager.getInstance().changeLevel(4);
                if (p.getVar("bernarde") != null)
                    p.unsetVar("bernarde");
                p.unsetVar("jude");
            }
        }
    }
}
