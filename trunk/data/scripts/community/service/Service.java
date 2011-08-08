package community.service;

import community.Community;
import community.StaticPage;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;

/**
 * User: Shaitan
 * Date: 07.11.2010
 * Time: 10:31:37
 */
public class Service implements mICommunityHandler
{
	public void onLoad()
	{
		if(!Community.communityService)
		{
			return;
		}
		mCommunityHandler.getInstance().addHandler(this);
	}

	public void useHandler(int objectId, String command)
	{
		if(command.equals("_bbsservice"))
		{
			Community.getInstance().show(objectId, StaticPage.pageService);
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbsservice",
			};
		return s;
	}
}