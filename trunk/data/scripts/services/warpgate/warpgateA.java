package services.warpgate;

import l2p.database.DatabaseUtils;
import l2p.database.FiltredPreparedStatement;
import l2p.database.L2DatabaseFactory;
import l2p.database.ThreadConnection;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.Announcements;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.util.Files;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 16:48
 * http://nova-tm.ru/
 */
public class warpgateA extends Functions implements ScriptFile
{
	public void enter()
	{
		L2Player player = (L2Player) getSelf();
		L2NpcInstance npc = getNpc();
        int HellboundLock = 0;

        ThreadConnection con = null;
		FiltredPreparedStatement trigger = null;

		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			trigger = con.prepareStatement("SELECT unlocked FROM hellbound WHERE name=8000");
			ResultSet trigger1 = trigger.executeQuery();
			trigger1 = trigger.executeQuery();

			while(trigger1.next())
			{
				HellboundLock = trigger1.getInt("unlocked");
			}
		}
		catch(final SQLException e1)
		{
			e1.printStackTrace();
		}
        finally
		{
			DatabaseUtils.closeDatabaseCS(con, trigger);
		}
        if(HellboundLock == 1)
		{
            if(player == null || npc == null)
		{
			return;
		}
			if(player.isGM() || player.isQuestCompleted("_130_PathToHellbound") && player.getLevel() >= 78)
			player.teleToLocation( -11272, 236464, -3248);
			else show(Files.read("data/scripts/services/warpgate/tele-no.htm", player), player);
		}
        else if(HellboundLock == 0)
		{
            if(player == null || npc == null)
		{
			return;
		}
			if(player.isGM() || player.isQuestCompleted("_130_PathToHellbound") && player.isQuestCompleted("_133_ThatsBloodyHot") && player.getLevel() >= 78)
			{
				player.teleToLocation( -11272, 236464, -3248);

				ThreadConnection con1 = null;
				FiltredPreparedStatement insertion = null;
				try
				{
					con1 = L2DatabaseFactory.getInstance().getConnection();
					insertion = con1.prepareStatement("DELETE FROM hellbound WHERE name=8000");
					insertion.executeUpdate();
					insertion.execute();
					insertion.close();
					insertion = con1.prepareStatement("INSERT INTO hellbound (name,hb_points,hb_level,unlocked,dummy) VALUES (?,?,?,?,?)");
					insertion.setInt(1, 8000);
					insertion.setInt(2, 0);
					insertion.setInt(3, 1);
					insertion.setInt(4, 1);
					insertion.setInt(5, 0);
					insertion.executeUpdate();
				}
				catch(final SQLException e)
				{
					e.printStackTrace();
				}
				finally
				{
					DatabaseUtils.closeDatabaseCS(con1, insertion);
				}
				Announcements.getInstance().announceToAll("Hellbound открыт. Уровень: 1.");
			}
			else show(Files.read("data/scripts/services/warpgate/tele-no.htm", player), player);
		}
	}

	public void onLoad()
	{
		System.out.println("Loaded Service: Enter Hellbound Island");
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}