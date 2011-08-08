package commands.admin;

import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.handler.AdminCommandHandler;
import l2p.gameserver.handler.IAdminCommandHandler;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2World;
/*
Done by Dexter
Shaitan Team
*/

public class AdminResetSkillCoolTime implements IAdminCommandHandler, ScriptFile
{
	private static enum Commands
	{
		admin_reset_skill_cool
	}

	public boolean useAdminCommand(Enum comm, String[] wordList, String fullString, L2Player activeChar)
	{
		Commands command = (Commands) comm;
		switch(command)
		{
			case admin_reset_skill_cool:
				final L2Player player;
				if(wordList.length == 1)
				{
					// Обработка по таргету
					L2Object target = activeChar.getTarget();
					if(target == null)
					{
						activeChar.sendMessage("Select character or specify player name.");
						break;
					}
					if(!target.isPlayer())
					{
						activeChar.sendPacket(Msg.INVALID_TARGET);
						break;
					}
					player = (L2Player) target;
				}
				else
				{
					// Обработка по нику
					player = L2World.getPlayer(wordList[1]);
					if(player == null)
					{
						activeChar.sendMessage("Character " + wordList[1] + " not found in game.");
						break;
					}
				}
				player.resetSkillsReuse();
				player.sendMessage("Your skills cool time have been reseted by GM.");
		}
		return true;
	}

	public Enum[] getAdminCommandEnum()
	{
		return Commands.values();
	}

	public void onLoad()
	{
		AdminCommandHandler.getInstance().registerAdminCommandHandler(this);
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}