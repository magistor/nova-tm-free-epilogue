package l2p.gameserver.clientpackets;

public class RequestExMpccPartymasterList extends L2GameClientPacket
{
	@Override
	public void runImpl()
	{
		System.out.println(getType());
	}

	@Override
	public void readImpl()
	{
		//just a trigger
	}
}