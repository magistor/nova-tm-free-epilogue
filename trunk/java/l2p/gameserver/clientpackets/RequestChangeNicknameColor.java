package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Player;

public class RequestChangeNicknameColor extends L2GameClientPacket
{
	private int _unk1, _unk3;
	private String _unk2;

	@Override
	protected void readImpl()
	{
		_unk1 = readD();
		_unk2 = readS();
		_unk3 = readD();
	}

	@Override
	protected void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar != null)
		{
			activeChar.sendMessage("RequestChangeNicknameColor: " + _unk1 + " - " + _unk2 + " - " + _unk3);
		}
	}
}