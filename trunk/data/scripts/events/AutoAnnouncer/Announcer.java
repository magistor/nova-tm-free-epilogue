package events.AutoAnnouncer;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.Announcements;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.model.L2Player;
import l2p.util.Files;

public class Announcer extends Functions implements ScriptFile
{
	private static boolean _active = false;
	private static String[][] text = {{"Не забываем голосовать за сервер!", "240000"}, {"Приятной игры!", "240001"}};

	private static boolean isActive()
	{
		return ServerVariables.getString("event_Announcer", "off").equalsIgnoreCase("on");
	}

	public void startEvent()
	{
		L2Player player = (L2Player) getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if(!isActive())
		{
			ServerVariables.set("event_Announcer", "on");
			announce_run();
			System.out.println("Event: AutoAnnouncer started.");
		}
		else
		{
			player.sendMessage("Event 'AutoAnnouncer' already started.");
		}
		_active = true;
		show(Files.read("data/html/admin/events.htm", player), player, null);
	}

	public void stopEvent()
	{
		L2Player player = (L2Player) getSelf();
		if(!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if(isActive())
		{
			ServerVariables.unset("event_Announcer");
			System.out.println("Event: AutoAnnouncer stopped.");
		}
		else
		{
			player.sendMessage("Event 'AutoAnnouncer' not started.");
		}
		_active = false;
		show(Files.read("data/html/admin/events.htm", player), player, null);
	}

	public static void announce_run()
	{
		if(_active)
		{
			for(String[] element : text)
			{
				executeTask("events.AutoAnnouncer.Announcer", "announce", new Object[] {element[0],
					Integer.valueOf(element[1])}, Integer.valueOf(element[1]));
			}
		}
	}

	public static void announce(String text, Integer inter)
	{
		if(_active)
		{
			Announcements.getInstance().announceToAll(text);
			executeTask("events.AutoAnnouncer.Announcer", "announce", new Object[] {text, inter}, inter);
		}
	}

	public void onLoad()
	{
		if(isActive())
		{
			_active = true;
			announce_run();
			System.out.println("Loaded Event: AutoAnnouncer [state: activated]");
		}
		else
		{
			System.out.println("Loaded Event: AutoAnnouncer [state: deactivated]");
		}
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}