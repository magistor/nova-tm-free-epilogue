package community.statistic;

import community.Community;
import community.StaticPage;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;

/**
 * User: Shaitan
 * Date: 28.12.10
 * Time: 12:18
 */
public class Statistic implements mICommunityHandler
{
	public void onLoad()
	{
		if(!Community.communityStatistic)
		{
			return;
		}
		mCommunityHandler.getInstance().addHandler(this);
	}

	public void useHandler(int objectId, String command)
	{
		if(command.equalsIgnoreCase("_bbsstatistic"))
		{
			Community.getInstance().show(objectId, StaticPage.pageStatistic);
		}
		else if(command.equalsIgnoreCase("_bbsstatistic_pvp"))
		{
			Community.getInstance().show(objectId, StaticPage.pageStatisticPvP);
		}
		else if(command.equalsIgnoreCase("_bbsstatistic_pk"))
		{
			Community.getInstance().show(objectId, StaticPage.pageStatisticPK);
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbsstatistic",
				"_bbsstatistic_pvp",
				"_bbsstatistic_pk"
			};
		return s;
	}
}