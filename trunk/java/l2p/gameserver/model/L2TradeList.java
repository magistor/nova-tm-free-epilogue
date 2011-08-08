package l2p.gameserver.model;

import l2p.Config;
import l2p.extensions.Stat;
import l2p.extensions.multilang.CustomMessage;
import l2p.gameserver.model.base.Transaction;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.tables.ItemTable;
import l2p.util.GArray;
import l2p.util.Log;
import l2p.util.SafeMath;
import l2p.util.Strings;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Контейнер для приватных магазинов покупки и продажи.
 *
 * @see Transaction
 * @see L2ManufactureList
 */
public class L2TradeList
{
	private final GArray<L2ItemInstance> _items = new GArray<L2ItemInstance>();
	private int _listId;
	private String _buyStoreName, _sellStoreName;

	public L2TradeList(int listId)
	{
		_listId = listId;
	}

	public L2TradeList()
	{
		this(0);
	}

	public void addItem(L2ItemInstance item)
	{
		synchronized(_items)
		{
			_items.add(item);
		}
	}

	public void removeAll()
	{
		_items.clear();
	}

	/**
	 * @return Returns the listId.
	 */
	public int getListId()
	{
		return _listId;
	}

	public void setSellStoreName(String name)
	{
		_sellStoreName = Strings.stripToSingleLine(name);
	}

	public String getSellStoreName()
	{
		return _sellStoreName;
	}

	public void setBuyStoreName(String name)
	{
		_buyStoreName = Strings.stripToSingleLine(name);
	}

	public String getBuyStoreName()
	{
		return _buyStoreName;
	}

	/**
	 * @return Returns the items.
	 */
	public GArray<L2ItemInstance> getItems()
	{
		return _items;
	}

	/**
	 * Проверяет, есть ли у игрока player в наличии необходимое количество вещей
	 */
	public static boolean validateTrade(L2Player player, Collection<TradeItem> items)
	{
		Inventory playersInv = player.getInventory();
		L2ItemInstance playerItem;
		synchronized(items)
		{
			for(TradeItem item : items)
			{
				playerItem = playersInv.getItemByObjectId(item.getObjectId());
				if(playerItem == null || playerItem.getCount() < item.getCount() || playerItem.getEnchantLevel() < item.getEnchantLevel() || !playerItem.canBeTraded(player))
				{
					return false;
				}
			}
			// TODO: добавить проверку на перевес после трейда с учетом отданного
			//if(!playersInv.validateCapacity(_items) || !playersInv.validateWeight(_items)) 
			//      return false; 
		}
		return true;
	}

	public void updateSellList(L2Player player, ConcurrentLinkedQueue<TradeItem> list)
	{
		Inventory playersInv = player.getInventory();
		L2ItemInstance item;
		for(L2ItemInstance temp : _items)
		{
			item = playersInv.getItemByObjectId(temp.getObjectId());
			if(item == null || item.getCount() <= 0)
			{
				for(TradeItem i : list)
				{
					if(i.getObjectId() == temp.getItemId())
					{
						list.remove(i);
						break;
					}
				}
			}
			else if(item.getCount() < temp.getCount())
			{
				temp.setCount(item.getCount());
			}
		}
	}

	public synchronized boolean buySellItems(L2Player buyer, ConcurrentLinkedQueue<TradeItem> listToBuy, L2Player seller, ConcurrentLinkedQueue<TradeItem> listToSell)
	{
		Inventory sellerInv = seller.getInventory();
		Inventory buyerInv = buyer.getInventory();
		TradeItem sellerTradeItem = null;
		L2ItemInstance sellerInventoryItem = null;
		L2ItemInstance TransferItem = null;
		L2ItemInstance temp;
		ConcurrentLinkedQueue<TradeItem> unsold = new ConcurrentLinkedQueue<TradeItem>();
		unsold.addAll(listToSell);
		long cost = 0, amount = 0;
		for(TradeItem buyerTradeItem : listToBuy)
		{
			sellerTradeItem = null;
			for(TradeItem unsoldItem : unsold)
			{
				if(unsoldItem.getItemId() == buyerTradeItem.getItemId() && unsoldItem.getOwnersPrice() == buyerTradeItem.getOwnersPrice())
				{
					sellerTradeItem = unsoldItem;
					break;
				}
			}
			if(sellerTradeItem == null)
			{
				continue;
			}
			sellerInventoryItem = sellerInv.getItemByObjectId(sellerTradeItem.getObjectId());
			unsold.remove(sellerTradeItem);
			if(sellerInventoryItem == null)
			{
				continue;
			}
			long buyerItemCount = buyerTradeItem.getCount();
			long sellerItemCount = sellerTradeItem.getCount();
			if(sellerItemCount > sellerInventoryItem.getCount())
			{
				sellerItemCount = sellerInventoryItem.getCount();
			}
			if(seller.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL || seller.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL_PACKAGE)
			{
				if(buyerItemCount > sellerItemCount)
				{
					buyerTradeItem.setCount(sellerItemCount);
				}
				if(buyerItemCount > sellerInventoryItem.getCount())
				{
					buyerTradeItem.setCount(sellerInventoryItem.getCount());
				}
				buyerItemCount = buyerTradeItem.getCount();
				amount = buyerItemCount;
				cost = amount * sellerTradeItem.getOwnersPrice();
			}
			if(buyer.getPrivateStoreType() == L2Player.STORE_PRIVATE_BUY)
			{
				if(sellerItemCount > buyerItemCount)
				{
					sellerTradeItem.setCount(buyerItemCount);
				}
				if(sellerItemCount > sellerInventoryItem.getCount())
				{
					sellerTradeItem.setCount(sellerInventoryItem.getCount());
				}
				sellerItemCount = sellerTradeItem.getCount();
				amount = sellerItemCount;
				cost = amount * buyerTradeItem.getOwnersPrice();
			}
			if(buyer.getAdena() < cost)
			{
				return false;
			}
			try
			{
				SafeMath.safeMulLong(buyerItemCount, buyerTradeItem.getOwnersPrice());
				SafeMath.safeMulLong(sellerItemCount, sellerTradeItem.getOwnersPrice());
			}
			catch(ArithmeticException e)
			{
				return false;
			}
			TransferItem = sellerInv.dropItem(sellerInventoryItem.getObjectId(), amount, false);
			Log.LogItem(seller, buyer, Log.PrivateStoreSell, TransferItem, amount);
			buyer.reduceAdena(cost, false);
			seller.addAdena(cost);
			int tax = (int) (cost * Config.SERVICES_TRADE_TAX / 100);
			if(seller.isInZone(L2Zone.ZoneType.offshore))
			{
				tax = (int) (cost * Config.SERVICES_OFFSHORE_TRADE_TAX / 100);
			}
			if(Config.SERVICES_TRADE_TAX_ONLY_OFFLINE && !seller.isInOfflineMode())
			{
				tax = 0;
			}
			if(seller.getReflection().getId() == -1) // Особая зона в Parnassus
			{
				tax = 0;
			}
			if(tax > 0)
			{
				seller.reduceAdena(tax, true);
				Stat.addTax(tax);
				seller.sendMessage(new CustomMessage("trade.HavePaidTax", seller).addNumber(tax));
			}
			temp = buyerInv.addItem(TransferItem);
			Log.LogItem(buyer, seller, Log.PrivateStoreBuy, TransferItem, amount);
			if(!temp.isStackable())
			{
				if(temp.getEnchantLevel() > 0)
				{
					seller.sendPacket(new SystemMessage(SystemMessage._S2S3_HAS_BEEN_SOLD_TO_S1_AT_THE_PRICE_OF_S4_ADENA).addString(buyer.getName()).addNumber(temp.getEnchantLevel()).addItemName(sellerInventoryItem.getItemId()).addNumber(cost));
					buyer.sendPacket(new SystemMessage(SystemMessage._S2S3_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S4_ADENA).addString(seller.getName()).addNumber(temp.getEnchantLevel()).addItemName(sellerInventoryItem.getItemId()).addNumber(cost));
				}
				else
				{
					seller.sendPacket(new SystemMessage(SystemMessage.S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA).addString(buyer.getName()).addItemName(sellerInventoryItem.getItemId()).addNumber(cost));
					buyer.sendPacket(new SystemMessage(SystemMessage.S2_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S3_ADENA).addString(seller.getName()).addItemName(sellerInventoryItem.getItemId()).addNumber(cost));
				}
			}
			else
			{
				seller.sendPacket(new SystemMessage(SystemMessage.S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA).addString(buyer.getName()).addItemName(sellerInventoryItem.getItemId()).addNumber(amount).addNumber(cost));
				buyer.sendPacket(new SystemMessage(SystemMessage.S3_S2_HAS_BEEN_PURCHASED_FROM_S1_FOR_S4_ADENA).addString(seller.getName()).addItemName(sellerInventoryItem.getItemId()).addNumber(amount).addNumber(cost));
			}
			sellerInventoryItem = null;
		}
		seller.sendChanges();
		buyer.sendChanges();
		updateSellerSellList(listToBuy, seller, listToSell);
		updateBuyerBuyList(buyer, listToBuy, listToSell);
		return true;
	}

	private void updateBuyerBuyList(L2Player buyer, ConcurrentLinkedQueue<TradeItem> listToBuy, ConcurrentLinkedQueue<TradeItem> listToSell)
	{
		GArray<TradeItem> tmp;
		if(buyer.getPrivateStoreType() == L2Player.STORE_PRIVATE_BUY)
		{
			tmp = new GArray<TradeItem>();
			tmp.addAll(buyer.getBuyList());
outer:
			for(TradeItem bl : listToBuy)
			{
				for(TradeItem sl : listToSell)
				{
					if(sl.getItemId() == bl.getItemId() && sl.getOwnersPrice() == bl.getOwnersPrice())
					{
						if(!ItemTable.getInstance().getTemplate(bl.getItemId()).isStackable())
						{
							tmp.remove(bl);
						}
						else
						{
							bl.setCount(bl.getCount() - sl.getCount());
							if(bl.getCount() <= 0)
							{
								tmp.remove(bl);
							}
						}
						listToSell.remove(sl);
						continue outer;
					}
				}
			}
			ConcurrentLinkedQueue<TradeItem> newlist = new ConcurrentLinkedQueue<TradeItem>();
			newlist.addAll(tmp);
			buyer.setBuyList(newlist);
		}
	}

	private void updateSellerSellList(ConcurrentLinkedQueue<TradeItem> listToBuy, L2Player seller, ConcurrentLinkedQueue<TradeItem> listToSell)
	{
		if(seller.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL_PACKAGE)
		{
			seller.setSellList(null);
			return;
		}
		GArray<TradeItem> tmp;
		if(seller.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL)
		{
			tmp = new GArray<TradeItem>();
			tmp.addAll(seller.getSellList());
			for(TradeItem sl : listToSell)
			{
				for(TradeItem bl : listToBuy)
				{
					if(sl.getItemId() == bl.getItemId() && sl.getOwnersPrice() == bl.getOwnersPrice())
					{
						L2ItemInstance inst = seller.getInventory().getItemByObjectId(sl.getObjectId());
						if(inst == null || inst.getCount() <= 0)
						{
							tmp.remove(sl);
							continue;
						}
						if(inst.isStackable())
						{
							sl.setCount(sl.getCount() - bl.getCount());
							if(sl.getCount() <= 0)
							{
								tmp.remove(sl);
								continue;
							}
							if(inst.getCount() < sl.getCount())
							{
								sl.setCount(inst.getCount());
							}
						}
					}
				}
			}
			ConcurrentLinkedQueue<TradeItem> newlist = new ConcurrentLinkedQueue<TradeItem>();
			newlist.addAll(tmp);
			seller.setSellList(newlist);
		}
	}

	/**
	 * Проверяет наличие всех продаваемых предметов для листа продажи и наличия нужной денежной суммы для листа покупки.
	 */
	public static boolean validateList(L2Player player)
	{
		if(player.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL || player.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL_PACKAGE)
		{
			ConcurrentLinkedQueue<TradeItem> selllist = player.getSellList();
			for(TradeItem tl : selllist)
			{
				L2ItemInstance inst = player.getInventory().getItemByObjectId(tl.getObjectId());
				if(inst == null || inst.getCount() <= 0)
				{
					if(player.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL_PACKAGE)
					{
						cancelStore(player);
						return false;
					}
					selllist.remove(tl);
					continue;
				}
				if(inst.isStackable() && inst.getCount() < tl.getCount())
				{
					if(player.getPrivateStoreType() == L2Player.STORE_PRIVATE_SELL_PACKAGE)
					{
						cancelStore(player);
						return false;
					}
					tl.setCount(inst.getCount());
				}
			}
		}
		else if(player.getPrivateStoreType() == L2Player.STORE_PRIVATE_BUY)
		{
			ConcurrentLinkedQueue<TradeItem> buylist = player.getBuyList();
			long sum = 0;
			for(TradeItem tl : buylist)
			{
				sum += tl.getOwnersPrice() * tl.getCount();
			}
			if(player.getAdena() < sum)
			{
				cancelStore(player);
				return false;
			}
		}
		return true;
	}

	/**
	 * Ссаживает чара с трейда. Оффлайн трейдеров кикает.
	 */
	public static void cancelStore(L2Player activeChar)
	{
		activeChar.setPrivateStoreType(L2Player.STORE_PRIVATE_NONE);
		activeChar.getBuyList().clear();
		if(activeChar.isInOfflineMode() && Config.SERVICES_OFFLINE_TRADE_KICK_NOT_TRADING)
		{
			activeChar.setOfflineMode(false);
			activeChar.logout(false, false, false, true);
			if(activeChar.getNetConnection() != null)
			{
				activeChar.getNetConnection().disconnectOffline();
			}
		}
		else
		{
			activeChar.broadcastUserInfo(true);
		}
	}
}