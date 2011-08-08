package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;

/**
 *
 * @author Nosferatus (c)
 */
public class RequestBR_GamePoint extends L2GameClientPacket {

    @Override
    public void readImpl() {
    }

    @Override
    public void runImpl() {
        L2Player activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }
    }
}