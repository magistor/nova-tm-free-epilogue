package l2p.gameserver.clientpackets;

import l2p.gameserver.modules.community.mCommunity;
import l2p.gameserver.modules.community.mICommunity;

public class RequestShowBoard extends L2GameClientPacket
{
	@Override
	public void readImpl()
	{
		readD();
	}

	@Override
	public void runImpl()
	{
		mICommunity mICommunity = mCommunity.getInstance().get();
		if(mICommunity != null)
		{
			mICommunity.show(getClient().getActiveChar().getObjectId(), "");
		}
	}
}