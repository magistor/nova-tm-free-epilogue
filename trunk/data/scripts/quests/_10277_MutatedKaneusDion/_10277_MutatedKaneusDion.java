package quests._10277_MutatedKaneusDion;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class _10277_MutatedKaneusDion extends Quest implements ScriptFile
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

	// NPCs
	private static final int Lucas = 30071;
	private static final int Mirien = 30461;
	// MOBs
	private static final int CrimsonHatuOtis = 18558;
	private static final int SeerFlouros = 18559;
	// Items
	private static final int Tissue1 = 13832;
	private static final int Tissue2 = 13833;
	public CheckStatus LAST_CHECK_STATUS = CheckStatus.GRACIA_EPILOGUE;

	public _10277_MutatedKaneusDion()
	{
		super(true);
		addStartNpc(Lucas);
		addTalkId(Mirien);
		addKillId(CrimsonHatuOtis, SeerFlouros);
		addQuestItem(Tissue1, Tissue2);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30071-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30461-02.htm"))
		{
			st.giveItems(57, 120000);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int id = st.getState();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		if(id == COMPLETED)
		{
			if(npcId == Lucas)
			{
				htmltext = "30071-0a.htm";
			}
		}
		else if(id == CREATED && npcId == Lucas)
		{
			if(st.getPlayer().getLevel() >= 28)
			{
				htmltext = "30071-01.htm";
			}
			else
			{
				htmltext = "30071-00.htm";
			}
		}
		else
		{
			if(npcId == Lucas)
			{
				if(cond == 1)
				{
					htmltext = "30071-04.htm";
				}
				else if(cond == 2)
				{
					htmltext = "30071-05.htm";
				}
			}
			else if(npcId == Mirien)
			{
				if(cond == 1)
				{
					htmltext = "30461-01a.htm";
				}
				else if(cond == 2)
				{
					htmltext = "30461-01.htm";
				}
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		if(st.getState() == STARTED && st.getCond() == 1)
		{
			st.giveItems(Tissue1, 1);
			st.giveItems(Tissue2, 1);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}