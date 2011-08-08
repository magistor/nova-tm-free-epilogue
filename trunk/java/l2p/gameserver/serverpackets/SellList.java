package l2p.gameserver.serverpackets;

import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.L2ItemInstance;

import java.util.TreeSet;

@Deprecated
public class SellList extends L2GameServerPacket
{
	private long _money;
	private TreeSet<L2ItemInstance> _selllist = new TreeSet<L2ItemInstance>(Inventory.OrderComparator);

	/**
	 * Список вещей для продажи в обычный магазин
	 *
	 * @param player
	 */
	public SellList(L2Player player)
	{
		_money = player.getAdena();
		for(L2ItemInstance item : player.getInventory().getItemsList())
		{
			if(item.getItem().isSellable() && item.canBeTraded(player))
			{
				_selllist.add(item);
			}
		}
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x06);
		writeQ(_money);
		writeD(0); //_listId etc?
		writeH(_selllist.size());
		for(L2ItemInstance item : _selllist)
		{
			writeH(item.getItem().getType1());
			writeD(item.getObjectId());
			writeD(item.getItemId());
			writeQ(item.getCount());
			writeH(item.getItem().getType2ForPackets());
			writeH(item.getCustomType1());
			writeD(item.getBodyPart());
			writeH(item.getEnchantLevel());
			writeH(item.getCustomType2());
			writeH(0x00); // unknown
			writeQ(item.getItem().getReferencePrice() / 2);
			writeItemElements(item);
		}
	}
}