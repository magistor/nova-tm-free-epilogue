package community.shop;

import community.Community;
import community.StaticPage;
import l2p.Config;
import l2p.gameserver.model.L2Multisell;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;

import java.io.File;
import java.util.StringTokenizer;

/**
 * User: Shaitan
 * Date: 01.11.2010
 * Time: 15:06:13
 */
public class Shop implements mICommunityHandler
{
	int[] multisell;

	public void onLoad()
	{
		if(!Community.communityShop)
		{
			return;
		}
		mCommunityHandler.getInstance().addHandler(this);
		File dir = new File(Config.DATAPACK_ROOT, "./custom/multisell");
		multisell = new int[dir.list().length];
		int i = 0;
		for(File f : dir.listFiles())
		{
			multisell[i] = Integer.parseInt(f.getName().replaceAll(".xml", ""));
			i++;
		}
	}

	public void useHandler(int objectId, String command)
	{
		if(command.equalsIgnoreCase("_bbsshop"))
		{
			Community.getInstance().show(objectId, StaticPage.pageShop);
		}
		else if(command.startsWith("_bbsshop_multisell"))
		{
			L2Player player = L2ObjectsStorage.getPlayer(objectId);
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			for(int i = 0; i < multisell.length; i++)
			{
				if(id == multisell[i])
				{
					L2Multisell.getInstance().SeparateAndSend(id, player, 0);
					break;
				}
			}
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbsshop",
				"_bbsshop_multisell"
			};
		return s;
	}
}