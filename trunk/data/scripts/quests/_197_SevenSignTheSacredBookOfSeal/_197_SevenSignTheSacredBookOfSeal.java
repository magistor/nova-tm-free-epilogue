package quests._197_SevenSignTheSacredBookOfSeal;

import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;

public class _197_SevenSignTheSacredBookOfSeal extends Quest implements ScriptFile
{
	// NPCs
	private static int WOOD = 32593;
	private static int ORVEN = 30857;
	private static int LEOPARD = 32594;
	private static int LAWRENCE = 32595;
	private static int SOFIA = 32596;
	private static int SHILENSEVIL = 27343;
	// ITEMS
	private static int TEXT = 13829;
	private static int SCULPTURE = 14356;

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}

	public L2NpcInstance monster;

	public _197_SevenSignTheSacredBookOfSeal()
	{
		super(false);
		addStartNpc(WOOD);
		addTalkId(WOOD, ORVEN, LEOPARD, LAWRENCE, SOFIA);
		addQuestItem(TEXT, SCULPTURE);
		addKillId(SHILENSEVIL);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		L2Player player = st.getPlayer();
		String htmltext = event;
		if(event.equalsIgnoreCase("32593-04.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30857-04.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32594-03.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32595-04.htm"))
		{
			monster = st.addSpawn(SHILENSEVIL, 152520, -57685, -3438, 60000);
			Functions.npcSay(monster, "You are not the owner of that item!");
			monster.setRunning();
			player.addDamageHate(monster, 0, 999);
			monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
		}
		else if(event.equalsIgnoreCase("32595-08.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(5);
		}
		else if(event.equalsIgnoreCase("32596-04.htm"))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(6);
			st.giveItems(TEXT, 1);
		}
		else if(event.equalsIgnoreCase("32593-08.htm"))
		{
			st.addExpAndSp(52518015, 5817676);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			st.takeItems(TEXT, 1);
			st.takeItems(SCULPTURE, 1);
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
		if(npcId == WOOD)
		{
			if(npcId == WOOD)
			{
				if(player.getLevel() < 79)
				{
					st.exitCurrentQuest(true);
					return "32593-00.htm";
				}
				QuestState qs = player.getQuestState("_196_SevenSignSealOfTheEmperor");
				if(qs == null)
				{
					st.exitCurrentQuest(true);
					return "noquest";
				}
				if(qs.isCompleted() && id == CREATED)
				{
					return "32593-01.htm";
				}
				else if(cond == 1)
				{
					return "32593-05.htm";
				}
				else if(cond == 6)
				{
					return "32593-06.htm";
				}
				else if(cond == 0)
				{
					st.exitCurrentQuest(true);
					return "32593-00.htm";
				}
			}
		}
		else if(npcId == ORVEN)
		{
			if(cond == 1)
			{
				return "30857-01.htm";
			}
			else if(cond == 2)
			{
				return "30857-05.htm";
			}
		}
		else if(npcId == LEOPARD)
		{
			if(cond == 2)
			{
				return "32594-01.htm";
			}
			else if(cond == 3)
			{
				return "32594-04.htm";
			}
		}
		else if(npcId == LAWRENCE)
		{
			if(cond == 3)
			{
				return "32595-01.htm";
			}
			else if(cond == 4)
			{
				return "32595-05.htm";
			}
			else if(cond == 5)
			{
				return "32595-09.htm";
			}
		}
		else if(npcId == SOFIA)
		{
			if(cond == 5)
			{
				return "32596-01.htm";
			}
			else if(cond == 6)
			{
				return "32596-05.htm";
			}
		}
		return "noquest";
	}

	public String onKill(L2NpcInstance npc, QuestState st)
	{
		L2Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		if(npcId == SHILENSEVIL && st.getCond() == 3)
		{
			st.giveItems(SCULPTURE, 1);
		}
		Functions.npcSay(npc, "... You may have won this time... But next time, I will surely capture you!");
		st.setCond(4);
		return null;
	}
}