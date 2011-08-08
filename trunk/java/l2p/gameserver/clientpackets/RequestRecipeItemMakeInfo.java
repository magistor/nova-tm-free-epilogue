package l2p.gameserver.clientpackets;

import l2p.gameserver.serverpackets.RecipeItemMakeInfo;

public class RequestRecipeItemMakeInfo extends L2GameClientPacket
{
	private int _id;

	/**
	 * packet type id 0xB7
	 * format:		cd
	 */
	@Override
	public void readImpl()
	{
		_id = readD();
	}

	@Override
	public void runImpl()
	{
		sendPacket(new RecipeItemMakeInfo(_id, getClient().getActiveChar(), 0xffffffff));
	}
}