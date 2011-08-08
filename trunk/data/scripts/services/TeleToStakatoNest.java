package services;

import l2p.Config;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Party;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Files;
import l2p.util.Location;

public class TeleToStakatoNest extends Functions implements ScriptFile
{
	private final static Location[] teleports = {new Location(80456, -52322, -5640), new Location(88718, -46214, -4640),
		new Location(87464, -54221, -5120), new Location(80848, -49426, -5128), new Location(87682, -43291, -4128)};

	public void list()
	{
		L2Player player = (L2Player) getSelf();
		L2NpcInstance npc = getNpc();
		if(player == null || npc == null)
		{
			return;
		}
		if(Config.DONTLOADQUEST)
		{
			if(player.isQuestCompleted("_240_ImTheOnlyOneYouCanTrust"))
			{
				show(Files.read("data/scripts/services/TeleToStakatoNest-no.htm", player), player);
				return;
			}
		}
		show(Files.read("data/scripts/services/TeleToStakatoNest.htm", player), player);
	}

	public void teleTo(String[] args)
	{
		L2Player player = (L2Player) getSelf();
		L2NpcInstance npc = getNpc();
		if(player == null || npc == null)
		{
			return;
		}
		if(args.length != 1)
		{
			return;
		}
		Location loc = teleports[Integer.parseInt(args[0]) - 1];
		L2Party party = player.getParty();
		if(party == null)
		{
			player.teleToLocation(loc);
		}
		else
		{
			for(L2Player member : party.getPartyMembers())
			{
				if(member != null && member.isInRange(player, 1000))
				{
					member.teleToLocation(loc);
				}
			}
		}
	}

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}