package l2p.gameserver.model.entity.olympiad;

import l2p.gameserver.Announcements;
import l2p.gameserver.model.entity.Hero;

public class ValidationTask implements Runnable
{
	public void run()
	{
		OlympiadDatabase.sortHerosToBe();
		OlympiadDatabase.saveNobleData();
		if(Hero.getInstance().computeNewHeroes(Olympiad._heroesToBe))
		{
			Olympiad._log.warning("Olympiad: Error while computing new heroes!");
		}
		Announcements.getInstance().announceToAll("Olympiad Validation Period has ended");
		Olympiad._period = 0;
		Olympiad._currentCycle++;
		OlympiadDatabase.cleanupNobles();
		OlympiadDatabase.loadNoblesRank();
		OlympiadDatabase.setNewOlympiadEnd();
		Olympiad.init();
		OlympiadDatabase.save();
	}
}