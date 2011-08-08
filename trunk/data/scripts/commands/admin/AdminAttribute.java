package commands.admin;

import javolution.text.TextBuilder;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.handler.AdminCommandHandler;
import l2p.gameserver.handler.IAdminCommandHandler;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.InventoryUpdate;
import l2p.gameserver.serverpackets.NpcHtmlMessage;
import l2p.util.Log;

public class AdminAttribute implements IAdminCommandHandler, ScriptFile
{
	private static enum Commands
	{
		admin_setatreh, // 6
		admin_setatrec, // 10
		admin_setatreg, // 9
		admin_setatrel, // 11
		admin_setatreb, // 12
		admin_setatrew, // 7
		admin_setatres, // 8
		admin_setatrle, // 1
		admin_setatrre, // 2
		admin_setatrlf, // 4
		admin_setatrrf, // 5
		admin_setatren, // 3
		admin_setatrun, // 0
		admin_setatrbl, // 24
		admin_attribute
	}

	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, L2Player activeChar)
	{
		Commands command = (Commands) comm;

		if(!activeChar.getPlayerAccess().CanEditChar)
			return false;

		int armorType = -1;

		switch(command)
		{
			case admin_attribute:
				showMainPage(activeChar);
				return true;
			case admin_setatreh:
				armorType = Inventory.PAPERDOLL_HEAD;
				break;
			case admin_setatrec:
				armorType = Inventory.PAPERDOLL_CHEST;
				break;
			case admin_setatreg:
				armorType = Inventory.PAPERDOLL_GLOVES;
				break;
			case admin_setatreb:
				armorType = Inventory.PAPERDOLL_FEET;
				break;
			case admin_setatrel:
				armorType = Inventory.PAPERDOLL_LEGS;
				break;
			case admin_setatrew:
				armorType = Inventory.PAPERDOLL_RHAND;
				break;
			case admin_setatres:
				armorType = Inventory.PAPERDOLL_LHAND;
				break;
			case admin_setatrle:
				armorType = Inventory.PAPERDOLL_LEAR;
				break;
			case admin_setatrre:
				armorType = Inventory.PAPERDOLL_REAR;
				break;
			case admin_setatrlf:
				armorType = Inventory.PAPERDOLL_LFINGER;
				break;
			case admin_setatrrf:
				armorType = Inventory.PAPERDOLL_RFINGER;
				break;
			case admin_setatren:
				armorType = Inventory.PAPERDOLL_NECK;
				break;
			case admin_setatrun:
				armorType = Inventory.PAPERDOLL_UNDER;
				break;
			case admin_setatrbl:
				armorType = Inventory.PAPERDOLL_BELT;
				break;
		}

		if(armorType == -1 || wordList.length < 2 || activeChar.getInventory().getPaperdollItem(armorType)==null)
		{
			showMainPage(activeChar);
			return true;
		}

		try
		{
			
			int ench = Integer.parseInt(wordList[1]);
			byte element = -2;
			
            if (wordList[2].equals("Fire")) element=0;
            if (wordList[2].equals("Water")) element=1;
            if (wordList[2].equals("Wind")) element=2;
            if (wordList[2].equals("Earth")) element=3;
            if (wordList[2].equals("Holy")) element=4;
            if (wordList[2].equals("Dark")) element=5;
       
			
					if (ench < 0 || ench > 450)
					activeChar.sendMessage("You must set the enchant level for ARMOR to be between 0-300.");
					else
					setEnchant(activeChar, ench, element, armorType);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			activeChar.sendMessage("Please specify a new enchant value.");
		}
		catch(NumberFormatException e)
		{
			activeChar.sendMessage("Please specify a valid new enchant value.");
		}

		// show the enchant menu after an action
		showMainPage(activeChar);
		return true;
	}

	private void setEnchant(L2Player activeChar, int value, byte element , int armorType)
	{
		L2Object target = activeChar.getTarget();
		if(target == null)
			target = activeChar;
		if(!target.isPlayer())
		{
			activeChar.sendMessage("Wrong target type.");
			return;
		}

		L2Player player = (L2Player) target;

		L2ItemInstance item = player.getInventory().getPaperdollItem(armorType);
		if(item != null)
		{
			item.setAttributeElement(element, value, true);
			player.getInventory().refreshListeners();
			player.sendPacket(new InventoryUpdate().addModifiedItem(item));
			player.sendChanges();
		}
	}
	private void showMainPage(L2Player activeChar)
	{
		activeChar.sendPacket(new NpcHtmlMessage(5).setFile("data/html/admin/attribute.htm"));
	}

	public Enum[] getAdminCommandEnum()
	{
		return Commands.values();
	}

	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	public void onReload()
	{}

	public void onShutdown()
	{}
}