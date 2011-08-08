package community.buffer;

import community.Community;
import community.GenerateElement;
import community.GeneratePage;
import javolution.util.FastList;
import l2p.Config;
import l2p.database.DatabaseUtils;
import l2p.database.FiltredPreparedStatement;
import l2p.database.L2DatabaseFactory;
import l2p.database.ThreadConnection;
import l2p.gameserver.model.L2Effect;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.L2Skill;
import l2p.gameserver.modules.community.buffer.Buff;
import l2p.gameserver.modules.community.buffer.OneScheme;
import l2p.gameserver.modules.community.mCommunityHandler;
import l2p.gameserver.modules.community.mICommunityHandler;
import l2p.gameserver.modules.option.mOption;
import l2p.gameserver.skills.Env;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.tables.SkillTable;
import l2p.util.Util;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import static java.lang.Thread.sleep;

/**
 * User: Shaitan
 * Date: 07.11.2010
 * Time: 9:13:56
 */
public class Buffer implements mICommunityHandler
{
	public static boolean buffer;
	private static int buffer_max_schemes;
	private static int buffer_max_buffs;
	private static String[] priceOneBuff;
	private static String[] priceOneBuffScheme;
	private static OneScheme normalScheme = new OneScheme("Normal");
	private static ArrayList<Buff> staticBuffs = new ArrayList<Buff>();

	private static Buff getBuff(int id)
	{
		for(Buff buff : staticBuffs)
		{
			if(buff.getId() == id)
			{
				return buff;
			}
		}
		return null;
	}

	public void onLoad()
	{
		if(!Community.communityBuffer)
		{
			return;
		}
		Properties community = mOption.loadFile("./config/custom/community.ini");
		buffer_max_schemes = mOption.getInt(community, "buffer_max_schemes");
		buffer_max_buffs = mOption.getInt(community, "buffer_max_buffs");
		String[] buffer_buffs = mOption.getString(community, "buffer_buffs").split(",");
		priceOneBuff = mOption.getString(community, "priceOneBuff").split(",");
		priceOneBuffScheme = mOption.getString(community, "priceOneBuffScheme").split(",");
		mCommunityHandler.getInstance().addHandler(this);
		for(int i = 0; i < buffer_buffs.length; i += 2)
		{
			Buff buff = new Buff(Integer.parseInt(buffer_buffs[i]), Integer.parseInt(buffer_buffs[i + 1]));
			normalScheme.buffs.add(buff);
			staticBuffs.add(buff);
		}
	}

	private static int[][] warrior = {
		{7057, 1}, // Greater Might
		{4345, 3}, // Might
		{4344, 3}, // Shield
		{4349, 2}, // Magic Barrier
		{4342, 2}, // Wind Walk
		{4347, 6}, // Bless the Body
		{4357, 2}, // Haste
		{4359, 3}, // Focus
		{4358, 3}, // Guidance
		{4360, 3}, // Death Whisper
		{4354, 4}, // Vampiric Rage
		{4346, 4} // Mental Shield
	};
	private static int[][] mage = {
		{7059, 1}, // Wild Magic
		{4356, 3}, // Empower
		{4355, 3}, // Acumen
		{4352, 1}, // Berserker Spirit
		{4346, 4}, // Mental Shield
		{4351, 6}, // Concentration
		{4342, 2}, // Wind Walk
		{4347, 6}, // Bless the Body
		{4348, 6}, // Bless the Soul
		{4344, 3}, // Shield
		{7060, 1}, // Clarity
		{4350, 4}, // Resist Shock
	};

	public void useHandler(int objectId, String command) throws InterruptedException {
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(command.equals("_bbsbaffer"))
		{
			Community.getInstance().show(objectId, showBuffer(objectId, null));
		}
		else if(command.startsWith("_bbsbaffer_create_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			try
			{
				String name = st.nextToken();
				if(player.schemes.size() >= buffer_max_schemes)
				{
					player.sendMessage("Максимальное количество схем: " + buffer_max_schemes);
					return;
				}
				if(!Util.isMatchingRegexp(name, Config.CNAME_TEMPLATE))
				{
					player.sendMessage("Название должно состоять только из английских букв.");
					return;
				}
				if(player.schemes.size() > 0)
				{
					for(OneScheme oneScheme : player.schemes)
					{
						if(oneScheme.getName().equals(name))
						{
							player.sendMessage("Схема с таким именем уже существует.");
							return;
						}
					}
				}
				player.schemes.add(new OneScheme(name));
				player.setVar("Buf_" + name, "");
			}
			catch(Exception e)
			{
				player.sendMessage("Введите имя схемы.");
			}
			Community.getInstance().show(objectId, showBuffer(objectId, null));
		}
		else if(command.startsWith("_bbsbaffer_select_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			Community.getInstance().show(objectId, showBuffer(objectId, st.nextToken()));
		}
		else if(command.startsWith("_bbsbaffer_edit_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String name = st.nextToken();
			int page = Integer.parseInt(st.nextToken());
			boolean add = Boolean.parseBoolean(st.nextToken());
			boolean del = Boolean.parseBoolean(st.nextToken());
			boolean run = Boolean.parseBoolean(st.nextToken());
			Community.getInstance().show(objectId, editScheme(objectId, page, player.getOneSchemeByName(name), add, del, run));
		}
		else if(command.startsWith("_bbsbaffer_del_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String name = st.nextToken();
			for(OneScheme oneScheme : player.schemes)
			{
				if(oneScheme.getName().equals(name))
				{
					player.schemes.remove(oneScheme);
					player.unsetVar("Buf_" + name);
					break;
				}
			}
			Community.getInstance().show(objectId, showBuffer(objectId, null));
		}
		else if(command.startsWith("_bbsbaffer_buff_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String name = st.nextToken();
			String type = st.nextToken();
			if(!mOption.price(player, Integer.parseInt(priceOneBuffScheme[0]), Integer.parseInt(priceOneBuffScheme[1])))
			{
				return;
			}
			for(Buff buff : player.getOneSchemeByName(name).buffs)
			{
				if(type.equals("Player"))
				{
					buff(buff.getId(), buff.getLevel(), objectId, false);
				}
				else if(type.equals("Pet") && player.getPet() != null)
				{
					buff(buff.getId(), buff.getLevel(), objectId, true);
				}
			}
			sleep(10000L);
		}
		else if(command.startsWith("_bbsbaffer_adddelrun_buff_scheme"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			String name = st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			int page = Integer.parseInt(st.nextToken());
			boolean add = Boolean.parseBoolean(st.nextToken());
			boolean del = Boolean.parseBoolean(st.nextToken());
			boolean run = Boolean.parseBoolean(st.nextToken());
			OneScheme oneScheme = name.equalsIgnoreCase("Normal") ? null : player.getOneSchemeByName(name);
			if(oneScheme != null)
			{
				if(add)
				{
					if(oneScheme.buffs.size() < buffer_max_buffs)
					{
						for(Buff buff : oneScheme.buffs)
						{
							if(buff.getId() == getBuff(id).getId())
							{
								Community.getInstance().show(objectId, editScheme(objectId, page, oneScheme, add, del, run));
								return;
							}
						}
						oneScheme.buffs.add(getBuff(id));
					}
					else
					{
						player.sendMessage("Максимальное количество баффов в схеме: " + buffer_max_buffs);
					}
				}
				else if(del)
				{
					if(oneScheme != null)
					{
						for(Buff buff : oneScheme.buffs)
						{
							if(buff.getId() == id)
							{
								oneScheme.buffs.remove(buff);
								break;
							}
						}
					}
				}
				else if(run)
				{
					if(!mOption.price(player, Integer.parseInt(priceOneBuff[0]), Integer.parseInt(priceOneBuff[1])))
					{
						return;
					}
					for(Buff buff : staticBuffs)
					{
						if(id == buff.getId())
						{
							buff(buff.getId(), buff.getLevel(), objectId, false);
							break;
						}
					}
				}
				Community.getInstance().show(objectId, editScheme(objectId, page, oneScheme, add, del, run));
			}
			else
			{
				if(!mOption.price(player, Integer.parseInt(priceOneBuffScheme[0]), Integer.parseInt(priceOneBuffScheme[1])))
				{
					return;
				}
				for(Buff buff : staticBuffs)
				{
					if(id == buff.getId())
					{
						buff(buff.getId(), buff.getLevel(), objectId, false);
						break;
					}
				}
				Community.getInstance().show(objectId, normalBuffer(page));
			}
		}
		else if(command.startsWith("_bbsbaffer_group_baff"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			int id = Integer.parseInt(st.nextToken());
			String type = st.nextToken();
			if(!mOption.price(player, Integer.parseInt(priceOneBuffScheme[0]), Integer.parseInt(priceOneBuffScheme[1])))
			{
				return;
			}
			for(int i = 0; i < (id == 1 ? warrior.length : mage.length); i++)
			{
				if(type.equals("Player"))
				{
					buff((id == 1 ? warrior : mage)[i][0], (id == 1 ? warrior : mage)[i][1], objectId, false);
				}
				else if(type.equals("Pet") && player.getPet() != null)
				{
					buff((id == 1 ? warrior : mage)[i][0], (id == 1 ? warrior : mage)[i][1], objectId, true);
				}
			}
			sleep(10000L);
		}
		else if(command.startsWith("_bbsbaffer_normal"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			int page = Integer.parseInt(st.nextToken());
			Community.getInstance().show(objectId, normalBuffer(page));
		}
	}

	public String[] getHandlerList()
	{
		String[] s =
			{
				"_bbsbaffer",
				"_bbsbaffer_create_scheme",
				"_bbsbaffer_select_scheme",
				"_bbsbaffer_edit_scheme",
				"_bbsbaffer_del_scheme",
				"_bbsbaffer_buff_scheme",
				"_bbsbaffer_adddelrun_buff_scheme",
				"_bbsbaffer_group_baff",
				"_bbsbaffer_normal"
			};
		return s;
	}

	private String showBuffer(int objectId, String scheme_name)
	{
		String s = "<table width=650>";
		s += "<tr>";
		s += "<td fixwidth=300 valign=top>";
		s += "<font color=3293F3>Кого бафать:</font>";
		s += "<combobox width=145 var=\"type\" list=\"Player;Pet\">";
		s += "<br>";
		s += GenerateElement.button("Набор для воина", "_bbsbaffer_group_baff 1 $type", 250, 25);
		s += GenerateElement.button("Набор для мага", "_bbsbaffer_group_baff 2 $type", 250, 25);
		s += "<br>";
		s += "<font color=3293F3>Введите название нового набора:</font>";
		s += "<br1><font color=LEVEL>Имя должно состоять из одного слова</font>";
		s += "<edit var=\"name\" width=250>";
		s += GenerateElement.button("Создать свой набор", "_bbsbaffer_create_scheme $name", 250, 25);
		s += "<font color=3293F3>Простой баффер:</font>";
		s += GenerateElement.button("Для игрока", "_bbsbaffer_normal 1", 250, 25);
		//s += Community.getInstance().button("Для питомца", "_bbsbaffer_normal 1 pet", 250, 25);
		s += "</td>";
		s += "<td fixwidth=300 valign=top>";
		s += "<font color=3293F3>Мои наборы:</font>";
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		for(OneScheme oneScheme : player.schemes)
		{
			s += GenerateElement.button(oneScheme.getName(), "_bbsbaffer_select_scheme " + oneScheme.getName(), 250, 25);
			if(oneScheme.getName().equals(scheme_name))
			{
				if(scheme_name != null)
				{
					s += GenerateElement.button("Редактировать " + oneScheme.getName(), "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 1 false false true", 150, 25);
					s += GenerateElement.button("Удалить " + oneScheme.getName(), "_bbsbaffer_del_scheme " + oneScheme.getName(), 150, 25);
					s += GenerateElement.button("Баффнуть " + oneScheme.getName(), "_bbsbaffer_buff_scheme " + oneScheme.getName() + " $type", 150, 25);
				}
			}
		}
		s += "</td>";
		s += "</tr>";
		s += "</table>";
		return GeneratePage.addToTemplate(s);
	}

	private String editScheme(int objectId, int page, OneScheme oneScheme, boolean add, boolean del, boolean run)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player.temp_schemes == null)
		{
			player.temp_schemes = new FastList<Buff>();
		}
		String s = "";
		s += "<table><tr>";
		s += GenerateElement.buttonTD("Добавить бафф", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 1 true false false", 150, 25);
		s += GenerateElement.buttonTD("Удалить бафф", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 1 false true false", 150, 25);
		s += GenerateElement.buttonTD("Использовать бафф", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 1 false false true", 150, 25);
		s += GenerateElement.buttonTD("Назад", "_bbsbaffer_select_scheme " + oneScheme.getName(), 150, 25);
		s += "</tr></table>";
		s += buttonsBuff(objectId, oneScheme, page, add, del, run);
		return GeneratePage.addToTemplate(s);
	}

	private String buttonsBuff(int objectId, OneScheme oneScheme, int page, boolean add, boolean del, boolean run)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player.temp_schemes == null)
		{
			player.temp_schemes = new FastList<Buff>();
		}
		else
		{
			player.temp_schemes.clear();
		}
		boolean on = true;
		for(Buff buff : staticBuffs)
		{
			for(Buff buff_temp : oneScheme.buffs)
			{
				if(buff_temp.getId() == buff.getId())
				{
					on = false;
					break;
				}
			}
			if(on)
			{
				player.temp_schemes.add(buff);
			}
			on = true;
		}
		String s = "<table><tr>";
		s += GenerateElement.buttonTD("1", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 1 " + add + " " + del + " " + run, 50, 25);
		if((add ? player.temp_schemes.size() : oneScheme.buffs.size()) > 28)
		{
			s += GenerateElement.buttonTD("2", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 2 " + add + " " + del + " " + run, 50, 25);
		}
		if((add ? player.temp_schemes.size() : oneScheme.buffs.size()) > 56)
		{
			s += GenerateElement.buttonTD("3", "_bbsbaffer_edit_scheme " + oneScheme.getName() + " 3 " + add + " " + del + " " + run, 50, 25);
		}
		s += "</tr></table>";
		int i = 0;
		int list = 0;
		if(page == 1)
		{
			i = 0;
			list = 28;
		}
		else if(page == 2)
		{
			i = 28;
			list = 56;
		}
		else if(page == 3)
		{
			i = 56;
			list = 84;
		}
		s += "<table width=650><tr>";
		int j = 0;
		for(; i < list; i++)
		{
			j++;
			s += "<td>";
			try
			{
				s += buttonBuff((add ? player.temp_schemes : oneScheme.buffs).get(i).getId(), (add ? player.temp_schemes : oneScheme.buffs).get(i).getLevel(), add, del, run, oneScheme, page);
			}
			catch(Exception e)
			{
				s += "</td>";
				if(j == 4)
				{
					s += "</tr>";
				}
				break;
			}
			s += "</td>";
			if(j == 4)
			{
				s += "</tr>";
				j = 0;
				s += "<tr>";
			}
		}
		s += "</tr>";
		s += "</table>";
		return s;
	}

	private String buttonBuff(int id, int level, boolean add, boolean del, boolean run, OneScheme oneScheme, int page)
	{
		String skiil_id = Integer.toString(id);
		String s = "<table fixwidth=135><tr>";
		String icon;
		if(skiil_id.length() == 3)
		{
			icon = 0 + skiil_id;
		}
		else
		{
			if(id == 4700 || id == 4699)
			{
				icon = "1331";
			}
			else if(id == 4702 || id == 4703)
			{
				icon = "1332";
			}
			else
			{
				icon = skiil_id;
			}
		}
		s += "<td FIXWIDTH=32 height=32 valign=top><img src=icon.skill" + icon + " width=32 height=32></td>";
		String value = "";
		if(add)
		{
			value = "+";
		}
		else if(del)
		{
			value = "-";
		}
		else if(run)
		{
			value = "$";
		}
		s += GenerateElement.buttonTD(value, "_bbsbaffer_adddelrun_buff_scheme " + oneScheme.getName() + " " + id + " " + page + " " + add + " " + del + " " + run, 25, 32);
		s += "<td FIXWIDTH=78 height=0><font color=3293F3>" + SkillTable.getInstance().getInfo(id, level).getName() + "</font><br1><font color=F2C202>Level " + level + "</font></td>";
		s += "</tr></table>";
		return s;
	}

	public static void save(int objectId, OneScheme oneScheme)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		String s = "";
		if(oneScheme.buffs != null)
		{
			for(Buff buff : oneScheme.buffs)
			{
				s += buff.getId() + "," + buff.getLevel() + ",";
			}
			player.setVar("Buf_" + oneScheme.getName(), s);
		}
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
				if(name.contains("Buf_"))
				{
					OneScheme oneScheme = new OneScheme(name.substring(4));
					String value = rs.getString("value");
					if(!value.equals(""))
					{
						String[] buffs = value.split(",");
						for(int i = 0; i < buffs.length; i += 2)
						{
							oneScheme.buffs.add(getBuff(Integer.parseInt(buffs[i])));
						}
					}
					player.schemes.add(oneScheme);
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

	private String normalBuffer(int page)
	{
		String s = "<table><tr>";
		s += GenerateElement.buttonTD("1", "_bbsbaffer_normal 1", 50, 25);
		if(normalScheme.buffs.size() > 28)
		{
			s += GenerateElement.buttonTD("2", "_bbsbaffer_normal 2", 50, 25);
		}
		if(normalScheme.buffs.size() > 56)
		{
			s += GenerateElement.buttonTD("3", "_bbsbaffer_normal 3", 50, 25);
		}
		s += "</tr></table>";
		int i = 0;
		int list = 0;
		if(page == 1)
		{
			i = 0;
			list = 28;
		}
		else if(page == 2)
		{
			i = 28;
			list = 56;
		}
		else if(page == 3)
		{
			i = 56;
			list = 84;
		}
		s += "<table width=650><tr>";
		int j = 0;
		for(; i < list; i++)
		{
			j++;
			s += "<td>";
			try
			{
				s += buttonBuff(normalScheme.buffs.get(i).getId(), normalScheme.buffs.get(i).getLevel(), false, false, true, normalScheme, page);
			}
			catch(Exception e)
			{
				s += "</td>";
				if(j == 4)
				{
					s += "</tr>";
				}
				break;
			}
			s += "</td>";
			if(j == 4)
			{
				s += "</tr>";
				j = 0;
				s += "<tr>";
			}
		}
		s += "</tr>";
		s += "</table>";
		return GeneratePage.addToTemplate(s);
	}

	public static void OnPlayerEnter(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player == null)
		{
			return;
		}
		if(player.schemes == null)
		{
			player.schemes = new FastList<OneScheme>();
			open(player.getObjectId());
		}
	}

	public static void OnPlayerExit(int objectId)
	{
		onDisconnect(objectId);
	}

	public static void onDisconnect(int objectId)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		if(player == null)
		{
			return;
		}
		if(player.schemes == null)
		{
			return;
		}
		for(OneScheme oneScheme : player.schemes)
		{
			save(player.getObjectId(), oneScheme);
		}
	}

	private static void buff(int id, int level, int objectId, boolean pet)
	{
		L2Player player = L2ObjectsStorage.getPlayer(objectId);
		L2Skill skill = SkillTable.getInstance().getInfo(id, level);
		for(EffectTemplate et : skill.getEffectTemplates())
		{
			Env env = new Env(pet ? player.getPet() : player, pet ? player.getPet() : player, skill);
			L2Effect effect = et.getEffect(env);
			effect.setPeriod(Community.communityBuffTimeModifier * 60 * 1000);
			(pet ? player.getPet() : player).getEffectList().addEffect(effect);
		}
	}
}