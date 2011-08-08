package l2p.extensions.scripts;

import l2p.extensions.scripts.Scripts.ScriptClassAndMethod;
import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2Player;
import l2p.util.Strings;

public final class Events
{
	public static boolean onAction(L2Player player, L2Object obj, boolean shift)
	{
		if(shift)
		{
			if(player.getVarB("noShift"))
			{
				return false;
			}
			ScriptClassAndMethod handler = Scripts.onActionShift.get(obj.getL2ClassShortName());
			if(handler == null && obj.isNpc())
			{
				handler = Scripts.onActionShift.get("L2NpcInstance");
			}
			if(handler == null && obj.isSummon())
			{
				handler = Scripts.onActionShift.get("L2SummonInstance");
			}
			if(handler == null && obj.isPet())
			{
				handler = Scripts.onActionShift.get("L2PetInstance");
			}
            if(handler == null && obj.isDoor())
			{
				handler = Scripts.onActionShift.get("L2DoorInstance");
			}
            if(handler == null && obj.isItem())
			{
				handler = Scripts.onActionShift.get("L2ItemInstance");
			}
			if(handler == null)
			{
				return false;
			}
			return Strings.parseBoolean(player.callScripts(handler.scriptClass, handler.method, new Object[] {player, obj}));
		}
		else
		{
			ScriptClassAndMethod handler = Scripts.onAction.get(obj.getL2ClassShortName());
			if(handler == null)
			{
				return false;
			}
			return Strings.parseBoolean(player.callScripts(handler.scriptClass, handler.method, new Object[] {player, obj}));
		}
	}
}