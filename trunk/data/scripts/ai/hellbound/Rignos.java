package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.serverpackets.MagicSkillUse;
import l2p.gameserver.tables.SkillTable;
import l2p.util.Files;
import l2p.util.GArray;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:54
 * http://nova-tm.ru/
 */
public class Rignos extends Functions implements ScriptFile {
    public void onLoad() {
        System.out.println("Loaded Service: Isle of Player Race");
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    private static String FilePatch = "data/html/hellbound/rignos/32349";//TODO добавить html

    public void Info() {
        final L2Player p = (L2Player) getSelf();
        show(Files.read(FilePatch + "-1.htm", p), p);
    }

    public void getTask() {
        final L2Player p = (L2Player) getSelf();
        boolean canTask = true;

        if (p.getLevel() < 78) {
            p.sendMessage("level < 78");
            show(Files.read(FilePatch + "-2.htm", p), p);
        } else {
            p.sendMessage("level > 78");
            if (canTask) {
                p.sendMessage("canTask != null");
                startRace();
            } else {
                p.sendMessage("canTask == null");
                show(Files.read(FilePatch + "-2.htm", p), p);
            }
        }
    }

    private void startRace() {
        final L2Player activeChar = (L2Player) getSelf();
        L2Skill skill = SkillTable.getInstance().getInfo(5239, 5);
        if (skill != null) {
            L2Character target = null;
            L2Object obj = activeChar.getTarget();
            if (obj != null && obj.isCharacter()) {
                target = (L2Character) obj;
            }
            if (target == null) {
                target = activeChar;
            }
            GArray<L2Character> targets = new GArray<L2Character>();
            targets.add(target);
            activeChar.callSkill(skill, targets, false);
            activeChar.sendPacket(new MagicSkillUse(activeChar, 5239, 5, skill.getHitTime(), 0));
            activeChar.setVar("RaceStarted", "started");
        }
    }

    public void finishRace() {
        final L2Player p = (L2Player) getSelf();
        if (getItemCount(p, 9850) < 4)
            return;

        // removeItem(p, 10013, -1);
        addItem(p, 9694, 3);

        p.getEffectList().stopEffectByDisplayId(5239);

        if (p.getPet() != null)
            p.getPet().getEffectList().stopEffectByDisplayId(5239);
        show(Files.read(FilePatch + "-5.htm", p), p);
        p.unsetVar("RaceStarted");
    }
}
