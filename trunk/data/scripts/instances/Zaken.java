package instances;

import javolution.util.FastMap;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.instancemanager.InstancedZoneManager;
import l2p.gameserver.model.L2CommandChannel;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.Reflection;
import l2p.gameserver.serverpackets.SystemMessage;

/**
 * User: Shaitan
 * Date: 23.12.10
 * Time: 10:40
 */
public class Zaken extends Functions implements ScriptFile
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

	public void enterInstance(String[] strings)
	{
		L2Player player = (L2Player) getSelf();
		if(player.getParty() == null)
		{
			return;
		}
		if(player.getParty().getCommandChannel() == null)
		{
			return;
		}
		L2CommandChannel cc = player.getParty().getCommandChannel();
		if(cc.getChannelLeader() != player)
		{
			player.sendMessage("You must be leader of the command channel.");
			return;
		}
		int instancedZoneId;
		if(strings[0].equalsIgnoreCase("Night") && GameTimeController.getInstance().isNowNight())
		{
			instancedZoneId = 515;
		}
		else if(strings[0].equalsIgnoreCase("Day") && !GameTimeController.getInstance().isNowNight())
		{
			instancedZoneId = 516;
		}
		else
		{
			player.sendMessage("Не подходящее время.");
			return;
		}
		InstancedZoneManager izm = InstancedZoneManager.getInstance();
		FastMap<Integer, InstancedZoneManager.InstancedZone> izs = InstancedZoneManager.getInstance().getById(instancedZoneId);
		if(izs == null)
		{
			player.sendPacket(Msg.SYSTEM_ERROR);
			return;
		}
		InstancedZoneManager.InstancedZone iz = izs.get(0);
		if(iz == null)
		{
			player.sendPacket(Msg.SYSTEM_ERROR);
			return;
		}
		String name = iz.getName();
		int timelimit = iz.getTimelimit();
		int minMembers = iz.getMinParty();
		int maxMembers = iz.getMaxParty();
		if(cc.getMemberCount() < minMembers)
		{
			player.sendMessage("The command channel must contains at least " + minMembers + " members.");
			return;
		}
		if(cc.getMemberCount() > maxMembers)
		{
			player.sendMessage("The command channel must contains not more than " + maxMembers + " members.");
			return;
		}
		for(L2Player member : cc.getMembers())
		{
			if(member.isCursedWeaponEquipped() || member.isInFlyingTransform() || member.isDead())
			{
				player.sendPacket(new SystemMessage(SystemMessage.C1S_QUEST_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED).addName(member));
				return;
			}
			if(!player.isInRange(member, 500))
			{
				member.sendPacket(Msg.ITS_TOO_FAR_FROM_THE_NPC_TO_WORK);
				player.sendPacket(Msg.ITS_TOO_FAR_FROM_THE_NPC_TO_WORK);
				return;
			}
			if(izm.getTimeToNextEnterInstance(name, member) > 0)
			{
				cc.broadcastToChannelMembers(new SystemMessage(SystemMessage.C1_MAY_NOT_RE_ENTER_YET).addName(member));
				return;
			}
		}
		Reflection r = new Reflection(name);
		r.setInstancedZoneId(instancedZoneId);
		for(InstancedZoneManager.InstancedZone i : izs.values())
		{
			if(r.getTeleportLoc() == null)
			{
				r.setTeleportLoc(i.getTeleportCoords());
			}
			r.FillSpawns(i.getSpawnsInfo());
			r.FillDoors(i.getDoors());
		}
		r.setCoreLoc(r.getReturnLoc());
		r.setReturnLoc(player.getLoc());
		for(L2Player member : cc.getMembers())
		{
			member.setVar(name, String.valueOf(System.currentTimeMillis()));
			member.setVar("backCoords", r.getReturnLoc().toXYZString());
			member.teleToLocation(iz.getTeleportCoords(), r.getId());
		}
		cc.setReflection(r);
		r.setCommandChannel(cc);
		if(timelimit > 0)
		{
			r.startCollapseTimer(timelimit * 60 * 1000L);
		}
	}
}