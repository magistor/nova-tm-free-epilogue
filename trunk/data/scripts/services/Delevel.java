package services;

import l2p.Config;
import l2p.extensions.multilang.CustomMessage;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.base.Experience;
import l2p.util.Util;
/*
Made by Dexter
Shaitan Team
*/

public class Delevel extends Functions implements ScriptFile
{
	public void delevel_page()
	{
		L2Player player = (L2Player) getSelf();
		if(player == null)
		{
			return;
		}
		String append = "Delevel";
		append += "<br>";
		append += "<font color=\"LEVEL\">" + new CustomMessage("scripts.services.Rename.DelevelFor", getSelf()).addString(Util.formatAdena(Config.SERVICES_DELEVEL_COUNT)).addItemName(Config.SERVICES_DELEVEL_ITEM) + "</font>";
		append += "<table>";
		append += "<tr><td></td></tr>";
		append += "<tr><td><button value=\"Делевел\" action=\"bypass -h scripts_services.Delevel:delevel\" width=80 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td></tr>";
		append += "</table>";
		show(append, player);
	}

	public void delevel()
	{
		L2Player player = (L2Player) getSelf();
		if(player.getLevel() <= Config.SERVICES_DELEVEL_MIN_LEVEL)
		{
			player.sendMessage("Данный сервис доступен персонажам с " + Config.SERVICES_DELEVEL_MIN_LEVEL + " уровня.");
			return;
		}
		else if(getItemCount(player, Config.SERVICES_DELEVEL_ITEM) < Config.SERVICES_DELEVEL_COUNT)
		{
			player.sendMessage("У вас недостаточно предметов.");
			return;
		}
		else
		{
			long pXp = player.getExp();
			long tXp = Experience.LEVEL[(player.getLevel() - 1)];
			if(pXp <= tXp)
			{
				return;
			}
			removeItem(player, Config.SERVICES_DELEVEL_ITEM, Config.SERVICES_DELEVEL_COUNT);
			player.addExpAndSp(-(pXp - tXp), 0, false, false);
		}
	}

	public void onLoad()
	{
		System.out.println("Loaded Service: Delevel");
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}