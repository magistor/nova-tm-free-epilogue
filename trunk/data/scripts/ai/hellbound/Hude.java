package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Files;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:42
 * http://nova-tm.ru/
 */
public class Hude extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/hude/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Hude");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void getSecondMark() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9676) >= 30 && getItemCount(p, 10012) >= 60) {
            removeItem(p, 9676, 30); // Mark of Betrayal
            removeItem(p, 10012, 60); // Scorpion Poison Stingers
            removeItem(p, 9850, 1); // Basic Caravan Certificate
            addItem(p, 9851, 1); // Standard Caravan Certificate
            show(Files.read(FilePatch + "caravan_hude004a.htm", p), p);
        } else {
            show(Files.read(FilePatch + "caravan_hude004b.htm", p), p);
        }
    }

    public void getThirdMark() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9681) >= 56 && getItemCount(p, 9682) >= 14) {
            removeItem(p, 9681, 56); // Life Force
            removeItem(p, 9682, 14); // Contained Life Force
            removeItem(p, 9851, 1); // Standard Caravan Certificate
            addItem(p, 9852, 1); // Premium Caravan Certificate
            show(Files.read(FilePatch + "caravan_hude006a.htm", p), p);
        } else {
            show(Files.read(FilePatch + "caravan_hude006b.htm", p), p);
        }
    }

    public void tradeSpecial() {
        final L2Player p = (L2Player) getSelf();
        final L2NpcInstance n = getNpc();
        if (getItemCount(p, 9851) > 0)
            n.onBypassFeedback(p, "Multisell 32298002");
        else {
            show(Files.read(FilePatch + "caravan_hude002b.htm", p), p);
        }
    }

    public void tradeDynasty() {
        final L2Player p = (L2Player) getSelf();
        final L2NpcInstance n = getNpc();
        if (getItemCount(p, 9852) > 0)
            n.onBypassFeedback(p, "Multisell 32298004");
        else {
            show(Files.read(FilePatch + "caravan_hude007.htm", p), p);
        }
    }
}
