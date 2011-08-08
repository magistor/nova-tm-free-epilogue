package l2p.gameserver.modules.data;

import l2p.Config;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.L2Territory;
import l2p.gameserver.tables.NpcTable;
import l2p.gameserver.tables.SpawnTable;
import l2p.gameserver.tables.TerritoryTable;
import l2p.gameserver.templates.L2NpcTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.StringTokenizer;

/**
 * User: Shaitan
 * Date: 03.01.11
 * Time: 9:34
 */
public class LocAndSpawn
{
	private static LocAndSpawn ourInstance = new LocAndSpawn();

	public static LocAndSpawn getInstance()
	{
		return ourInstance;
	}

	private LocAndSpawn()
	{
		readLocAndSpawn();
	}

	private void readLocAndSpawn()
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc;
		try
		{
			File f = new File(Config.DATAPACK_ROOT + "/data/xml/spawn.xml");
			doc = factory.newDocumentBuilder().parse(f);
			for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if(n.getNodeName().equalsIgnoreCase("territorys"))
				{
					for(Node ters = n.getFirstChild(); ters != null; ters = ters.getNextSibling())
					{
						if(ters.getNodeName().equalsIgnoreCase("territory"))
						{
							NamedNodeMap attr_ter = ters.getAttributes();
							int id = Integer.parseInt(attr_ter.getNamedItem("id").getNodeValue());
							for(Node ter = ters.getFirstChild(); ter != null; ter = ter.getNextSibling())
							{
								if(ter.getNodeName().equalsIgnoreCase("points"))
								{
									for(Node points = ter.getFirstChild(); points != null; points = points.getNextSibling())
									{
										if(points.getNodeName().equalsIgnoreCase("point"))
										{
											NamedNodeMap attr_point = points.getAttributes();
											String xyzz = attr_point.getNamedItem("xyzz").getNodeValue();
											if(TerritoryTable.getInstance()._locations.get(id) == null)
											{
												L2Territory t = new L2Territory(id);
												TerritoryTable.getInstance()._locations.put(id, t);
											}
											StringTokenizer st = new StringTokenizer(xyzz, " ");
											int x = Integer.parseInt(st.nextToken());
											int y = Integer.parseInt(st.nextToken());
											int zmin = Integer.parseInt(st.nextToken());
											int zmax = Integer.parseInt(st.nextToken());
											TerritoryTable.getInstance()._locations.get(id).add(x, y, zmin, zmax);
										}
									}
								}
								else if(ter.getNodeName().equalsIgnoreCase("spawns"))
								{
									for(Node spawn = ter.getFirstChild(); spawn != null; spawn = spawn.getNextSibling())
									{
										if(spawn.getNodeName().equalsIgnoreCase("spawn"))
										{
											NamedNodeMap attr_spawn = spawn.getAttributes();
											int mob_id = Integer.parseInt(attr_spawn.getNamedItem("id").getNodeValue());
											String xyzh = attr_spawn.getNamedItem("xyzh").getNodeValue();
											int count = Integer.parseInt(attr_spawn.getNamedItem("count").getNodeValue());
											int respawn = Integer.parseInt(attr_spawn.getNamedItem("respawn").getNodeValue());
											L2Spawn spawnDat;
											L2NpcTemplate npc;
											npc = NpcTable.getTemplate(mob_id);
											if(npc != null)
											{
												spawnDat = new L2Spawn(npc);
												spawnDat.setAmount(count);
												if(xyzh.equalsIgnoreCase("any"))
												{
													spawnDat.setLocx(0);
													spawnDat.setLocy(0);
													spawnDat.setLocz(0);
													spawnDat.setHeading(0);
												}
												else
												{
													StringTokenizer st = new StringTokenizer(xyzh, " ");
													spawnDat.setLocx(Integer.parseInt(st.nextToken()));
													spawnDat.setLocy(Integer.parseInt(st.nextToken()));
													spawnDat.setLocz(Integer.parseInt(st.nextToken()));
													spawnDat.setHeading(Integer.parseInt(st.nextToken()));
												}
												spawnDat.setRespawnDelay(respawn);
												if(respawn > 0)
												{
													spawnDat.startRespawn();
												}
												spawnDat.setLocation(id);
												spawnDat.setReflection(0);
												spawnDat.init();
												SpawnTable.getInstance()._spawntable.add(spawnDat);
											}
										}
									}
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
	}
}