package l2p.gameserver.modules.community;

import java.util.HashMap;

/**
 * User: Shaitan
 * Date: 01.11.2010
 * Time: 18:47:05
 */
public class mCommunityHandler
{
	private static mCommunityHandler mCommunityHandler = new mCommunityHandler();

	public static mCommunityHandler getInstance()
	{
		return mCommunityHandler;
	}

	HashMap<Integer, mICommunityHandler> handlers = new HashMap<Integer, mICommunityHandler>();

	public void addHandler(mICommunityHandler mICommunityHandler)
	{
		for(String command : mICommunityHandler.getHandlerList())
		{
			String temp = command;
			if(command.indexOf(" ") != -1)
			{
				temp = command.substring(0, command.indexOf(" "));
			}
			handlers.put(temp.toLowerCase().hashCode(), mICommunityHandler);
		}
	}

	public mICommunityHandler getHandler(String command)
	{
		String bbs_command = command;
		if(command.indexOf(" ") != -1)
		{
			bbs_command = command.substring(0, command.indexOf(" "));
		}
		return handlers.get(bbs_command.toLowerCase().hashCode());
	}
}