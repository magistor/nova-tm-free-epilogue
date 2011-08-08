package l2p.gameserver.modules.quest;

import l2p.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * User: Shaitan
 * Date: 19.12.10
 * Time: 13:09
 */
public class mQuest
{
	public static ArrayList<mQuestObject> mQuestObjects = new ArrayList<mQuestObject>();
	private static mQuest ourInstance = new mQuest();

	public static mQuest getInstance()
	{
		return ourInstance;
	}

	private mQuest()
	{
		LineNumberReader lnr;
		try
		{
			File vehicleData = new File(Config.DATAPACK_ROOT, "config/QuestMod.ini");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(vehicleData)));
			String line;
			while((line = lnr.readLine()) != null)
			{
				if(line.trim().length() == 0 || line.startsWith("#"))
				{
					continue;
				}
				parseLine(line);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void parseLine(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ";");
		int idQuest = Integer.parseInt(st.nextToken());
		int idItem = Integer.parseInt(st.nextToken());
		int countItem = Integer.parseInt(st.nextToken());
		mQuestObject mQuestObject = new mQuestObject(idQuest, idItem, countItem);
		mQuestObjects.add(mQuestObject);
	}
}