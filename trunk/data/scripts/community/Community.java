package community;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.modules.community.mCommunity;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunity;
import l2p.gameserver.modules.community.mICommunityHandler;
import l2p.gameserver.modules.option.mOption;
import l2p.gameserver.serverpackets.ShowBoard;

import java.util.Properties;

/**
 * User: Shaitan
 * Date: 01.11.2010
 * Time: 7:37:56
 */
public class Community implements mICommunity, mICommunityHandler
{
	private static Community Community = new Community();

	public static Community getInstance()
	{
		return Community;
	}

	private static boolean communityEnabled = false;
	public static boolean communityShop = false;
	public static boolean communityBuffer = false;
	public static int communityBuffTimeModifier;
	public static boolean communityService = false;
	public static boolean communityStatistic = false;
	public static boolean communityCareer = false;
	public static boolean communityTeleport = false;

	public void onLoad()
	{
		mCommunity.getInstance().set(this);
		mCommunityHandler.getInstance().addHandler(this);
		Properties community = mOption.loadFile("./config/custom/community.ini");
		communityEnabled = mOption.getBoolean(community, "communityEnabled");
		if(!communityEnabled)
		{
			return;
		}
		communityShop = mOption.getBoolean(community, "communityShop");
		communityBuffer = mOption.getBoolean(community, "communityBuffer");
		communityBuffTimeModifier = mOption.getInt(community, "communityBuffTimeModifier");
		communityService = mOption.getBoolean(community, "communityService");
		communityStatistic = mOption.getBoolean(community, "communityStatistic");
		communityCareer = mOption.getBoolean(community, "communityCareer");
		communityTeleport = mOption.getBoolean(community, "communityTeleport");
		GeneratePage.getInstance();
	}

	public void show(int objectId, String s)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(!communityEnabled)
		{
			player.sendPacket(Msg.THE_COMMUNITY_SERVER_IS_CURRENTLY_OFFLINE);
			return;
		}
		player.sendPacket(new ShowBoard(s));
	}

	public void useHandler(int objectId, String command)
	{
		if(command.equals("_bbshome"))
		{
			show(objectId, StaticPage.pageMain);
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbshome"
			};
		return s;
	}
}