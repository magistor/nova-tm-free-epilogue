package quests._688_DefeatTheElrokianRaiders;

import l2p.Config;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.util.Rnd;

public class _688_DefeatTheElrokianRaiders extends Quest implements ScriptFile
{
	//Settings: drop chance in %
	private static int DROP_CHANCE = 50;
	private static int DINOSAUR_FANG_NECKLACE = 8785;

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}

	public _688_DefeatTheElrokianRaiders()
	{
		super(false);
		addStartNpc(32105);
		addTalkId(32105);
		addKillId(22214);
		addQuestItem(DINOSAUR_FANG_NECKLACE);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		long count = st.getQuestItemsCount(DINOSAUR_FANG_NECKLACE);
		if(event.equalsIgnoreCase("32105-02.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32105-08.htm"))
		{
			if(count > 0)
			{
				st.takeItems(DINOSAUR_FANG_NECKLACE, -1);
				st.giveItems(ADENA_ID, count * 3000);
			}
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else if(event.equalsIgnoreCase("32105-06.htm"))
		{
			st.takeItems(DINOSAUR_FANG_NECKLACE, -1);
			st.giveItems(ADENA_ID, count * 3000);
		}
		else if(event.equalsIgnoreCase("32105-07.htm"))
		{
			if(count >= 100)
			{
				st.takeItems(DINOSAUR_FANG_NECKLACE, 100);
				st.giveItems(ADENA_ID, 450000);
			}
			else
			{
				htmltext = "32105-04.htm";
			}
		}
		else if(event.equalsIgnoreCase("None"))
		{
			htmltext = null;
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		long count = st.getQuestItemsCount(DINOSAUR_FANG_NECKLACE);
		if(cond == 0)
		{
			if(st.getPlayer().getLevel() >= 75)
			{
				htmltext = "32105-01.htm";
			}
			else
			{
				htmltext = "32105-00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 1)
		{
			if(count == 0)
			{
				htmltext = "32105-04.htm";
			}
			else
			{
				htmltext = "32105-05.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		long count = st.getQuestItemsCount(DINOSAUR_FANG_NECKLACE);
		if(st.getInt("cond") == 1 && count < 100 && Rnd.chance(DROP_CHANCE))
		{
			long numItems = (int) Config.RATE_QUESTS_REWARD;
			if(count + numItems > 100)
			{
				numItems = 100 - count;
			}
			if(count + numItems >= 100)
			{
				st.playSound("ItemSound.quest_middle");
			}
			else
			{
				st.playSound("ItemSound.quest_itemget");
			}
			st.giveItems(DINOSAUR_FANG_NECKLACE, numItems);
		}
		return null;
	}
}