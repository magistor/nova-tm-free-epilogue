package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.model.L2Character;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 4:09
 * http://nova-tm.ru/
 */
public class HBFighter extends Fighter {
    private L2Character _atacker;

    public HBFighter(L2Character actor) {
        super(actor);
    }

    protected void onEvtAttacked(L2Character attacker, int damage) {
        _atacker = attacker;
        super.onEvtAttacked(_atacker, damage);
    }

    protected void onEvtDead(L2Character killer) {
        int id = getActor().getNpcId();
        int hLevel = HellboundManager.getInstance().getLevel();

        switch (id) {
            case 22320:
            case 22324:
            case 22325:
                if (hLevel <= 1)
                    HellboundManager.getInstance().addPoints(1);
                break;
            case 22327:
            case 22329:
                if (hLevel <= 1)
                    HellboundManager.getInstance().addPoints(3);
                break;
            case 22322:
            case 22323:
            case 22450:
                HellboundManager.getInstance().addPoints(-10);
                break;
            case 22361:
                HellboundManager.getInstance().addPoints(20);
                break;
            case 25536:
                HellboundManager.getInstance().addPoints(200);
                break;
            case 22341:
                if (hLevel == 3)
                    HellboundManager.getInstance().addPoints(100);
                break;
            case 18466:
                HellboundManager.getInstance().changeLevel(9);
                break;
            case 22326:
                if (hLevel == 6) {
                    HellboundManager.getInstance().addPoints(300);
                }
                break;
            case 18467:
                if (hLevel == 7) {
                    int curr = getmbguard();
                    changembguard(1);
                    if (curr >= 7)
                        ServerVariables.set("mbguard", "false");
                }
                break;
        }

        super.onEvtDead(killer);
    }

    public static void changembguard(int mod) {
        int curr = getmbguard();
        int n = Math.max(0, mod + curr);
        if (curr != n) {
            ServerVariables.set("mb_guard", n);
        }
    }

    public static int getmbguard() {
        return ServerVariables.getInt("mb_guard", 0);
    }
}
