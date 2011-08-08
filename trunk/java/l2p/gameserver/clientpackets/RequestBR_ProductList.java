package l2p.gameserver.clientpackets;

import l2p.gameserver.instancemanager.PrimeShopManager;
import l2p.gameserver.model.L2Player;

/**
 *
 * @author Nosferatus (c)
 */
public class RequestBR_ProductList extends L2GameClientPacket {

    @Override
    public void readImpl() {
    }

    @Override
    public void runImpl() {
        L2Player player = getClient().getActiveChar();

        if (player == null) {
            return;
        }
        PrimeShopManager.getInstance().showList(player);
    }
}