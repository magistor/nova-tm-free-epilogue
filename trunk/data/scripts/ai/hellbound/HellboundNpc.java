package ai.hellbound;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 6:14
 * http://nova-tm.ru/
 */
public class HellboundNpc extends Functions implements ScriptFile {
    public void onLoad() {
    }

    public void onReload() {
    }

    public void onShutdown() {
    }

    public void buyMagicBottle() {
        final L2Player p = (L2Player) getSelf();
        final L2NpcInstance n = getNpc();
        if (getItemCount(p, 9851) == 0 && getItemCount(p, 9852) == 0 && getItemCount(p, 9853) == 0) // нет второй или выше марки
        {
            n.onBypassFeedback(p, "Chat 1");
            return;
        }

        if (getItemCount(p, 10012) >= 20) {
            removeItem(p, 10012, 20); // Scorpion Poison Stingers
            addItem(p, 9672, 1); // Magic Bottle
        } else
            n.onBypassFeedback(p, "Chat 1");
    }
}
