package l2p.gameserver.serverpackets;

import l2p.gameserver.model.items.L2ItemInstance;

public class TradeUpdate extends L2GameServerPacket
{
	private L2ItemInstance temp;
	private long _amount;

	public TradeUpdate(L2ItemInstance x, long amount)
	{
		temp = x;
		_amount = amount;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x81);
		writeH(1);
		boolean stackable = temp.isStackable();
		if(_amount == 0)
		{
			_amount = 1;
			stackable = false;
		}
		writeH(stackable ? 3 : 2);
		int type = temp.getItem().getType1();
		writeH(type); // item type1
		writeD(temp.getObjectId());
		writeD(temp.getItemId());
		writeQ(_amount);
		writeH(temp.getItem().getType2ForPackets()); // item type2
		writeH(0x00); // ?
		writeD(temp.getBodyPart()); // rev 415  slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
		writeH(temp.getEnchantLevel()); // enchant level
		writeH(0x00); // ?
		writeH(0x00);
		writeItemElements(temp);
	}
}