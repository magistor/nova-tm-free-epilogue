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
 * Time: 5:36
 * http://nova-tm.ru/
 */
public class Bernarde extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/bernarde/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Bernarde");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void HolyWater() {
        L2Player p = (L2Player) getSelf();

        if (getItemCount(p, 9674) < 5) // Darion's Badge
        {
            show(Files.read(FilePatch + "bernarde002c.htm", p), p);
        } else {
            removeItem(p, 9674, 5); // Darion's Badge
            addItem(p, 9673, 1); // Holy Water
            show(Files.read(FilePatch + "bernarde002b.htm", p), p);
        }
    }

    public void help() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "bernarde003h.htm", p), p);
    }

    public void alreadysaid() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "bernarde003b.htm", p), p);
    }

    public void Derek() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "bernarde003d.htm", p), p);
    }

    public void rumors() {
        L2Player p = (L2Player) getSelf();
        int hLevel = HellboundManager.getInstance().getLevel();

        if (hLevel == 6) {
            show(Files.read(FilePatch + "bernarde003i.htm", p), p);
        } else if (hLevel == 7) {
            show(Files.read(FilePatch + "bernarde003c.htm", p), p);
        } else if (hLevel == 8) {
            show(Files.read(FilePatch + "bernarde003f.htm", p), p);
        }
    }

    public void ruins() {
        // TODO
    }

    public void treasure() {
        L2Player p = (L2Player) getSelf();
        long treasure = getItemCount(p, 9684);
        boolean condition = Boolean.parseBoolean(p.getVar("bernarde"));

        if (condition) {
            show(Files.read(FilePatch + "bernarde003a.htm", p), p);
        }

        if (treasure < 1) {
            show(Files.read(FilePatch + "bernarde002e.htm", p), p);
        } else {
            removeItem(p, 9684, 1); // Native Treasure
            p.setVar("bernarde", "true");
            show(Files.read(FilePatch + "bernarde002d.htm", p), p);
        }

        boolean condition2 = Boolean.parseBoolean(p.getVar("jude"));
        int hLevel = HellboundManager.getInstance().getLevel();

        if (condition && condition2 && HellboundManager.getInstance().getPoints() >= 1000000) {
            if (hLevel == 3) {
                ServerVariables.set("HellboundCanChangeLevel", true);
                HellboundManager.getInstance().changeLevel(4);
                p.unsetVar("bernarde");
                if (p.getVar("jude") != null)
                    p.unsetVar("jude");
            }
        }
    }
}
