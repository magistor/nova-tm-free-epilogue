package l2p.gameserver.clientpackets;

import l2p.gameserver.instancemanager.PrimeShopManager;
import l2p.gameserver.model.L2Player;

/**
 *
 * @author Nosferatus (c)
 */
public class RequestBR_BuyProduct extends L2GameClientPacket {

    private int iProductID;
    private int iAmount;

    @Override
    public void readImpl() {
        iProductID = readD();
        iAmount = readD();
    }

    @Override
    public void runImpl() {
        L2Player player = getClient().getActiveChar();

        if (player == null) {
            return;
        }
        PrimeShopManager.getInstance().requestBuyItem(player, iProductID, iAmount);
    }
}