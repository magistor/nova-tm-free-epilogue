package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.util.Files;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:11
 * http://nova-tm.ru/
 */
public class Falk extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/falk/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Falk");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void Go() {
        L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) < 1) {
            show(Files.read(FilePatch + "falk002.htm", p), p);
        } else {
            show(Files.read(FilePatch + "falk001a.htm", p), p);
        }
    }

    public void getFirstMark() {
        L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) > 0 || getItemCount(p, 9851) > 0 || getItemCount(p, 9852) > 0 || getItemCount(p, 9853) > 0) // уже есть
        {
            show(Files.read(FilePatch + "falk002d.htm", p), p);
        } else if (getItemCount(p, 9674) >= 20) {
            removeItem(p, 9674, 20); // Darion's Badge
            addItem(p, 9850, 1); // Basic Caravan Certificate
            show(Files.read(FilePatch + "falk002a.htm", p), p);
        } else {
            show(Files.read(FilePatch + "falk002b.htm", p), p);
        }
    }

    public void Back1a() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "falk001a.htm", p), p);
    }

    public void Back2() {
        L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "falk002.htm", p), p);
    }
}
