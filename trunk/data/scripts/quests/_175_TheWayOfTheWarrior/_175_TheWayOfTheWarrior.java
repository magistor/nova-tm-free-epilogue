package quests._175_TheWayOfTheWarrior;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.serverpackets.ExShowScreenMessage;
import l2p.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;

/**
 * Квест на вторую профессию The Way Of The Warrior
 *
 * @author Sergey Ibryaev aka Artful
 */
public class _175_TheWayOfTheWarrior extends Quest implements ScriptFile
{
	//NPC
	private static final int Kekropus = 32138;
	private static final int Perwan = 32133;
	//Quest Items
	private static final int WolfTail = 9807;
	private static final int MuertosClaw = 9808;
	//Items
	private static final int WarriorsSword = 9720;
	//MOBs
	private static final int MountainWerewolf = 22235;
	private static final int MountainWerewolfChief = 22235;
	private static final int MuertosArcher = 22236;
	private static final int MuertosGuard = 22239;
	private static final int MuertosScout = 22240;
	private static final int MuertosWarrior = 22242;
	private static final int MuertosCaptain = 22243;
	private static final int MuertosLieutenant = 22245;
	private static final int MuertosCommander = 22246;
	//Drop Cond
	//# [COND, NEWCOND, ID, REQUIRED, ITEM, NEED_COUNT, CHANCE, DROP]	
	private static final int[][] DROPLIST_COND = {{2, 3, MountainWerewolf, 0, WolfTail, 5, 35, 1},
		{2, 3, MountainWerewolfChief, 0, WolfTail, 5, 40, 1}, {7, 8, MuertosArcher, 0, MuertosClaw, 10, 32, 1},
		{7, 8, MuertosGuard, 0, MuertosClaw, 10, 44, 1}, {7, 8, MuertosScout, 0, MuertosClaw, 10, 48, 1},
		{7, 8, MuertosWarrior, 0, MuertosClaw, 10, 56, 1}, {7, 8, MuertosCaptain, 0, MuertosClaw, 10, 60, 1},
		{7, 8, MuertosLieutenant, 0, MuertosClaw, 10, 68, 1}, {7, 8, MuertosCommander, 0, MuertosClaw, 10, 72, 1}};

	public void onLoad()
	{
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}

	public _175_TheWayOfTheWarrior()
	{
		super(false);
		addStartNpc(Kekropus);
		addTalkId(Perwan);
		for(int i = 0; i < DROPLIST_COND.length; i++)
		{
			addKillId(DROPLIST_COND[i][2]);
		}
		addQuestItem(WolfTail);
		addQuestItem(MuertosClaw);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("32138-04.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("32138-08.htm"))
		{
			st.takeItems(MuertosClaw, -1);
			st.giveItems(WarriorsSword, 1);
			st.giveItems(ADENA_ID, 8799, false);
			st.getPlayer().addExpAndSp(20739, 1777, false, false);
			if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarB("p1q3"))
			{
				st.getPlayer().setVar("p1q3", "1"); // flag for helper
				st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ScreenMessageAlign.TOP_CENTER, true));
				st.giveItems(1060, 100); // healing potion
				for(int item = 4412; item <= 4417; item++)
				{
					st.giveItems(item, 10);
				} // echo cry
				st.playTutorialVoice("tutorial_voice_026");
				st.giveItems(5789, 7000); // newbie ss
			}
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == Kekropus)
		{
			if(cond == 0)
			{
				if(st.getPlayer().getRace() != Race.kamael)
				{
					htmltext = "32138-00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 10)
				{
					htmltext = "32138-01.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "32138-02.htm";
				}
			}
			else if(cond == 1)
			{
				htmltext = "32138-04.htm";
			}
			else if(cond == 4)
			{
				st.set("cond", "5");
				st.setState(STARTED);
				htmltext = "32138-05.htm";
			}
			else if(cond == 6)
			{
				st.set("cond", "7");
				st.setState(STARTED);
				htmltext = "32138-06.htm";
			}
			else if(cond == 8)
			{
				htmltext = "32138-07.htm";
			}
		}
		else if(npcId == Perwan)
		{
			if(cond == 1)
			{
				st.set("cond", "2");
				st.setState(STARTED);
				htmltext = "32133-01.htm";
			}
			else if(cond == 3)
			{
				st.takeItems(WolfTail, -1);
				st.set("cond", "4");
				st.setState(STARTED);
				htmltext = "32133-02.htm";
			}
			else if(cond == 5)
			{
				st.set("cond", "6");
				st.setState(STARTED);
				htmltext = "32133-03.htm";
			}
		}
		return htmltext;
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		for(int i = 0; i < DROPLIST_COND.length; i++)
		{
			if(cond == DROPLIST_COND[i][0] && npcId == DROPLIST_COND[i][2])
			{
				if(DROPLIST_COND[i][3] == 0 || st.getQuestItemsCount(DROPLIST_COND[i][3]) > 0)
				{
					if(DROPLIST_COND[i][5] == 0)
					{
						st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][6]);
					}
					else if(st.rollAndGive(DROPLIST_COND[i][4], DROPLIST_COND[i][7], DROPLIST_COND[i][7], DROPLIST_COND[i][5], DROPLIST_COND[i][6]))
					{
						if(DROPLIST_COND[i][1] != cond && DROPLIST_COND[i][1] != 0)
						{
							st.set("cond", String.valueOf(DROPLIST_COND[i][1]));
							st.setState(STARTED);
						}
					}
				}
			}
		}
		return null;
	}
}