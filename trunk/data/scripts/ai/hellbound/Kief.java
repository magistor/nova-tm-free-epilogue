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
 * Time: 6:51
 * http://nova-tm.ru/
 */
public class Kief extends Functions implements ScriptFile {
    private static String FilePatch = "data/html/hellbound/kief/";

    public void onLoad() {
        System.out.println("Loaded AI for Hellbound NPC: Kief");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void Badges() {
        final L2Player p = (L2Player) getSelf();
        long badges = getItemCount(p, 9674);
        if (badges < 1) {
            show(Files.read(FilePatch + "kief010a.htm", p), p);
        } else {
            long points = 10 * badges;
            HellboundManager.getInstance().addPoints(points);
            removeItem(p, 9674, badges);
            show(Files.read(FilePatch + "kief010.htm", p), p);
        }
    }

    public void Bottle() {
        final L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "kief011g.htm", p), p);
    }

    public void getBottle() {
        final L2Player p = (L2Player) getSelf();
        long stinger = getItemCount(p, 10012);
        if (stinger == 0) {
            show(Files.read(FilePatch + "kief011f.htm", p), p);
        } else if (stinger < 20) {
            show(Files.read(FilePatch + "kief011i.htm", p), p);
        } else {
            removeItem(p, 10012, 20);
            addItem(p, 9672, 1);
            show(Files.read(FilePatch + "kief011h.htm", p), p);
        }
    }

    public void dlf() {
        final L2Player p = (L2Player) getSelf();
        long dimlf = getItemCount(p, 9680);
        if (dimlf < 1) {
            show(Files.read(FilePatch + "kief011b.htm", p), p);
        } else {
            long points = 10 * dimlf;
            removeItem(p, 9680, dimlf);
            changePoints(points);
            checklvlup();
            show(Files.read(FilePatch + "kief011c.htm", p), p);
        }
    }

    public void lf() {
        final L2Player p = (L2Player) getSelf();
        long lifef = getItemCount(p, 9681);
        if (lifef < 1) {
            show(Files.read(FilePatch + "kief011b.htm", p), p);
        } else {
            long points = 20 * lifef;
            removeItem(p, 9681, lifef);
            changePoints(points);
            checklvlup();
            show(Files.read(FilePatch + "kief011e.htm", p), p);
        }
    }

    public void clf() {
        final L2Player p = (L2Player) getSelf();
        long conlf = getItemCount(p, 9682);
        if (conlf < 1) {
            show(Files.read(FilePatch + "kief011d.htm", p), p);
        } else {
            long points = 50 * conlf;
            removeItem(p, 9682, conlf);
            changePoints(points);
            checklvlup();
            show(Files.read(FilePatch + "kief011a.htm", p), p);
        }
    }

    public static void changePoints(long mod) {
        long curr = getPoints();
        long n = Math.max(0, mod + curr);
        if (curr != n) {
            ServerVariables.set("life_points", n);
        }
    }

    public static long getPoints() {
        return ServerVariables.getInt("life_points", 0);
    }

    public void checklvlup() {
        int curHBLevel = HellboundManager.getInstance().getLevel();
        long curr = getPoints();
        if (curr >= 1000000)
            if (curHBLevel == 7)
                HellboundManager.getInstance().changeLevel(8);
    }
}
