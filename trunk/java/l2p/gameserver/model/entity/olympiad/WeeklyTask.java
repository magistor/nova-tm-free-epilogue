package l2p.gameserver.model.entity.olympiad;

import l2p.Config;

import java.util.Calendar;

public class WeeklyTask implements Runnable
{
	public void run()
	{
		Olympiad.addWeeklyPoints();
		Olympiad._log.info("Olympiad System: Added weekly points to nobles");
		Calendar nextChange = Calendar.getInstance();
		Olympiad._nextWeeklyChange = nextChange.getTimeInMillis() + Config.ALT_OLY_WPERIOD;
	}
}