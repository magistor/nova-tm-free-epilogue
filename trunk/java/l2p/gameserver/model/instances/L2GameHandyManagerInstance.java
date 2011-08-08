package l2p.gameserver.model.instances;

import l2p.Config;
import l2p.gameserver.instancemanager.games.GameHandyManager;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.templates.L2NpcTemplate;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 03.02.11
 * Time: 19:03
 * http://nova-tm.ru/
 */
public class L2GameHandyManagerInstance extends L2NpcInstance
{
	private static final String htmlPath = Config.DATAPACK_ROOT + "data/html/gamehandy/";

	public L2GameHandyManagerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onBypassFeedback(L2Player player, String command)
	{
		if (!(command.startsWith("Register")))
			return;
		if (player.getInventoryLimit() * 0.8 <= player.getInventory().getSize())
		{
            player.sendPacket(new SystemMessage(SystemMessage.PROGRESS_IN_A_QUEST_IS_POSSIBLE_ONLY_WHEN_YOUR_INVENTORYS_WEIGHT_AND_VOLUME_ARE_LESS_THAN_80_PERCENT_OF_CAPACITY));
			showChatWindow(player, "data/html/gamehandy/limit.htm");
			return;
		}
		if (player.isCursedWeaponEquipped())
		{
			showChatWindow(player, "data/html/gamehandy/CursedWeaponEquipped.htm");

			return;
		}
		if (Olympiad.isRegistered(player))
		{
			showChatWindow(player, "data/html/gamehandy/Olympiad.htm");
			return;
		}
		if (player.getKarma() > 0)
		{
			showChatWindow(player, "data/html/gamehandy/Karma.htm");

			return;
		}
		int arenaId = Integer.parseInt(command.substring(9));
		if (GameHandyManager.registerPlayer(player, arenaId))
			showChatWindow(player, "data/html/gamehandy/Registered.htm");
		else
			showChatWindow(player, 0);
	}

	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		if (val == 0)
			pom = "" + npcId;
		else
			pom = npcId + "-" + val;
		return "data/html/gamehandy/" + pom + ".htm";
	}
}