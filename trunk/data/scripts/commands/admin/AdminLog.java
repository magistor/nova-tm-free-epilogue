package commands.admin;

import l2p.debug.HeapDumper;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.handler.AdminCommandHandler;
import l2p.gameserver.handler.IAdminCommandHandler;
import l2p.gameserver.model.L2Player;

/**
 * User: Shaitan
 * Date: 25.12.10
 * Time: 14:46
 */
public class AdminLog implements IAdminCommandHandler, ScriptFile
{
	private static enum Commands
	{
		admin_dumpheap
	}

	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, L2Player activeChar)
	{
		Commands command = (Commands) comm;
		if(!activeChar.getPlayerAccess().IsGM)
		{
			return false;
		}
		switch(command)
		{
			case admin_dumpheap:
				HeapDumper.dumpHeap("./snapshot", false);
				break;
		}
		return true;
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
	{
	}

	public void onShutdown()
	{
	}
}