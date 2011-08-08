package l2p.gameserver.serverpackets;

import l2p.gameserver.instancemanager.PrimeShopManager;

/**
 *
 * @author Nosferatus
 */
public class ExBR_ProductInfo extends L2GameServerPacket {

    private PrimeShopManager.ItemMallItem _item;

    public ExBR_ProductInfo(PrimeShopManager.ItemMallItem item) {
        _item = item;
    }

    @Override
    protected void writeImpl() {
        writeC(EXTENDED_PACKET);
        writeH(0xBA);
        writeD(_item.template.brId);
        writeD(_item.price);
        writeD(1);
        writeD(_item.template.itemId);
        writeD(_item.count);
        writeD(_item.iWeight);
        writeD((_item.iDropable) ? 1 : 0);
    }
}