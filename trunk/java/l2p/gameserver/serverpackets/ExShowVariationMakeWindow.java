package l2p.gameserver.serverpackets;

/**
 * Открывает окно аугмента, название от фонаря.
 */
public class ExShowVariationMakeWindow extends L2GameServerPacket
{
	@Override
	protected final void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0x51);
	}
}