package community.teleport;

import community.Community;
import community.GenerateElement;
import community.GeneratePage;
import javolution.util.FastList;
import l2p.Config;
import l2p.database.DatabaseUtils;
import l2p.database.FiltredPreparedStatement;
import l2p.database.L2DatabaseFactory;
import l2p.database.ThreadConnection;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Zone.ZoneType;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;
import l2p.gameserver.modules.community.teleport.TeleportPoint;
import l2p.gameserver.modules.option.mOption;
import l2p.util.Files;
import l2p.util.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * User: Shaitan
 * Date: 09.12.2010
 * Time: 11:23:28
 */
public class Teleport implements mICommunityHandler
{
	class GroupTeleport
	{
		String name;
		String path;

		GroupTeleport(String name, String path)
		{
			this.name = name;
			this.path = path;
		}
	}

	private static ArrayList<GroupTeleport> GroupTeleportMain = new ArrayList<GroupTeleport>();
	private static ArrayList<GroupTeleport> GroupTeleportPremium = new ArrayList<GroupTeleport>();

	public void onLoad()
	{
		if(!Community.communityTeleport)
		{
			return;
		}
		Properties community = mOption.loadFile("./config/custom/community.ini");
		String[] GroupTeleportTemp = mOption.getString(community, "GroupTeleport").split(",");
		for(int i = 0; i < GroupTeleportTemp.length; i += 2)
		{
			GroupTeleportMain.add(new GroupTeleport(GroupTeleportTemp[i], GroupTeleportTemp[i + 1]));
		}
		String[] GroupTeleportPremiumTemp = mOption.getString(community, "PremiumGroupTeleport").split(",");
		for(int i = 0; i < GroupTeleportPremiumTemp.length; i += 2)
		{
			GroupTeleportPremium.add(new GroupTeleport(GroupTeleportPremiumTemp[i], GroupTeleportPremiumTemp[i + 1]));
		}
		mCommunityHandler.getInstance().addHandler(this);
	}

	public void useHandler(int objectId, String command)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(command.equalsIgnoreCase("_bbsteleport"))
		{
			Community.getInstance().show(objectId, GeneratePage.addToTemplate(showTeleport(objectId, false, null)));
		}
		else if(command.startsWith("_bbsteleport_list"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			Community.getInstance().show(objectId, GeneratePage.addToTemplate(showTeleport(objectId, true, "custom/community/" + st.nextToken())));
		}
		else if(command.startsWith("_bbsteleport_goto"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int z = Integer.parseInt(st.nextToken());
			player.teleToLocation(x, y, z);
		}
		else if(command.startsWith("_bbsteleport_save"))
		{
			if(player.isInZone(ZoneType.Castle) ||
				player.isInZone(ZoneType.Fortress) ||
				player.isInZone(ZoneType.ClanHall) ||
				player.isInZone(ZoneType.OlympiadStadia) ||
				player.isInZone(ZoneType.siege_residense) ||
				player.isInZone(ZoneType.CastleDefenderSpawn) ||
				player.isInZone(ZoneType.Siege) ||
				player.isInZone(ZoneType.epic) ||
				player.isInZone(ZoneType.no_escape) ||
				player.isInZone(ZoneType.no_summon))
			{
				player.sendMessage("Не подходящие условия сохранения точки ТП.");
				return;
			}
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			if(player.teleportPoints.size() >= 10)
			{
				player.sendMessage("Можно сохранить не более 10 точек.");
				return;
			}
			try
			{
				String name = st.nextToken();
				if(!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE))
				{
					player.sendMessage("Название должно состоять только из английских букв.");
					return;
				}
				String xyz = player.getX() + " " + player.getY() + " " + player.getZ();
				player.setVar("bbsteleport_" + name, xyz);
				player.teleportPoints.add(new TeleportPoint(name, xyz));
			}
			catch(Exception e)
			{
				return;
			}
			Community.getInstance().show(objectId, GeneratePage.addToTemplate(showTeleport(objectId, false, null)));
		}
		else if(command.startsWith("_bbsteleport_del"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String name = st.nextToken();
			player.unsetVar("bbsteleport_" + name);
			player.teleportPoints.remove(player.getTeleportPointByName(name));
			Community.getInstance().show(objectId, GeneratePage.addToTemplate(showTeleport(objectId, false, null)));
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbsteleport",
				"_bbsteleport_list",
				"_bbsteleport_goto",
				"_bbsteleport_save",
				"_bbsteleport_del"
			};
		return s;
	}

	private String showTeleport(int objectId, boolean file, String path)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		String s = "<table width=650>";
		s += "<tr>";
		s += "<td fixwidth=300 valign=top>";
		s += "<table>";
		//======================================================================================================
		if(!file)
		{
			s += "<tr>";
			s += "<td FIXWIDTH = 150>";
			s += "<font color=1E90FF>Телепорт:</font>";
			s += "</td>";
			s += "<td FIXWIDTH = 150>";
			s += "<font color=1E90FF>Премиум Телепорт:</font>";
			s += "</td>";
			s += "</tr>";
			s += "</table>";
			//======================================================================================================
			s += "<table><tr>";
			s += "<td FIXWIDTH = 150 VALIGN=top>";
			s += list();
			s += "</td>";
			s += "<td FIXWIDTH = 150 VALIGN=top>";
			if(player.getBonus().RATE_XP > 1)
			{
				s += premiumList();
			}
			else
			{
				s += "Необходимо приобрести премиум аккаунт";
			}
			s += "</td>";
			s += "</tr>";
		}
		else
		{
			s += listView(path);
		}
		//======================================================================================================
		s += "</table>";
		s += "</td>";
		s += "<td fixwidth=300 valign=top>";
		s += listMy(objectId);
		s += "</td>";
		s += "</tr>";
		s += "</table>";
		return s;
	}

	private static String list()
	{
		String s = "";
		s += "<table>";
		for(GroupTeleport gp : GroupTeleportMain)
		{
			s += "<tr>";
			s += GenerateElement.buttonTD(gp.name, "_bbsteleport_list " + gp.path, 130, 25);
			s += "</tr>";
		}
		s += "</table>";
		return s;
	}

	private static String premiumList()
	{
		String s = "";
		s += "<table>";
		for(GroupTeleport gp : GroupTeleportPremium)
		{
			s += "<tr>";
			s += GenerateElement.buttonTD(gp.name, "_bbsteleport_list " + gp.path, 130, 25);
			s += "</tr>";
		}
		s += "</table>";
		return s;
	}

	private static String listView(String file)
	{
		String s = "";
		s += "<tr><td><font color=1E90FF>Список:</font><br></td></tr>";
		s += Files.read(file);
		return s;
	}

	private static String listMy(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		String s = "<table><tr>";
		s += "<td FIXWIDTH = 300>";
		s += "<font color=1E90FF>Введите имя новой точки:</font>";
		s += "<edit var=\"name\" width=200>";
		s += GenerateElement.button("Сохранить координаты", "_bbsteleport_save $name", 200, 25);
		s += "</td>";
		s += "</tr></table>";
		s += "<table>";
		for(TeleportPoint point : player.teleportPoints)
		{
			s += "<tr><td FIXWIDTH = 150>";
			s += "<button value=\"" + point.getName() + "\" action=\"bypass -h _bbsteleport_goto " + point.getXYZ() + "\" width=150 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
			s += "</td>";
			s += "<td FIXWIDTH = 50>";
			s += "<button value=\"Удалить\" action=\"bypass -h _bbsteleport_del " + point.getName() + "\" width=50 height=25 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">";
			s += "</td></tr>";
		}
		s += "</table>";
		return s;
	}

	public static void open(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		String SQL = "SELECT * FROM character_variables WHERE obj_Id = ?";
		ThreadConnection tc = null;
		FiltredPreparedStatement fps = null;
		ResultSet rs = null;
		try
		{
			tc = L2DatabaseFactory.getInstance().getConnection();
			fps = tc.prepareStatement(SQL);
			fps.setInt(1, player.getObjectId());
			rs = fps.executeQuery();
			while(rs.next())
			{
				String name = rs.getString("name");
				if(name.contains("bbsteleport_"))
				{
					String value = rs.getString("value");
					TeleportPoint point = new TeleportPoint(name.substring(12), value);
					player.teleportPoints.add(point);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DatabaseUtils.closeDatabaseCSR(tc, fps, rs);
		}
	}

	public static void OnPlayerEnter(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player == null)
		{
			return;
		}
		if(player.teleportPoints == null)
		{
			player.teleportPoints = new FastList<TeleportPoint>();
			open(player.getObjectId());
		}
	}
}