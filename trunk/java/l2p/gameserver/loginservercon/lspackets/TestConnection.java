package l2p.gameserver.loginservercon.lspackets;

import l2p.gameserver.loginservercon.AttLS;
import l2p.gameserver.loginservercon.gspackets.TestConnectionResponse;

public class TestConnection extends LoginServerBasePacket
{
	public TestConnection(byte[] decrypt, AttLS loginServer)
	{
		super(decrypt, loginServer);
	}

	@Override
	public void read()
	{
		//System.out.println("GS: request obtained");
		getLoginServer().sendPacket(new TestConnectionResponse());
	}
}