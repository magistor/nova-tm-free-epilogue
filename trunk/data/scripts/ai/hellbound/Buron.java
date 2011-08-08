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
 * Time: 6:06
 * http://nova-tm.ru/
 */
public class Buron extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/buron/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Buron");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void craftNativeHelmet() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) == 0 && getItemCount(p, 9851) == 0 && getItemCount(p, 9852) == 0 && getItemCount(p, 9853) == 0) // нет марки
        {
            show(Files.read(FilePatch + "buron002b.htm", p), p);
        }

        if (getItemCount(p, 9674) >= 10) {
            removeItem(p, 9674, 10); // Darion's Badge
            addItem(p, 9669, 1); // Native Helmet
        } else {
            show(Files.read(FilePatch + "buron002a.htm", p), p);
        }
    }

    public void craftNativeTunic() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) == 0 && getItemCount(p, 9851) == 0 && getItemCount(p, 9852) == 0 && getItemCount(p, 9853) == 0) // нет марки
        {
            show(Files.read(FilePatch + "buron002b.htm", p), p);
        }

        if (getItemCount(p, 9674) >= 10) {
            removeItem(p, 9674, 10); // Darion's Badge
            addItem(p, 9670, 1); // Native Tunic
        } else {
            show(Files.read(FilePatch + "buron002a.htm", p), p);
        }
    }

    public void craftNativePants() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) == 0 && getItemCount(p, 9851) == 0 && getItemCount(p, 9852) == 0 && getItemCount(p, 9853) == 0) // нет марки
        {
            show(Files.read(FilePatch + "buron002b.htm", p), p);
        }

        if (getItemCount(p, 9674) >= 10) {
            removeItem(p, 9674, 10); // Darion's Badge
            addItem(p, 9671, 1); // Native Pants
        } else {
            show(Files.read(FilePatch + "buron002a.htm", p), p);
        }
    }

    public void Rumors() {
        final L2Player p = (L2Player) getSelf();

        int hLevel = HellboundManager.getInstance().getLevel();
        if (hLevel == 1) {
            show(Files.read(FilePatch + "buron003a.htm", p), p);
        }
        if (hLevel == 2) {
            show(Files.read(FilePatch + "buron003b.htm", p), p);
        }
        if (hLevel == 3) {
            show(Files.read(FilePatch + "buron003c.htm", p), p);
        }
        if (hLevel == 4) {
            show(Files.read(FilePatch + "buron003h.htm", p), p);
        }
        if (hLevel == 5) {
            show(Files.read(FilePatch + "buron003d.htm", p), p);
        }
        if (hLevel == 6) {
            show(Files.read(FilePatch + "buron003i.htm", p), p);
        }
        if (hLevel == 7) {
            show(Files.read(FilePatch + "buron003e.htm", p), p);
        }
        if (hLevel == 8) {
            show(Files.read(FilePatch + "buron003f.htm", p), p);
        }
        if (hLevel == 9) {
            show(Files.read(FilePatch + "buron003g.htm", p), p);
        }
        if (hLevel == 10) {
            show(Files.read(FilePatch + "buron003j.htm", p), p);
        }
        if (hLevel == 11) {
            show(Files.read(FilePatch + "buron003k.htm", p), p);
        }
    }

    public void Back() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "buron001.htm", p), p);
    }
}
