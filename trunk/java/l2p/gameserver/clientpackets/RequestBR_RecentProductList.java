package l2p.gameserver.clientpackets;

import l2p.extensions.network.ReceivablePacket;
import l2p.gameserver.network.L2GameClient;

/**
 *
 * @author Nosferatus
 */
public class RequestBR_RecentProductList extends ReceivablePacket<L2GameClient> {

    @Override
    protected boolean read() {
        return false;
    }

    @Override
    public void run() {
    }
}