package l2p.gameserver.clientpackets;

import l2p.gameserver.instancemanager.PrimeShopManager;
import l2p.gameserver.model.L2Player;

/**
 *
 * @author Nosferatus (c)
 */
public class RequestBR_ProductInfo extends L2GameClientPacket {

    private int iProductID;

    @Override
    public void readImpl() {
        this.iProductID = readD();
    }

    @Override
    public void runImpl() {
        L2Player player = getClient().getActiveChar();

        if (player == null) {
            return;
        }
        PrimeShopManager.getInstance().showItemInfo(player, iProductID);
    }
}