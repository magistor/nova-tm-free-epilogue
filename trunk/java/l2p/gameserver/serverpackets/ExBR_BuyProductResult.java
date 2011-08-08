package l2p.gameserver.serverpackets;

/**
 *
 * @author Nosferatus
 */
public class ExBR_BuyProductResult extends L2GameServerPacket {

    private int _code;

    public ExBR_BuyProductResult(int code) {
        _code = code;
    }

    @Override
    protected void writeImpl() {
        writeC(EXTENDED_PACKET);
        writeH(0xBB);
        writeD(_code);
    }

    @Override
    public String getType() {
        return ExBR_BuyProductResult.class.getName();
    }
}