package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.handler.AdminCommandHandler;
import l2p.gameserver.handler.IVoicedCommandHandler;
import l2p.gameserver.handler.VoicedCommandHandler;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Multisell;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.entity.olympiad.OlympiadManager;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;
import l2p.gameserver.network.L2GameClient;
import l2p.gameserver.serverpackets.NpcHtmlMessage;

import java.util.HashMap;
import java.util.logging.Level;

public class RequestBypassToServer extends L2GameClientPacket
{
	private String bypass = null;

	@Override
	public void readImpl()
	{
		bypass = readS();
	}

	@Override
	public void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null || bypass == null)
		{
			return;
		}
		try
		{
			L2NpcInstance npc = activeChar.getLastNpc();
			L2Object target = activeChar.getTarget();
			if(npc == null && target != null && target.isNpc())
			{
				npc = (L2NpcInstance) target;
			}
			if(bypass.startsWith("admin_"))
			{
				AdminCommandHandler.getInstance().useAdminCommandHandler(activeChar, bypass);
			}
			else if(bypass.equals("come_here") && activeChar.isGM())
			{
				comeHere(getClient());
			}
			else if(bypass.startsWith("player_help "))
			{
				playerHelp(activeChar.getObjectId(), bypass.substring(12));
			}
			else if(bypass.startsWith("scripts_"))
			{
				String command = bypass.substring(8).trim();
				String[] word = command.split("\\s+");
				String[] args = command.substring(word[0].length()).trim().split("\\s+");
				String[] path = word[0].split(":");
				if(path.length != 2)
				{
					_log.warning("Bad Script bypass!");
					return;
				}
				HashMap<String, Object> variables = new HashMap<String, Object>();
				if(npc != null)
				{
					variables.put("npc", npc.getStoredId());
				}
				else
				{
					variables.put("npc", null);
				}
				if(word.length == 1)
				{
					activeChar.callScripts(path[0], path[1], new Object[] {}, variables);
				}
				else
				{
					activeChar.callScripts(path[0], path[1], new Object[] {args}, variables);
				}
			}
			else if(bypass.startsWith("user_"))
			{
				String command = bypass.substring(5).trim();
				String word = command.split("\\s+")[0];
				String args = command.substring(word.length()).trim();
				IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getVoicedCommandHandler(word);
				if(vch != null)
				{
					vch.useVoicedCommand(word, activeChar, args);
				}
				else
				{
					_log.warning("Unknow voiced command '" + word + "'");
				}
			}
			else if(bypass.startsWith("npc_"))
			{
				int endOfId = bypass.indexOf('_', 5);
				String id;
				if(endOfId > 0)
				{
					id = bypass.substring(4, endOfId);
				}
				else
				{
					id = bypass.substring(4);
				}
				L2Object object = activeChar.getVisibleObject(Integer.parseInt(id));
				if(object != null && object.isNpc() && endOfId > 0 && activeChar.isInRange(object.getLoc(), L2Character.INTERACTION_DISTANCE))
				{
					activeChar.setLastNpc((L2NpcInstance) object);
					((L2NpcInstance) object).onBypassFeedback(activeChar, bypass.substring(endOfId + 1));
				}
			}
			// используется для перехода с арены на арену при обсервинге олимпиады
			else if(bypass.startsWith("oly_"))
			{
				if(!Config.ENABLE_OLYMPIAD_SPECTATING)
				{
					return;
				}
				// Временно отключено, глючит.
				if(Boolean.TRUE)
				{
					return;
				}
				if(!activeChar.inObserverMode())
				{
					_log.warning(activeChar.getName() + " possible cheater: tried to switch arena usind not standart method!");
					return;
				}
				int arenaId = Integer.parseInt(bypass.substring(4));
				OlympiadManager manager = Olympiad._manager;
				if(manager == null || manager.getOlympiadInstance(arenaId) == null)
				{
					return;
				}
				activeChar.switchOlympiadObserverArena(arenaId);
			}
			else if(bypass.startsWith("manor_menu_select?")) // Navigate throught Manor windows
			{
				L2Object object = activeChar.getTarget();
				if(object != null && object.isNpc())
				{
					((L2NpcInstance) object).onBypassFeedback(activeChar, bypass);
				}
			}
			else if(bypass.startsWith("multisell "))
			{
				L2Multisell.getInstance().SeparateAndSend(Integer.parseInt(bypass.substring(10)), activeChar, 0);
			}
			else if(bypass.startsWith("Quest "))
			{
				String p = bypass.substring(6).trim();
				int idx = p.indexOf(' ');
				if(idx < 0)
				{
					activeChar.processQuestEvent(p, "", npc);
				}
				else
				{
					activeChar.processQuestEvent(p.substring(0, idx), p.substring(idx).trim(), npc);
				}
			}
			else if(bypass.startsWith("_bbs"))
			{
				if(activeChar.isDead() ||
					activeChar.isAlikeDead() ||
					activeChar.isCastingNow() ||
					activeChar.isInCombat() ||
					activeChar.isAttackingNow() ||
					activeChar.isInOlympiadMode() ||
					activeChar.isInVehicle() ||
					activeChar.isFlying() ||
					activeChar.isInFlyingTransform()
					)
				{
					activeChar.sendMessage("Community нельзя использовать в данных условиях.");
					return;
				}
				mICommunityHandler mICommunityHandler = mCommunityHandler.getInstance().getHandler(bypass);
				if(mICommunityHandler != null)
				{
					mICommunityHandler.useHandler(activeChar.getObjectId(), bypass);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			String st = "Bad RequestBypassToServer: " + bypass;
			L2Object target = activeChar.getTarget();
			if(target != null && target.isNpc())
			{
				st = st + " via NPC #" + ((L2NpcInstance) target).getNpcId();
			}
			_log.log(Level.WARNING, st, e);
		}
	}

	private void comeHere(L2GameClient client)
	{
		L2Object obj = client.getActiveChar().getTarget();
		if(obj != null && obj.isNpc())
		{
			L2NpcInstance temp = (L2NpcInstance) obj;
			L2Player activeChar = client.getActiveChar();
			temp.setTarget(activeChar);
			temp.moveToLocation(activeChar.getLoc(), 0, true);
		}
	}

	private void playerHelp(int objectId, String path)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		String filename = "data/html/" + path;
		NpcHtmlMessage html = new NpcHtmlMessage(5);
		html.setFile(filename);
		player.sendPacket(html);
	}
}