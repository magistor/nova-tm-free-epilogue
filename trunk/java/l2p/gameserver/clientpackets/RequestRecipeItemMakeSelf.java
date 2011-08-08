package l2p.gameserver.clientpackets;

import l2p.gameserver.RecipeController;
import l2p.gameserver.model.L2Player;

public class RequestRecipeItemMakeSelf extends L2GameClientPacket
{
	private int _id;

	/**
	 * packet type id 0xB8
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
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		if(activeChar.getDuel() != null || activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		RecipeController.getInstance().requestMakeItem(activeChar, _id);
	}
}