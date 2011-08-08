package quests._198_Embryo;

import javolution.util.FastMap;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.ai.CtrlEvent;
import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.instancemanager.InstancedZoneManager;
import l2p.gameserver.instancemanager.InstancedZoneManager.InstancedZone;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.Reflection;
import l2p.gameserver.model.instances.L2DoorInstance;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.model.quest.Quest;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.serverpackets.ExStartScenePlayer;
import l2p.gameserver.serverpackets.SystemMessage;
import l2p.gameserver.tables.ReflectionTable;
import l2p.util.Location;
import quests._197_SevenSignTheSacredBookOfSeal._197_SevenSignTheSacredBookOfSeal;

import java.util.HashMap;

public class _198_Embryo extends Quest implements ScriptFile
{
	private static final int Wood = 32593;
	private static final int Franz = 32597;
	private static final int Jaina = 32582;
	private static final int SculptureofDoubt = 14360;
	private static final int AncientAdena = 5575;
	private static final int DawnsBracelet = 15312;
	private static final int ShilensEvilThoughts = 27346;

	private static FastMap<Integer, Integer> spawns = new FastMap<Integer, Integer>();

	public _198_Embryo()
	{
		super(false);

		addStartNpc(Wood);
		addTalkId(Franz);
		addTalkId(Jaina);
		addKillId(ShilensEvilThoughts);
		addQuestItem(SculptureofDoubt);
	}

	@Override
	public String onEvent(String event, QuestState st, L2NpcInstance npc)
	{
		String htmltext = event;
		L2Player player = st.getPlayer();
		Reflection r = player.getReflection();

		if(event.equals("32593-02.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("32593-03.htm"))
		{
			enterInstance(player);
			return "32593-03.htm";
		}
		else if(event.equals("32582-03.htm"))
		{
			returnToAden(player);
			return "32582-03.htm";
		}
		else if(event.equals("32597-05.htm"))
		{
			L2NpcInstance mob = addSpawnToInstance(ShilensEvilThoughts, new Location(-23752,-9192,-5384), 0, r.getId());
			spawns.put(player.getObjectId(), mob.getObjectId());
			Functions.npcSay(mob, "You are not the owner of that item.");
			mob.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 100000);
		}
		else if(event.equals("32597-11.htm"))
		{
			st.set("cond", "3");
			st.playSound(SOUND_MIDDLE);
			Functions.npcSay(npc, "We will be with you always...");
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int id = st.getState();
		L2Player player = st.getPlayer();

		if(npcId == Wood)
		{
			if(id == CREATED)
			{
				if(player.getLevel() < 79)
				{
					st.exitCurrentQuest(true);
					return "32593-00.htm";
				}
				QuestState qs = player.getQuestState(_197_SevenSignTheSacredBookOfSeal.class);
				if(qs == null || !qs.isCompleted())
				{
					st.exitCurrentQuest(true);
					return "noquest";
				}
				return "32593-01.htm";
			}
			if(cond >= 1 && cond <= 2)
				return "32593-04.htm";
			else if(cond == 3)
			{
				st.takeItems(SculptureofDoubt, 1);
				htmltext = "32593-05.htm";
				st.addExpAndSp(315108090, 34906059);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
				st.giveItems(AncientAdena, 1500000);
				st.giveItems(DawnsBracelet, 1);
			}
		}
		else if(npcId == Franz)
		{
			if(cond == 1)
			{
				Integer oid = spawns.get(player.getObjectId());
				L2NpcInstance mob = oid != null ? L2ObjectsStorage.getNpc(oid) : null;
				if(mob == null || mob.isDead())
					return "32597-01.htm";
				else
					return "32597-06.htm";
			}
			else if(cond == 2)
				return "32597-07.htm";
			else if(cond == 3)
				return "32597-12.htm";
		}
		else if(npcId == Jaina)
			if(cond >= 1)
				return "32582-01.htm";
		return htmltext;
	}

	private void enterInstance(L2Player player)
	{
		int instancedZoneId = 113;
		InstancedZoneManager ilm = InstancedZoneManager.getInstance();
		FastMap<Integer, InstancedZone> ils = ilm.getById(instancedZoneId);

		if(ils == null)
		{
			player.sendPacket(Msg.SYSTEM_ERROR);
			return;
		}

		InstancedZone il = ils.get(0);

		assert il != null;

		Reflection r = new Reflection(il.getName());
		r.setInstancedZoneId(instancedZoneId);
		for(InstancedZone i : ils.values())
		{
			if(r.getReturnLoc() == null)
				r.setReturnLoc(i.getReturnCoords());
			if(r.getTeleportLoc() == null)
				r.setTeleportLoc(i.getTeleportCoords());
			r.FillSpawns(i.getSpawnsInfo());
		}

		player.setReflection(r);
		player.teleToLocation(il.getTeleportCoords());
		player.setVar("backCoords", r.getReturnLoc().toXYZString());
	}

	private void returnToAden(L2Player player)
	{
		Reflection r = player.getReflection();
		if(r.getReturnLoc() != null)
		    player.teleToLocation(r.getReturnLoc(), 0);
		else
		    player.setReflection(0);
		    player.unsetVar("backCoords");
	}

	@Override
	public String onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		L2Player player = st.getPlayer();
		if(player == null)
			return null;
		Integer id = spawns.get(player.getObjectId());
		if(npcId == ShilensEvilThoughts && cond == 1 && id != null && id == npc.getObjectId())
		{
		    npc.decayMe();
			player.sendPacket(new ExStartScenePlayer(ExStartScenePlayer.SCENE_SSQ_EMBRYO));
			st.set("cond", "2");
			st.playSound(SOUND_ITEMGET);
			st.giveItems(SculptureofDoubt, 1);
			Functions.npcSay(npc, player.getName() + "! You may have won this time... But next time, I will surely capture you!");
			L2NpcInstance franz = L2ObjectsStorage.getByNpcId(Franz);
			if(franz != null)
				Functions.npcSay(franz, "Well done. " + player.getName() + ". You help is much appreciated.");
		}
		return null;
	}

	@Override
    public void onLoad()
	{}

	@Override
    public void onReload()
	{}

	@Override
    public void onShutdown()
	{}
}