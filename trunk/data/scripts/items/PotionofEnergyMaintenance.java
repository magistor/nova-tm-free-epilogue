package items;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.IItemHandler;
import l2p.gameserver.handler.ItemHandler;
import l2p.gameserver.model.L2Playable;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.L2ItemInstance;

/**
 * @author SYS
 */
public class PotionofEnergyMaintenance implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = {20391};

	public void useItem(L2Playable playable, L2ItemInstance item, Boolean ctrl)
	{
		if(playable == null || !playable.isPlayer())
		{
			return;
		}
		L2Player player = (L2Player) playable;
		player.setVitality(10000);
		player.sendPacket(Msg.YOU_HAVE_GAINED_VITALITY_POINTS);
		Functions.removeItem(player, 20391, 1);
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