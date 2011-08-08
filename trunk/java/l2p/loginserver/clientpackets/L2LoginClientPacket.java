package l2p.loginserver.clientpackets;

import l2p.extensions.network.ReceivablePacket;
import l2p.loginserver.L2LoginClient;

import java.util.logging.Logger;

public abstract class L2LoginClientPacket extends ReceivablePacket<L2LoginClient>
{
	private static Logger _log = Logger.getLogger(L2LoginClientPacket.class.getName());

	@Override
	protected final boolean read()
	{
		try
		{
			return readImpl();
		}
		catch(Exception e)
		{
			_log.severe("ERROR READING: " + this.getClass().getSimpleName());
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void run()
	{
		try
		{
			runImpl();
		}
		catch(Exception e)
		{
			_log.severe("runImpl error: Client: " + getClient().toString());
			e.printStackTrace();
		}
		getClient().can_runImpl = true;
	}

	protected abstract boolean readImpl();

	protected abstract void runImpl() throws Exception;
}