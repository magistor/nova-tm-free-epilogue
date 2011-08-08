package items;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.IItemHandler;
import l2p.gameserver.handler.ItemHandler;
import l2p.gameserver.model.L2Playable;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.model.items.L2ItemInstance;
import l2p.gameserver.serverpackets.Dice;
import l2p.gameserver.serverpackets.SystemMessage;

/**
 * @author SYS
 */
public class PotionofVitalityReplenishing implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = {20392};

	public void useItem(L2Playable playable, L2ItemInstance item, Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return;
		}
		L2Player player = (L2Player) playable;
		SkillTable.getInstance().getInfo(2580, 1).getEffects(player, player, false, false);
		player.sendPacket(Msg.YOU_HAVE_GAINED_VITALITY_POINTS);
		Functions.removeItem(player, 20392, 1);
	}
		
	public final int[] getItemIds()
	{
		return _itemIds;
	}

	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}