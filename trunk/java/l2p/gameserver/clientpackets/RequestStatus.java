package l2p.gameserver.clientpackets;

import l2p.gameserver.serverpackets.SendStatus;

import java.util.logging.Logger;

public final class RequestStatus extends L2GameClientPacket
{
	static Logger _log = Logger.getLogger(RequestStatus.class.getName());

	@Override
	protected void readImpl()
	{
	}

	@Override
	protected void runImpl()
	{
		getClient().close(new SendStatus());
	}
}