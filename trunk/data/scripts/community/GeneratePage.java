package community;

import l2p.Config;
import l2p.common.ThreadPoolManager;
import l2p.database.DatabaseUtils;
import l2p.database.FiltredPreparedStatement;
import l2p.database.L2DatabaseFactory;
import l2p.database.ThreadConnection;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.util.Files;

import java.sql.ResultSet;

/**
 * User: Shaitan
 * Date: 29.12.10
 * Time: 18:52
 */
public class GeneratePage
{
	private static GeneratePage ourInstance = new GeneratePage();

	public static GeneratePage getInstance()
	{
		return ourInstance;
	}

	public GeneratePage()
	{
		StaticPage.setPageTemplate(generateTemplate());
		StaticPage.pageMain = addToTemplate(Files.read("custom/community/main.htm"));
		StaticPage.pageShop = addToTemplate(Files.read("custom/community/shop.htm"));
		StaticPage.pageService = addToTemplate(addService());
		StaticPage.pageEvents = addToTemplate(addEvent());
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new generateStatistic(), 1000, 10 * 60 * 1000);
	}
	//--------------------------------------------------------------------------------------------------------------

	public static String generateTemplate()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<html>").append("<body>").append("<center>");
		sb.append("<table width=751><tr>");
		sb.append("<td fixwidth=100 valign=top><table width=100>");
		sb.append(GenerateElement.buttonTDTR("Главная", "_bbshome", 100, 25));
		sb.append(Community.communityShop ? GenerateElement.buttonTDTR("Магазин", "_bbsshop", 100, 25) : "");
		sb.append(Community.communityBuffer ? GenerateElement.buttonTDTR("Баффер", "_bbsbaffer", 100, 25) : "");
		sb.append(Community.communityService ? GenerateElement.buttonTDTR("Сервисы", "_bbsservice", 100, 25) : "");
		sb.append(GenerateElement.buttonTDTR("Эвенты", "_bbsevents", 100, 25));
		sb.append(Community.communityStatistic ? GenerateElement.buttonTDTR("Статистика", "_bbsstatistic", 100, 25) : "");
		sb.append(Community.communityCareer ? GenerateElement.buttonTDTR("Карьера", "_bbscareer", 100, 25) : "");
		sb.append(Community.communityTeleport ? GenerateElement.buttonTDTR("Телепорт", "_bbsteleport", 100, 25) : "");
		sb.append("</table></td>");
		sb.append("<td fixwidth=1 valign=top>");
		sb.append(GenerateElement.line(1, 495));
		sb.append("</td>");
		sb.append("<td fixwidth=650 valign=top><br>");
		sb.append("%main%");
		sb.append("</td>");
		sb.append("</tr></table>");
		sb.append("</center>").append("</body>").append("</html>");
		return sb.toString();
	}

	public static String addToTemplate(String s)
	{
		return StaticPage.getPageTemplate().replace("%main%", s);
	}
	//--------------------------------------------------------------------------------------------------------------
    private static String addEvent()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<table width=650>");
        sb.append(GenerateElement.buttonTDTR("Регистрация TvT", "scripts_events.TvT.TvT:addPlayer", 200, 25));
        sb.append(GenerateElement.buttonTDTR("Регистраци CtF", "scripts_events.CtF.CtF:addPlayer", 200, 25));
        sb.append(GenerateElement.buttonTDTR("Регистрация Last Hero", "scripts_events.lastHero.LastHero:addPlayer", 200, 25));
        sb.append("</table>");
        return sb.toString();
    }
	private static String addService()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<table width=650>");
		sb.append(Config.SERVICES_NOBLESS_SELL_ENABLED ? GenerateElement.buttonTDTR("Ноблес", "scripts_services.NoblessSell:get", 200, 25) : "");
		sb.append(Config.SERVICES_RATE_BONUS_ENABLED ? GenerateElement.buttonTDTR("Премиум Аккаунт", "scripts_services.RateBonus:list", 200, 25) : "");
		sb.append(Config.SERVICES_EXPAND_CWH_ENABLED ? GenerateElement.buttonTDTR("Расширение кланового склада", "scripts_services.ExpandCWH:show", 200, 25) : "");
		sb.append(Config.SERVICES_EXPAND_INVENTORY_ENABLED ? GenerateElement.buttonTDTR("Расширение инвентаря", "scripts_services.ExpandInventory:show", 200, 25) : "");
		sb.append(Config.SERVICES_EXPAND_WAREHOUSE_ENABLED ? GenerateElement.buttonTDTR("Расширение личного склада", "scripts_services.ExpandWarhouse:show", 200, 25) : "");
		sb.append(Config.SERVICES_CHANGE_NICK_ENABLED ? GenerateElement.buttonTDTR("Смена ника", "scripts_services.Rename:rename_page", 200, 25) : "");
		sb.append(Config.SERVICES_CHANGE_SEX_ENABLED ? GenerateElement.buttonTDTR("Смена пола", "scripts_services.Rename:changesex_page", 200, 25) : "");
		sb.append(Config.SERVICES_CHANGE_NICK_COLOR_ENABLED ? GenerateElement.buttonTDTR("Цвет ника", "scripts_services.NickColor:list", 200, 25) : "");
		sb.append(Config.SERVICES_SEPARATE_SUB_ENABLED ? GenerateElement.buttonTDTR("Отделить саб", "scripts_services.Rename:separate_page", 200, 25) : "");
		sb.append(Config.SERVICES_CHANGE_BASE_ENABLED ? GenerateElement.buttonTDTR("Сменить класс", "scripts_services.Rename:changebase_page", 200, 25) : "");
		sb.append(Config.SERVICES_DELEVEL_ENABLED ? GenerateElement.buttonTDTR("Понизить уровень", "scripts_services.Delevel:delevel_page", 200, 25) : "");
		sb.append("</table>");
		return sb.toString();
	}
	//--------------------------------------------------------------------------------------------------------------

	private static class generateStatistic implements Runnable
	{
		public void run()
		{
			StaticPage.pageStatistic = addToTemplate(addStatisticMenu(addStatisticPvP()));
			StaticPage.pageStatisticPvP = StaticPage.pageStatistic;
			StaticPage.pageStatisticPK = addToTemplate(addStatisticMenu(addStatisticPK()));
		}
	}

	private static String addStatisticMenu(String s)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<center><table width=200>");
		sb.append("<tr>");
		sb.append("<td FIXWIDTH = 100>" + GenerateElement.button("Топ PVP", "_bbsstatistic_pvp", 100, 25) + "</td>");
		sb.append("<td FIXWIDTH = 100>" + GenerateElement.button("Топ PK", "_bbsstatistic_pk", 100, 25) + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<table width=200>");
		sb.append("<tr><td><center>Обновляется раз в 10 минут</center></td></tr>");
		sb.append("<tr><td><center><font color=0093FF>Онлайн: " + L2ObjectsStorage.getAllPlayersCount() + "</font></center></td></tr>");
		sb.append("</table></center>");
		sb.append(s);
		return sb.toString();
	}

	private static String addStatisticPvP()
	{
		ThreadConnection tc = null;
		FiltredPreparedStatement fps = null;
		ResultSet rs = null;
		try
		{
			tc = L2DatabaseFactory.getInstance().getConnection();
			fps = tc.prepareStatement("SELECT * FROM characters ORDER BY pvpkills DESC LIMIT 20;");
			rs = fps.executeQuery();
			StringBuilder sb = new StringBuilder();
			sb.append("<table width=650>");
			sb.append("<tr><td><center>ТОП 20 PVP");
			sb.append("<img src=L2UI.SquareWhite width=450 height=1>");
			sb.append("<table width=450 bgcolor=CCCCCC>");
			sb.append("<tr>");
			sb.append("<td width=250>Ник</td>");
			sb.append("<td width=50>Пол</td>");
			sb.append("<td width=100>Время в игре</td>");
			sb.append("<td width=50>PK</td>");
			sb.append("<td width=50><font color=00CC00>PVP</font></td>");
			sb.append("<td width=100>Статус</td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<img src=L2UI.SquareWhite width=450 height=1>");
			sb.append("<table width=450>");
			while(rs.next())
			{
				int PlayerId = rs.getInt("obj_Id");
				String ChName = rs.getString("char_name");
				int ChSex = rs.getInt("sex");
				int ChGameTime = rs.getInt("onlinetime");
				int ChPk = rs.getInt("pkkills");
				int ChPvP = rs.getInt("pvpkills");
				int ChOnOff = rs.getInt("online");
				String OnOff;
				String color;
				String sex;
				sex = ChSex == 1 ? "Ж" : "М";
				if(ChOnOff == 1)
				{
					OnOff = "Онлайн";
					color = "00CC00";
				}
				else
				{
					OnOff = "Оффлайн";
					color = "D70000";
				}
				sb.append("<tr>");
				sb.append("<td width=250>" + ChName + "</td>");
				sb.append("<td width=50>" + sex + "</td>");
				sb.append("<td width=100>" + OnlineTime(ChGameTime) + "</td>");
				sb.append("<td width=50>" + ChPk + "</td>");
				sb.append("<td width=50><font color=00CC00>" + ChPvP + "</font></td>");
				sb.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			sb.append("</center></td></tr></table>");
			return sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtils.closeDatabaseCSR(tc, fps, rs);
		}
		return null;
	}

	private static String addStatisticPK()
	{
		ThreadConnection tc = null;
		FiltredPreparedStatement fps = null;
		ResultSet rs = null;
		try
		{
			tc = L2DatabaseFactory.getInstance().getConnection();
			fps = tc.prepareStatement("SELECT * FROM characters ORDER BY pkkills DESC LIMIT 20;");
			rs = fps.executeQuery();
			StringBuilder sb = new StringBuilder();
			sb.append("<table width=650>");
			sb.append("<tr><td><center>ТОП 20 PK");
			sb.append("<img src=L2UI.SquareWhite width=450 height=1>");
			sb.append("<table width=450 bgcolor=CCCCCC>");
			sb.append("<tr>");
			sb.append("<td width=250>Ник</td>");
			sb.append("<td width=50>Пол</td>");
			sb.append("<td width=100>Время в игре</td>");
			sb.append("<td width=50><font color=00CC00>PK</font></td>");
			sb.append("<td width=50>PVP</td>");
			sb.append("<td width=100>Статус</td>");
			sb.append("</tr>");
			sb.append("</table>");
			sb.append("<img src=L2UI.SquareWhite width=450 height=1>");
			sb.append("<table width=450>");
			while(rs.next())
			{
				int PlayerId = rs.getInt("obj_Id");
				String ChName = rs.getString("char_name");
				int ChSex = rs.getInt("sex");
				int ChGameTime = rs.getInt("onlinetime");
				int ChPk = rs.getInt("pkkills");
				int ChPvP = rs.getInt("pvpkills");
				int ChOnOff = rs.getInt("online");
				String OnOff;
				String color;
				String sex;
				sex = ChSex == 1 ? "Ж" : "М";
				if(ChOnOff == 1)
				{
					OnOff = "Онлайн";
					color = "00CC00";
				}
				else
				{
					OnOff = "Оффлайн";
					color = "D70000";
				}
				sb.append("<tr>");
				sb.append("<td width=250>" + ChName + "</td>");
				sb.append("<td width=50>" + sex + "</td>");
				sb.append("<td width=100>" + OnlineTime(ChGameTime) + "</td>");
				sb.append("<td width=50><font color=00CC00>" + ChPk + "</font></td>");
				sb.append("<td width=50>" + ChPvP + "</td>");
				sb.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			sb.append("</center></td></tr></table>");
			return sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtils.closeDatabaseCSR(tc, fps, rs);
		}
		return null;
	}

	private static String OnlineTime(int time)
	{
		long onlinetimeH;
		int onlinetimeM;
		if(time / 60 / 60 - 0.5 <= 0)
		{
			onlinetimeH = 0;
		}
		else
		{
			onlinetimeH = Math.round((time / 60 / 60) - 0.5);
		}
		onlinetimeM = Math.round(((time / 60 / 60) - onlinetimeH) * 60);
		return "" + onlinetimeH + " ч. " + onlinetimeM + " м.";
	}
}