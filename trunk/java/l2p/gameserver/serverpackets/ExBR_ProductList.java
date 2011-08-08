package l2p.gameserver.serverpackets;

import java.util.Collection;
import l2p.Config;
import l2p.gameserver.instancemanager.PrimeShopManager;
import l2p.gameserver.instancemanager.PrimeShopManager.ItemMallItem;

/**
 *
 * @author Nosferatus
 */
public class ExBR_ProductList extends L2GameServerPacket {

    public Collection<PrimeShopManager.ItemMallItem> col;

    @Override
    protected void writeImpl() {
        writeC(EXTENDED_PACKET);
        writeH(0xB9);
        writeD(col.size());
        for (ItemMallItem cs : col) {
            writeD(cs.template.brId);
            writeH(cs.template.category);
            writeD(cs.price);
            int cat = 0;
            if (cs.iSale >= Config.colUp) {
                switch (cs.iCategory2) {
                    case 0:
                    case 2:
                        cat = 2;
                        break;
                    case 1:
                        cat = 3;
                }
            }
            writeD(cat);//дополнительная категория, 0 - откл, 1 - ивент, 2 - выбор дня, 3 и туда и туда. для ивентовых, слева от молла при открытии вылазит песдатое окошко^^
            if ((cs.iStartSale > 0) && (cs.iEndSale > 0)) {
                writeD(cs.iStartSale); //start_sale //12CEDE40
                writeD(cs.iEndSale); //end_sale //7ECE3CD0
            } else {
                writeD(0x12CEDE40);
                writeD(0x7ECE3CD0);
            }
            writeC(0x7F); //DayWeek. Хз как задействовать
            writeC(cs.iStartHour); //start_hour 0-23
            writeC(cs.iStartMin); //start_min 0-59
            writeC(cs.iEndHour); //end_hour 0-23
            writeC(cs.iEndMin); //end_min 0-59
            writeD(cs.iStock); //сколько уже купили от макс стока
            writeD(cs.iMaxStock); //max_stock
        }
    }

    @Override
    public String getType()
  {
    return ExBR_ProductList.class.getName();
  }
}
