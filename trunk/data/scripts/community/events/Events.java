package community.statistic;

import community.Community;
import community.StaticPage;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;

public class Events implements mICommunityHandler
{
        public void onLoad()
        {
                mCommunityHandler.getInstance().addHandler(this);
        }

        public void useHandler(int objectId, String command)
        {
                if(command.equalsIgnoreCase("_bbsevents"))
                {
                        Community.getInstance().show(objectId, StaticPage.pageEvents);
                }
        }

        public String[] getHandlerList()
        {
                String[] s =
                {
                        "_bbsevents",
                };
                return s;
        }
}