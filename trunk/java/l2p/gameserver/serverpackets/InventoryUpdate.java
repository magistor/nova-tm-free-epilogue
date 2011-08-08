package l2p.gameserver.serverpackets;

import l2p.gameserver.model.items.L2ItemInstance;
import l2p.util.GArray;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * sample
 * <p/>
 * 21			// packet type
 * 01 00			// item count
 * <p/>
 * 03 00			// update type   01-added?? 02-modified 03-removed
 * 04 00			// itemType1  0-weapon/ring/earring/necklace  1-armor/shield  4-item/questitem/adena
 * c6 37 50 40	// objectId
 * cd 09 00 00	// itemId
 * 05 00 00 00	// count
 * 05 00			// itemType2  0-weapon  1-shield/armor  2-ring/earring/necklace  3-questitem  4-adena  5-item
 * 00 00			// always 0 ??
 * 00 00			// equipped 1-yes
 * 00 00 00 00	// slot  0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
 * 00 00			// enchant level
 * 00 00			// always 0 ??
 * 00 00 00 00	// augmentation id
 * ff ff ff ff	// shadow weapon time remaining
 * <p/>
 * format   h (hh dddhhhd hh dd) revision 740
 * format   h (hhdddQhhhdhhhhdddddddddd) Gracia Final
 */
public class InventoryUpdate extends L2GameServerPacket
{
	private final List<ItemInfo> _items = Collections.synchronizedList(new Vector<ItemInfo>());

	public InventoryUpdate()
	{
	}

	public InventoryUpdate(GArray<L2ItemInstance> items)
	{
		for(L2ItemInstance item : items)
		{
			_items.add(new ItemInfo(item));
		}
	}

	public InventoryUpdate addNewItem(L2ItemInstance item)
	{
		item.setLastChange(L2ItemInstance.ADDED);
		_items.add(new ItemInfo(item));
		return this;
	}

	public InventoryUpdate addModifiedItem(L2ItemInstance item)
	{
		item.setLastChange(L2ItemInstance.MODIFIED);
		_items.add(new ItemInfo(item));
		return this;
	}

	public InventoryUpdate addRemovedItem(L2ItemInstance item)
	{
		item.setLastChange(L2ItemInstance.REMOVED);
		_items.add(new ItemInfo(item));
		return this;
	}

	public InventoryUpdate addItem(L2ItemInstance item)
	{
		if(item == null)
		{
			return null;
		}
		switch(item.getLastChange())
		{
			case L2ItemInstance.ADDED:
			{
				addNewItem(item);
				break;
			}
			case L2ItemInstance.MODIFIED:
			{
				addModifiedItem(item);
				break;
			}
			case L2ItemInstance.REMOVED:
			{
				addRemovedItem(item);
			}
		}
		return this;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x21);
		writeH(_items.size());
		for(ItemInfo temp : _items)
		{
			writeH(temp.getLastChange());
			writeH(temp.getType1()); // item type1
			writeD(temp.getObjectId());
			writeD(temp.getItemId());
			writeD(temp.getEquipSlot()); //order
			writeQ(temp.getCount());
			writeH(temp.getType2()); // item type2
			writeH(temp.getCustomType1());
			writeH(temp.isEquipped() ? 1 : 0);
			writeD(temp.getBodyPart()); // rev 415   slot    0006-lr.ear  0008-neck  0030-lr.finger  0040-head  0080-??  0100-l.hand  0200-gloves  0400-chest  0800-pants  1000-feet  2000-??  4000-r.hand  8000-r.hand
			writeH(temp.getEnchantLevel()); // enchant level or pet level
			writeH(temp.getCustomType2()); // Pet name exists or not shown in control item
			writeD(temp.getAugmentationId());
			writeD(temp.getShadowLifeTime()); //interlude FF FF FF FF
			writeH(temp.getAttackElement()[0]);
			writeH(temp.getAttackElement()[1]);
			writeH(temp.getDefenceFire());
			writeH(temp.getDefenceWater());
			writeH(temp.getDefenceWind());
			writeH(temp.getDefenceEarth());
			writeH(temp.getDefenceHoly());
			writeH(temp.getDefenceUnholy());
			writeD(temp.getTemporalLifeTime()); // limited time item life remaining
			if(getClient().getRevision() >= 152) //FIXME
			{
				writeH(0);
				writeH(0);
				writeH(0);
			}
		}
	}

	private class ItemInfo
	{
		private final short lastChange;
		private final short type1;
		private final int objectId;
		private final int itemId;
		private final long long_count;
		private final short type2;
		private final short customType1;
		private final boolean isEquipped;
		private final int bodyPart;
		private final short enchantLevel;
		private final short customType2;
		private final int augmentationId;
		private final int shadowLifeTime;
		private final int[] attackElement;
		private final int defenceFire;
		private final int defenceWater;
		private final int defenceWind;
		private final int defenceEarth;
		private final int defenceHoly;
		private final int defenceUnholy;
		private final int equipSlot;
		private final int temporalLifeTime;

		private ItemInfo(L2ItemInstance item)
		{
			lastChange = (short) item.getLastChange();
			type1 = (short) item.getItem().getType1();
			objectId = item.getObjectId();
			itemId = item.getItemId();
			long_count = item.getCount();
			type2 = (short) item.getItem().getType2ForPackets();
			customType1 = (short) item.getCustomType1();
			isEquipped = item.isEquipped();
			bodyPart = item.getItem().getBodyPart();
			enchantLevel = (short) item.getEnchantLevel();
			customType2 = (short) item.getCustomType2();
			augmentationId = item.getAugmentationId();
			shadowLifeTime = item.isShadowItem() ? item.getLifeTimeRemaining() : -1;
			attackElement = item.getAttackElement();
			defenceFire = item.getDefenceFire();
			defenceWater = item.getDefenceWater();
			defenceWind = item.getDefenceWind();
			defenceEarth = item.getDefenceEarth();
			defenceHoly = item.getDefenceHoly();
			defenceUnholy = item.getDefenceUnholy();
			equipSlot = item.getEquipSlot();
			temporalLifeTime = item.isTemporalItem() ? item.getLifeTimeRemaining() : 0x00;
		}

		public short getLastChange()
		{
			return lastChange;
		}

		public short getType1()
		{
			return type1;
		}

		public int getObjectId()
		{
			return objectId;
		}

		public int getItemId()
		{
			return itemId;
		}

		public long getCount()
		{
			return long_count;
		}

		public short getType2()
		{
			return type2;
		}

		public short getCustomType1()
		{
			return customType1;
		}

		public boolean isEquipped()
		{
			return isEquipped;
		}

		public int getBodyPart()
		{
			return bodyPart;
		}

		public short getEnchantLevel()
		{
			return enchantLevel;
		}

		public int getAugmentationId()
		{
			return augmentationId;
		}

		public int getShadowLifeTime()
		{
			return shadowLifeTime;
		}

		public short getCustomType2()
		{
			return customType2;
		}

		public int[] getAttackElement()
		{
			return attackElement;
		}

		public int getDefenceFire()
		{
			return defenceFire;
		}

		public int getDefenceWater()
		{
			return defenceWater;
		}

		public int getDefenceWind()
		{
			return defenceWind;
		}

		public int getDefenceEarth()
		{
			return defenceEarth;
		}

		public int getDefenceHoly()
		{
			return defenceHoly;
		}

		public int getDefenceUnholy()
		{
			return defenceUnholy;
		}

		public int getEquipSlot()
		{
			return equipSlot;
		}

		public int getTemporalLifeTime()
		{
			return temporalLifeTime;
		}
	}
}