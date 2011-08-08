package events.RabbitsToRiches;

import l2p.common.ThreadPoolManager;
import l2p.gameserver.model.L2Spawn;
import l2p.gameserver.model.instances.L2MonsterInstance;
import l2p.gameserver.tables.SpawnTable;
import l2p.gameserver.templates.L2NpcTemplate;

import java.util.Collection;

public class RichesTreasureInstance extends L2MonsterInstance
{
	public RichesTreasureInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	public class UnSpawnTask implements Runnable
	{
		public final static int SEARCH_TREASURE = 13098;

		@SuppressWarnings("unused")
		private void checkSpawn()
		{
			ThreadPoolManager.getInstance().scheduleAi(new UnSpawnTask(), 18000, false);
			unSpawn(SEARCH_TREASURE);
		}

		public void unSpawn(int id)
		{
			Collection<L2Spawn> worldObjects = SpawnTable.getInstance().getSpawnTable();
			for(L2Spawn i : worldObjects)
			{
				int npcId = i.getNpcId();
				if(npcId == id)
				{
					i.stopRespawn();
					i.despawnAll();
				}
			}
		}

		@Override
		public void run()
		{
		}
	}
}