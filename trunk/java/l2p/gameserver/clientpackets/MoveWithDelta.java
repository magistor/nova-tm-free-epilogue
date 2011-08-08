package l2p.gameserver.clientpackets;

public class MoveWithDelta extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		readD();
		readD();
		readD();
	}

	@Override
	protected void runImpl()
	{
	}
}