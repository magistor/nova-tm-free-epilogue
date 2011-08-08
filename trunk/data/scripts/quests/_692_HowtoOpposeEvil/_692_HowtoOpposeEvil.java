package quests._692_HowtoOpposeEvil;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class _692_HowtoOpposeEvil extends Quest implements ScriptFile
{
	// NPCs
	private static int DILIOS = 32549;
	private static int KUTRAN = 32550;
	// items
	private static int LEKONS_CERTIFICATE = 13857;
	private static int NUCLEUS_OF_A_FREED_SOUL = 13796;
	private static int FLEET_STEED_TROUPS_CHARM = 13841;
	// kill
	private static int[] DESTRUCTION_MOBS = {22537, 22538, 22539, 22540, 22541, 22542, 22543, 22544, 22546, 22547, 22548, 22549, 22550, 22551, 22552, 22593, 22596, 22597};
	private static int[] IMMORTALITY_MOBS = {22510, 22511, 22512, 22513, 22514, 22515};

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}

	public _692_HowtoOpposeEvil()
	{
		super(false);
		addStartNpc(DILIOS);
		addTalkId(KUTRAN);
		addQuestItem(LEKONS_CERTIFICATE, NUCLEUS_OF_A_FREED_SOUL, FLEET_STEED_TROUPS_CHARM);
		for(int id : DESTRUCTION_MOBS)
		{
			addKillId(id);
		}
		for(int id : IMMORTALITY_MOBS)
		{
			addKillId(id);
		}
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		L2Player player = st.getPlayer();
		String htmltext = event;
		if(event.equalsIgnoreCase("32549-03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32550-04.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int id = st.getState();
		L2Player player = st.getPlayer();
		if(id == CREATED)
		{
			if(player.getLevel() < 75)
			{
				st.exitCurrentQuest(true);
				return "noquest";
			}
			else
			{
				return "32549-01.htm";
			}
		}
		else
		{
			if(npcId == DILIOS)
			{
				if(cond == 1 && st.getQuestItemsCount(LEKONS_CERTIFICATE) >= 1)
				{
					st.setCond(2);
					return "32549-04.htm";
				}
				else if(cond == 2)
				{
					return "32549-05.htm";
				}
			}
			else
			{
				if(cond == 2)
				{
					return "32550-01.htm";
				}
				else if(cond == 3)
				{
					return "32550-04.htm";
				}
			}
		}
		return "noquest";
	}

	public String onKill(L2NpcInstance npc, QuestState st)
	{
		L2Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		for(int id : DESTRUCTION_MOBS)
		{
			if(npcId == id)
			{
				st.giveItems(FLEET_STEED_TROUPS_CHARM, 3);
				return null;
			}
		}
		for(int id : IMMORTALITY_MOBS)
		{
			if(npcId == id)
			{
				st.giveItems(NUCLEUS_OF_A_FREED_SOUL, 3);
				return null;
			}
		}
		return null;
	}
}