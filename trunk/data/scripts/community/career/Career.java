package community.career;

import community.Community;
import community.GeneratePage;
import javolution.text.TextBuilder;
import l2p.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.base.ClassId;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;
import l2p.gameserver.modules.option.mOption;
import l2p.gameserver.tables.ItemTable;
import l2p.gameserver.templates.L2Item;
import l2p.util.Util;

import java.util.StringTokenizer;

/**
 * User: Shaitan
 * Date: 08.11.2010
 * Time: 10:19:55
 */
public class Career implements mICommunityHandler
{
	public void onLoad()
	{
		if(!Community.communityCareer)
		{
			return;
		}
		mCommunityHandler.getInstance().addHandler(this);
	}

	public void useHandler(int objectId, String command)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(command.equals("_bbscareer"))
		{
			Community.getInstance().show(objectId, GeneratePage.addToTemplate(showCareer(objectId)));
		}
		else if(command.startsWith("_bbscareerclass"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			short val = Short.parseShort(st.nextToken());
			int price = Integer.parseInt(st.nextToken());
			if(mOption.price(player, Config.CLASS_MASTERS_PRICE_ITEM, price))
			{
				changeClass(objectId, val);
				Community.getInstance().show(objectId, GeneratePage.addToTemplate(showCareer(objectId)));
			}
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbscareer",
				"_bbscareerclass"
			};
		return s;
	}

	private String showCareer(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		ClassId classId = player.getClassId();
		int jobLevel = classId.getLevel();
		int level = player.getLevel();
		TextBuilder html = new TextBuilder("");
		html.append("<br>");
		html.append("<table width=600>");
		html.append("<tr><td>");
		if(Config.ALLOW_CLASS_MASTERS_LIST.isEmpty() || !Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			jobLevel = 4;
		}
		if((level >= 20 && jobLevel == 1 || level >= 40 && jobLevel == 2 || level >= 76 && jobLevel == 3) && Config.ALLOW_CLASS_MASTERS_LIST.contains(jobLevel))
		{
			L2Item item = ItemTable.getInstance().getTemplate(Config.CLASS_MASTERS_PRICE_ITEM);
			html.append("Вы должны заплатить: <font color=\"LEVEL\">");
			html.append(Util.formatAdena(Config.CLASS_MASTERS_PRICE_LIST[jobLevel])).append("</font> <font color=\"LEVEL\">").append(item.getName()).append("</font> для смены профессии<br>");
			html.append("<center><table width=600><tr>");
			for(ClassId cid : ClassId.values())
			{
				if(cid == ClassId.inspector)
				{
					continue;
				}
				if(cid.childOf(classId) && cid.level() == classId.level() + 1)
				{
					html.append("<td><center><button value=\"").append(cid.name()).append("\" action=\"bypass -h _bbscareerclass ").append(cid.getId()).append(" ").append(Config.CLASS_MASTERS_PRICE_LIST[jobLevel]).append("\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></center></td>");
				}
			}
			html.append("</tr></table></center>");
			html.append("</td>");
			html.append("</tr>");
			html.append("</table>");
		}
		else
		{
			switch(jobLevel)
			{
				case 1:
					html.append("Приветствую <font color=F2C202>" + player.getName() + "</font> ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того чтобы сменить вашу профессию вы должны достичь: <font color=F2C202>20-го уровня</font><br>");
					break;
				case 2:
					html.append("Приветствую <font color=F2C202>" + player.getName() + "</font> ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того чтобы сменить вашу профессию вы должны достичь: <font color=F2C202>40-го уровня</font><br>");
					break;
				case 3:
					html.append("Приветствую <font color=F2C202>" + player.getName() + "</font> ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для того чтобы сменить вашу профессию вы должны достичь: <font color=F2C202>76-го уровня</font><br>");
					break;
				case 4:
					html.append("Приветствую <font color=F2C202>" + player.getName() + "</font> ваша текущая профессия <font color=F2C202>" + player.getClassId().name() + "</font><br>");
					html.append("Для вас больше нет доступных профессий, либо Класс мастер в данный момент недоступен.<br>");
					if(level >= 76)
					{
						html.append("Вы достигли <font color=F2C202>76-го уровня</font> активация сабклассов теперь доступна<br>");
					}
					break;
			}
		}
		return html.toString();
	}

	public static void changeClass(int objectId, int val)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player.getClassId().getLevel() == 3)
		{
			player.sendPacket(Msg.YOU_HAVE_COMPLETED_THE_QUEST_FOR_3RD_OCCUPATION_CHANGE_AND_MOVED_TO_ANOTHER_CLASS_CONGRATULATIONS); // для 3 профы
		}
		else
		{
			player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);
		}
		player.setClassId(val, false);
		player.broadcastUserInfo(true);
	}
}