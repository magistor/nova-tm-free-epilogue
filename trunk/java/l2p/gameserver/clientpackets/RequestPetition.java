package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;
import l2p.util.Log;

/**
 * Format: (c) Sd
 */
public class RequestPetition extends L2GameClientPacket
{
	private String _text;
	private int _type;

	@Override
	public void readImpl()
	{
		_text = readS(4096);
		_type = readD();
	}

	@Override
	public void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		Log.LogPetition(activeChar, _type, _text);
	}
}