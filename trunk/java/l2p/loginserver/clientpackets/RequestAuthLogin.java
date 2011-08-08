package l2p.loginserver.clientpackets;

import l2p.Config;
import l2p.loginserver.L2LoginClient;
import l2p.loginserver.L2LoginClient.LoginClientState;
import l2p.loginserver.LoginController;
import l2p.loginserver.LoginController.State;
import l2p.loginserver.LoginController.Status;
import l2p.loginserver.serverpackets.LoginFail;
import l2p.loginserver.serverpackets.LoginFail.LoginFailReason;
import l2p.loginserver.serverpackets.LoginOk;
import l2p.loginserver.serverpackets.ServerList;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

public class RequestAuthLogin extends L2LoginClientPacket
{
	private byte[] _raw = new byte[128];
	private String _user;
	private String _password;
	private int _ncotp;

	public String getPassword()
	{
		return _password;
	}

	public String getUser()
	{
		return _user;
	}

	public int getOneTimePassword()
	{
		return _ncotp;
	}

	@Override
	public boolean readImpl()
	{
		L2LoginClient client = getClient();
		if(client.isProtectUsed() && client.getHWID() == null)
		{
			return false;
		}
		if(getAvaliableBytes() >= 128)
		{
			readB(_raw);
			try
			{
				readD();
				readD();
				readD();
				readD();
				readD();
				//это как-то связано с GG
				readD(); //const = 8
				readH();
				readC();
				return true;
				// System.out.println("RequestAuthLogin: d1:"+d1+"|d2:"+d2+"|d3:"+d3+"|d4:"+d4+"|d5:"+d5+"|d6:"+d6+"|h:"+h+"|ClientOrder:"+clientOrder);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void runImpl()
	{
		L2LoginClient client = getClient();
		byte[] decrypted;
		try
		{
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
			decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80);
		}
		catch(GeneralSecurityException e)
		{
			e.printStackTrace();
			return;
		}
		_user = new String(decrypted, 0x5E, 14).trim();
		_user = _user.toLowerCase();
		_password = new String(decrypted, 0x6C, 16).trim();
		_ncotp = decrypted[0x7c];
		_ncotp |= decrypted[0x7d] << 8;
		_ncotp |= decrypted[0x7e] << 16;
		_ncotp |= decrypted[0x7f] << 24;
		LoginController lc = LoginController.getInstance();
		Status status = lc.tryAuthLogin(_user, _password, client);
		if(status.state == State.IN_USE)
		{
			L2LoginClient oldClient = lc.getAuthedClient(_user);
			// кикаем другого клиента, подключенного к логину
			if(oldClient != null)
			{
				oldClient.close(LoginFailReason.REASON_ACCOUNT_IN_USE);
			}
			if(lc.isAccountInLoginServer(_user))
			{
				lc.removeAuthedLoginClient(_user).close(LoginFailReason.REASON_ACCOUNT_IN_USE);
			}
			status.state = State.VALID;
		}
		if(status.state == State.VALID)
		{
			client.setAccount(_user);
			client.setState(LoginClientState.AUTHED_LOGIN);
			client.setSessionKey(lc.assignSessionKeyToClient());
			lc.addAuthedLoginClient(_user, client);
			client.setBonus(status.bonus, status.bonus_expire);
			client.sendPacket(new LoginOk(client.getSessionKey()));
		}
		else if(status.state == State.WRONG)
		{
			if(Config.LoginProtectBrute)
			{
				client.sendPacket(new ServerList(client, true));
			}
			else
			{
				client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
			}
		}
		else if(status.state == State.BANNED)
		{
			client.close(new LoginFail(LoginFailReason.REASON_ACCESS_FAILED));
		}
		else if(status.state == State.IP_ACCESS_DENIED)
		{
			client.close(LoginFailReason.REASON_ATTEMPTED_RESTRICTED_IP);
		}
	}
}