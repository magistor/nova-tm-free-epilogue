package quests._310_OnlyWhatRemains;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.util.Rnd;

public class _310_OnlyWhatRemains extends Quest implements ScriptFile
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

	private static final int KINTAIJIN = 32640;
	private static final int GROW_ACCELERATOR = 14832;
	private static final int MULTI_COLORED_JEWEL = 14835;
	private static final int DIRTY_BEAD = 14880;
	//private static final int DROP_CHANCE = 60; no need core support
	private static final int BOSS = 25667;
	private static final int COCOON = 18793;
	private static final int SMALL = 14833;
	private static final int BIG = 14834;
	private static int[] MOBS = new int[] {22617, 22618, 22619, 22620, 22621, 22622, 22623, 22624, 22625, 22626, 22627, 22628, 22629, 22630, 22631, 22632, 22633, 22634};

	public _310_OnlyWhatRemains()
	{
		super("Only What Remains", true);
		addStartNpc(KINTAIJIN);
		addTalkId(KINTAIJIN);
		addTalkId(COCOON);
		addKillId(MOBS);
		addKillId(BOSS);
		addQuestItem(MULTI_COLORED_JEWEL, GROW_ACCELERATOR, DIRTY_BEAD, SMALL, BIG);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("32640-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		if(event.equalsIgnoreCase("32640-quit.htm"))
		{
			st.exitCurrentQuest(true);
			st.unset("cond");
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		L2Player player = st.getPlayer();
		QuestState qs = player.getQuestState("_240_ImTheOnlyOneYouCanTrust");
		String htmltext = "noquest";
		int id = st.getState();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if(id == CREATED)
		{
			if(npcId == KINTAIJIN)
			{
				if(qs != null && qs.isCompleted() && player.getLevel() >= 81 && cond == 0)
				{
					htmltext = "32640-01.htm";
				}
				else
				{
					htmltext = "32640-00.htm";
					st.exitCurrentQuest(true);
				}
			}
		}
		else if(id == STARTED)
		{
			if(npcId == KINTAIJIN && cond == 1)
			{
				if(st.getQuestItemsCount(DIRTY_BEAD) == 0)
				{
					htmltext = "32640-08.htm";
				}
				else if(st.getQuestItemsCount(DIRTY_BEAD) < 500)
				{
					htmltext = "32640-09.htm";
				}
				else if(st.getQuestItemsCount(DIRTY_BEAD) >= 500)
				{
					st.takeItems(DIRTY_BEAD, 500);
					st.giveItems(GROW_ACCELERATOR, 1);
					st.giveItems(MULTI_COLORED_JEWEL, 1);
					htmltext = "32640-10.htm";
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(IsInIntArray(npcId, MOBS) && cond == 1 && Rnd.chance(60))
		{
			st.giveItems(DIRTY_BEAD, 1);
		}
		if(npcId == BOSS)
		{
			int chance = Rnd.get(0, 100);
			if(chance > 50)
			{
				st.giveItems(SMALL, 1);
			}
			else
			{
				st.giveItems(BIG, 1);
			}
		}
		return null;
	}

	private static boolean IsInIntArray(int i, int[] a)
	{
		for(int _i : a)
		{
			if(_i == i)
			{
				return true;
			}
		}
		return false;
	}
}