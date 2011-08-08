package quests._040_ASpecialOrder;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class _040_ASpecialOrder extends Quest implements ScriptFile
{
	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}

	// NPC
	static final int Helvetia = 30081;
	static final int OFulle = 31572;
	static final int Gesto = 30511;
	// Items
	static final int FatOrangeFish = 6452;
	static final int NimbleOrangeFish = 6450;
	static final int OrangeUglyFish = 6451;
	// Quest items
	static final int FishChest = 12764;
	static final int SeedJar = 12765;
	static final int WondrousCubic = 10632;

	public _040_ASpecialOrder()
	{
		super(false);
		addStartNpc(Helvetia);
		addQuestItem(FishChest);
		addQuestItem(SeedJar);
		addTalkId(OFulle);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		if(event.equals("take"))
		{
			st.set("cond", "2");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == Helvetia)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getLevel() >= 40)
				{
					htmltext = "Helvetia-1.htm";
				}
				else
				{
					htmltext = "Helvetia-level.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 2 || cond == 3)
			{
				htmltext = "Helvetia-whereismyfish.htm";
			}
			else if(cond == 4)
			{
				st.takeAllItems(FishChest);
				st.giveItems(WondrousCubic, 1, false);
				st.exitCurrentQuest(false);
				htmltext = "Helvetia-finish.htm";
			}
		}
		else if(npcId == OFulle)
		{
			if(cond == 2)
			{
				htmltext = "OFulle-1.htm";
				st.set("cond", "3");
			}
			else if(cond == 3)
			{
				if(st.getQuestItemsCount(FatOrangeFish) >= 10 && st.getQuestItemsCount(NimbleOrangeFish) >= 10 && st.getQuestItemsCount(OrangeUglyFish) >= 10)
				{
					st.takeItems(FatOrangeFish, 10);
					st.takeItems(NimbleOrangeFish, 10);
					st.takeItems(OrangeUglyFish, 10);
					st.giveItems(FishChest, 1, false);
					st.set("cond", "4");
					htmltext = "OFulle-2.htm";
				}
				else
				{
					htmltext = "OFulle-1.htm";
				}
			}
		}
		return htmltext;
	}
}