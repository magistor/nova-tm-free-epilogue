/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import l2p.extensions.scripts.Functions;
import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Rnd;

/**
 * @author Unkonown, Angy
 *         mobId 20270
 */
public class BrekaOrcOverlord extends DefaultAI {
    private static boolean _firstTimeAttacked;

    public BrekaOrcOverlord(L2Character actor) {
        super(actor);
        _firstTimeAttacked = true;
    }

    @Override
    protected void onEvtAttacked(L2Character attacker, int damage) {
        L2NpcInstance actor = getActor();
        if (actor == null) {
            return;
        }
        if (_firstTimeAttacked) {
            if (Rnd.get(100) == 50) {
                Functions.npcSay(actor, "Extreme strength! ! ! !");
            } else if (Rnd.get(100) == 50) {
                Functions.npcSay(actor, "Humph, wanted to win me to be also in tender!");
            } else if (Rnd.get(100) == 50) {
                Functions.npcSay(actor, "Haven't thought to use this unique skill for this small thing!");
            }
            _firstTimeAttacked = false;
        }
        super.onEvtAttacked(attacker, damage);
    }

    @Override
    protected void onEvtDead(L2Character killer) {
        _firstTimeAttacked = true;
        super.onEvtDead(killer);
    }
}