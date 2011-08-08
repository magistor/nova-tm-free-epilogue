package l2p.gameserver.serverpackets;

import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.L2ItemInstance;

public class GMViewItemList extends L2GameServerPacket
{
	private L2ItemInstance[] _items;
	private L2Player _player;

	public GMViewItemList(L2Player cha)
	{
		_items = cha.getInventory().getItems();
		_player = cha;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x9a);
		writeS(_player.getName());
		writeD(_player.getInventoryLimit()); //c4?
		writeH(1); // show window ??
		writeH(_items.length);
		for(L2ItemInstance temp : _items)
		{
			writeH(temp.getItem().getType1()); // item type1
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(0x00);
			writeQ(temp.getCount());
			writeH(temp.getItem().getType2ForPackets()); // item type2
			writeH(temp.getCustomType1()); // ?
			writeH(temp.isEquipped() ? 1 : 0);
			writeD(temp.getBodyPart()); // rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			writeH(temp.getEnchantLevel()); // enchant level
			writeH(temp.getCustomType2()); // ?
			writeD(temp.getAugmentationId());
			writeD(temp.isShadowItem() ? temp.getLifeTimeRemaining() : -1); //interlude
			writeItemElements(temp);
			writeD(temp.isTemporalItem() ? temp.getLifeTimeRemaining() : 0x00); // limited time item life remaining
			writeItemRev152();
		}
	}
}