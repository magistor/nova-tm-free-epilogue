package l2p.loginserver.clientpackets;

import l2p.loginserver.LoginController;
import l2p.loginserver.SessionKey;
import l2p.loginserver.serverpackets.LoginFail.LoginFailReason;
import l2p.loginserver.serverpackets.PlayOk;

public class RequestServerLogin extends L2LoginClientPacket
{
	private int _skey1;
	private int _skey2;
	private int _serverId;

	public int getSessionKey1()
	{
		return _skey1;
	}

	public int getSessionKey2()
	{
		return _skey2;
	}

	public int getServerID()
	{
		return _serverId;
	}

	@Override
	public boolean readImpl()
	{
		if(getAvaliableBytes() >= 9)
		{
			_skey1 = readD();
			_skey2 = readD();
			_serverId = readC();
			return true;
		}
		return false;
	}

	@Override
	public void runImpl()
	{
		SessionKey sk = getClient().getSessionKey();
		if(sk.checkLoginPair(_skey1, _skey2))
		{
			if(LoginController.getInstance().isLoginPossible(getClient(), _serverId))
			{
				getClient().sendPacket(new PlayOk(sk));
			}
			else
			{
				getClient().close(LoginFailReason.REASON_ACCESS_FAILED);
			}
		}
		else
		{
			getClient().close(LoginFailReason.REASON_ACCESS_FAILED);
		}
	}
}