package l2p.gameserver.modules.data;

import l2p.Config;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.L2Territory;
import l2p.gameserver.model.instances.L2DoorInstance;
import l2p.gameserver.templates.L2CharTemplate;
import l2p.gameserver.templates.StatsSet;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

public class DoorTable
{
	private static final Logger _log = Logger.getLogger(DoorTable.class.getName());
	private HashMap<Integer, L2DoorInstance> _staticItems;
	private static DoorTable _instance;

	public static DoorTable getInstance()
	{
		if(_instance == null)
		{
			new DoorTable();
		}
		return _instance;
	}

	public DoorTable()
	{
		_instance = this;
		_staticItems = new HashMap<Integer, L2DoorInstance>();
		readDoors();
	}

	public void respawn()
	{
		for(L2DoorInstance door : _staticItems.values())
		{
			if(door != null)
			{
				door.decayMe();
			}
		}
		_instance = new DoorTable();
	}

	private void readDoors()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc;
		try
		{
			File f = new File(Config.DATAPACK_ROOT + "/data/xml/doors.xml");
			doc = factory.newDocumentBuilder().parse(f);
			for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if(n.getNodeName().equalsIgnoreCase("doors"))
				{
					for(Node n1 = n.getFirstChild(); n1 != null; n1 = n1.getNextSibling())
					{
						if(n1.getNodeName().equalsIgnoreCase("door"))
						{
							NamedNodeMap attr = n1.getAttributes();
							//--------------------------------------------------------------
							int id = Integer.parseInt(attr.getNamedItem("id").getNodeValue());
							String name = attr.getNamedItem("name").getNodeValue();
							int hp = Integer.parseInt(attr.getNamedItem("hp").getNodeValue());
							int pdef = Integer.parseInt(attr.getNamedItem("pdef").getNodeValue());
							int mdef = Integer.parseInt(attr.getNamedItem("mdef").getNodeValue());
							boolean unlockable = Boolean.parseBoolean(attr.getNamedItem("unlockable").getNodeValue());
							int key = Integer.parseInt(attr.getNamedItem("key").getNodeValue());
							byte level = Byte.parseByte(attr.getNamedItem("level").getNodeValue());
							boolean showHp = Boolean.parseBoolean(attr.getNamedItem("showHp").getNodeValue());
							int posx = Integer.parseInt(attr.getNamedItem("posx").getNodeValue());
							int posy = Integer.parseInt(attr.getNamedItem("posy").getNodeValue());
							int posz = Integer.parseInt(attr.getNamedItem("posz").getNodeValue());
							int ax = Integer.parseInt(attr.getNamedItem("ax").getNodeValue());
							int ay = Integer.parseInt(attr.getNamedItem("ay").getNodeValue());
							int bx = Integer.parseInt(attr.getNamedItem("bx").getNodeValue());
							int by = Integer.parseInt(attr.getNamedItem("by").getNodeValue());
							int cx = Integer.parseInt(attr.getNamedItem("cx").getNodeValue());
							int cy = Integer.parseInt(attr.getNamedItem("cy").getNodeValue());
							int dx = Integer.parseInt(attr.getNamedItem("dx").getNodeValue());
							int dy = Integer.parseInt(attr.getNamedItem("dy").getNodeValue());
							int minz = Integer.parseInt(attr.getNamedItem("minz").getNodeValue());
							int maxz = Integer.parseInt(attr.getNamedItem("maxz").getNodeValue());
							boolean siege_weapon = Boolean.parseBoolean(attr.getNamedItem("siege_weapon").getNodeValue());
							boolean geodata = Boolean.parseBoolean(attr.getNamedItem("geodata").getNodeValue());
							//--------------------------------------------------------------
							StatsSet baseDat = new StatsSet();
							baseDat.set("level", 0);
							baseDat.set("jClass", "door");
							baseDat.set("baseSTR", 0);
							baseDat.set("baseCON", 0);
							baseDat.set("baseDEX", 0);
							baseDat.set("baseINT", 0);
							baseDat.set("baseWIT", 0);
							baseDat.set("baseMEN", 0);
							baseDat.set("baseShldDef", 0);
							baseDat.set("baseShldRate", 0);
							baseDat.set("baseAccCombat", 38);
							baseDat.set("baseEvasRate", 38);
							baseDat.set("baseCritRate", 38);
							baseDat.set("collision_radius", 5);
							baseDat.set("collision_height", 0);
							baseDat.set("sex", "male");
							baseDat.set("type", "");
							baseDat.set("baseAtkRange", 0);
							baseDat.set("baseMpMax", 0);
							baseDat.set("baseCpMax", 0);
							baseDat.set("revardExp", 0);
							baseDat.set("revardSp", 0);
							baseDat.set("basePAtk", 0);
							baseDat.set("baseMAtk", 0);
							baseDat.set("basePAtkSpd", 0);
							baseDat.set("aggroRange", 0);
							baseDat.set("baseMAtkSpd", 0);
							baseDat.set("rhand", 0);
							baseDat.set("lhand", 0);
							baseDat.set("armor", 0);
							baseDat.set("baseWalkSpd", 0);
							baseDat.set("baseRunSpd", 0);
							baseDat.set("baseHpReg", 0);
							baseDat.set("baseCpReg", 0);
							baseDat.set("baseMpReg", 0);
							baseDat.set("siege_weapon", false);
							baseDat.set("geodata", true);
							//--------------------------------------------------------------
							StatsSet npcDat;
							npcDat = baseDat.clone();
							npcDat.set("npcId", id);
							npcDat.set("name", name);
							npcDat.set("baseHpMax", hp);
							npcDat.set("basePDef", pdef);
							npcDat.set("baseMDef", mdef);
							L2CharTemplate template = new L2CharTemplate(npcDat);
							L2DoorInstance door = new L2DoorInstance(IdFactory.getInstance().getNextId(), template, id, name, unlockable, showHp);
							_staticItems.put(id, door);
							L2Territory pos = new L2Territory(id);
							door.setGeoPos(pos);
							pos.add(ax, ay, minz, maxz);
							pos.add(bx, by, minz, maxz);
							pos.add(cx, cy, minz, maxz);
							pos.add(dx, dy, minz, maxz);
							door.getTemplate().collisionHeight = maxz - minz & 0xfff0;
							door.getTemplate().collisionRadius = Math.max(50, Math.min(posx - pos.getXmin(), posy - pos.getYmin()));
							door.setXYZInvisible(posx, posy, minz + 32);
							door.setCurrentHpMp(door.getMaxHp(), door.getMaxMp(), true);
							door.setOpen(false);
							door.level = level;
							door.key = key;
							// Дверь/стена может быть атакована только осадным оружием
							door.setSiegeWeaponOlnyAttackable(siege_weapon);
							door.setGeodata(geodata);
							door.spawnMe(door.getLoc());
							if(Config.debugDoor)
							{
								if(door.getTemplate().collisionRadius > 200)
								{
									System.out.println("DoorId: " + id + ", collision: " + door.getTemplate().collisionRadius + ", posx: " + posx + ", posy: " + posy + ", xMin: " + pos.getXmin() + ", yMin: " + pos.getYmin());
								}
								if(pos.getXmin() == pos.getXmax())
								{
									_log.warning("door " + id + " has zero size");
								}
								else if(pos.getYmin() == pos.getYmax())
								{
									_log.warning("door " + id + " has zero size");
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		_log.config("DoorTable: Loaded " + _staticItems.size() + " doors.");
	}

	public boolean isInitialized()
	{
		boolean _initialized = true;
		return _initialized;
	}

	public L2DoorInstance getDoor(Integer id)
	{
		return _staticItems.get(id);
	}

	public void putDoor(Integer id, L2DoorInstance door)
	{
		_staticItems.put(id, door);
	}

	public void removeDoor(Integer id)
	{
		_staticItems.remove(id);
	}

	public L2DoorInstance[] getDoors()
	{
		return _staticItems.values().toArray(new L2DoorInstance[_staticItems.size()]);
	}

	/**
	 * Performs a check and sets up a scheduled task for
	 * those doors that require auto opening/closing.
	 */
	public void checkAutoOpen()
	{
		for(L2DoorInstance doorInst : getDoors())
		// Garden of Eva (every 7 minutes)
		{
			if(doorInst.getDoorName().startsWith("Eva_"))
			{
				doorInst.setAutoActionDelay(420000);
			}
			// Tower of Insolence (every 5 minutes)
			else if(doorInst.getDoorName().startsWith("hubris_"))
			{
				doorInst.setAutoActionDelay(300000);
			}
			// Giran Devil island (every 5 minutes)
			else if(doorInst.getDoorName().startsWith("Devil.opendoor"))
			{
				doorInst.setAutoActionDelay(300000);
			}
			// Coral Garden Gate (every 15 minutes)
			else if(doorInst.getDoorName().startsWith("Coral_garden"))
			{
				doorInst.setAutoActionDelay(900000);
			}
			// Normil's cave (every 5 minutes)
			else if(doorInst.getDoorName().startsWith("Normils_cave"))
			{
				doorInst.setAutoActionDelay(300000);
			}
			// Normil's Garden (every 15 minutes)
			else if(doorInst.getDoorName().startsWith("Normils_garden"))
			{
				doorInst.setAutoActionDelay(900000);
			}
		}
	}
}