package l2p.gameserver.serverpackets;

import l2p.gameserver.model.L2Clan;

/**
 * sample
 * 0000: cd b0 98 a0 48 1e 01 00 00 00 00 00 00 00 00 00    ....H...........
 * 0010: 00 00 00 00 00                                     .....
 * <p/>
 * format   ddddd
 */
public class PledgeStatusChanged extends L2GameServerPacket
{
	private int leader_id, clan_id, level;

	public PledgeStatusChanged(L2Clan clan)
	{
		leader_id = clan.getLeaderId();
		clan_id = clan.getClanId();
		level = clan.getLevel();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0xCD);
		writeD(leader_id);
		writeD(clan_id);
		writeD(0);
		writeD(level);
		writeD(0);
		writeD(0);
		writeD(0);
	}
}