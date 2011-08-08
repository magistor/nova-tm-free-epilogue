package l2p.gameserver.modules.option;

import l2p.Server;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.tables.ItemTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: Shaitan
 * Date: 02.11.2010
 * Time: 10:47:43
 */
public class mOption
{
	public static Properties loadFile(String fileOption)
	{
		Properties properties = new Properties();
		try
		{
			InputStream inputStream = new FileInputStream(new File(fileOption));
			properties.load(inputStream);
		}
		catch(Exception e)
		{
			Server.exit(0, "File " + fileOption + " not found");
		}
		return properties;
	}

	public static int getInt(Properties Option, String s)
	{
		return Integer.parseInt(Option.getProperty(s.trim()));
	}

	public static boolean getBoolean(Properties Option, String s)
	{
		return Boolean.parseBoolean(Option.getProperty(s.trim()));
	}

	public static String getString(Properties Option, String s)
	{
		return Option.getProperty(s.trim());
	}

	public static boolean price(L2Player player, int itemid, int count)
	{
		if(player.getInventory().getItemByItemId(itemid) == null)
		{
			player.sendMessage("Цена: " + count + " " + getItemName(itemid));
			player.sendMessage("У вас вообще нету " + getItemName(itemid));
			return false;
		}
		else if(getItemCount(player, itemid) < count)
		{
			player.sendMessage("Цена: " + count + " " + getItemName(itemid));
			player.sendMessage("У вас нету " + count + " " + getItemName(itemid));
			return false;
		}
		else
		{
			player.getInventory().destroyItemByItemId(itemid, count, true);
			return true;
		}
	}

	public static String getItemName(int ItemId)
	{
		return ItemTable.getInstance().getTemplate(ItemId).getName();
	}

	public static long getItemCount(L2Player player, int ItemId)
	{
		return player.getInventory().getItemByItemId(ItemId).getCount();
	}
}