package l2p.gameserver.clientpackets;

import l2p.gameserver.serverpackets.ExBR_MinigameLoadScoresPacket;

public class RequestBR_MinigameLoadScores extends L2GameClientPacket
{
	@Override
	protected void readImpl() throws Exception
	{
		//just a trigger
	}

	@Override
	protected void runImpl() throws Exception
	{
		new ExBR_MinigameLoadScoresPacket();
		//TODO send
	}
}