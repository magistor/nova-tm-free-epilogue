package l2p.gameserver.serverpackets;

public class ExReplyRegisterDominion extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0x91);
		System.out.println("WTF? ExReplyRegisterDominion");
		// TODO dddddd
	}
}