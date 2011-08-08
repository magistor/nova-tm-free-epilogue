package l2p.gameserver.handler;

import l2p.extensions.multilang.CustomMessage;
import l2p.gameserver.model.L2Player;
import l2p.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class AdminCommandHandler
{
	private static Logger _log = Logger.getLogger(AdminCommandHandler.class.getName());
	private static AdminCommandHandler _instance;
	private Map<String, IAdminCommandHandler> _datatable;

	public static AdminCommandHandler getInstance()
	{
		if(_instance == null)
		{
			_instance = new AdminCommandHandler();
		}
		return _instance;
	}

	private AdminCommandHandler()
	{
		_datatable = new HashMap<String, IAdminCommandHandler>();
	}

	public void registerAdminCommandHandler(IAdminCommandHandler handler)
	{
		for(Enum<?> e : handler.getAdminCommandEnum())
		{
			_datatable.put(e.toString().toLowerCase(), handler);
		}
	}

	public IAdminCommandHandler getAdminCommandHandler(String adminCommand)
	{
		String command = adminCommand;
		if(adminCommand.indexOf(" ") != -1)
		{
			command = adminCommand.substring(0, adminCommand.indexOf(" "));
		}
		return _datatable.get(command);
	}

	public void useAdminCommandHandler(L2Player activeChar, String adminCommand)
	{
		if(!(activeChar.isGM() || activeChar.getPlayerAccess().CanUseGMCommand))
		{
			activeChar.sendMessage(new CustomMessage("l2p.gameserver.clientpackets.SendBypassBuildCmd.NoCommandOrAccess", activeChar).addString(adminCommand));
			//activeChar.illegalAction(activeChar.getName() + " user use adm command " + adminCommand, 200);
			return;
		}
		String[] wordList = adminCommand.split(" ");
		IAdminCommandHandler handler = _datatable.get(wordList[0]);
		if(handler != null)
		{
			int command_success = 0;
			try
			{
				for(Enum<?> e : handler.getAdminCommandEnum())
				{
					if(e.toString().equalsIgnoreCase(wordList[0]))
					{
						command_success = handler.useAdminCommand(e, wordList, adminCommand, activeChar) ? 1 : 0;
						break;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			Log.LogCommand(activeChar, Log.CMD_ADMH, adminCommand, command_success);
		}
		else
		{
			_log.warning("No handler for admin command " + wordList[0]);
		}
	}

	public int size()
	{
		return _datatable.size();
	}

	public void clear()
	{
		_datatable.clear();
	}

	public Set<String> getAllCommands()
	{
		return _datatable.keySet();
	}
}