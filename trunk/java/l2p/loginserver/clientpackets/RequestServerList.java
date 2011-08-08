package l2p.loginserver.clientpackets;

import l2p.loginserver.serverpackets.LoginFail.LoginFailReason;
import l2p.loginserver.serverpackets.ServerList;

public class RequestServerList extends L2LoginClientPacket
{
	private int _skey1;
	private int _skey2;

	public int getSessionKey1()
	{
		return _skey1;
	}

	public int getSessionKey2()
	{
		return _skey2;
	}

	@Override
	public boolean readImpl()
	{
		if(getAvaliableBytes() >= 8)
		{
			_skey1 = readD();
			_skey2 = readD();
			return true;
		}
		return false;
	}

	@Override
	public void runImpl()
	{
		if(getClient().getSessionKey().checkLoginPair(_skey1, _skey2))
		{
			getClient().sendPacket(new ServerList(getClient(), false));
		}
		else
		{
			getClient().close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}