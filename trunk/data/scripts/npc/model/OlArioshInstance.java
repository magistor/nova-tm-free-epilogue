package npc.model;

import l2p.common.ThreadPoolManager;
import l2p.extensions.scripts.Functions;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.instances.L2MinionInstance;
import l2p.gameserver.model.instances.L2ReflectionBossInstance;
import l2p.gameserver.templates.L2NpcTemplate;

import java.util.concurrent.ScheduledFuture;

public class OlArioshInstance extends L2ReflectionBossInstance
{
	private ScheduledFuture<?> _spawner;

	public OlArioshInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void notifyMinionDied(L2MinionInstance minion)
	{
		if(_minionList != null)
		{
			_minionList.removeSpawnedMinion(minion);
		}
		_spawner = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new MinionSpawner(), 60000, 60000, false);
	}

	@Override
	public void onSpawn()
	{
		setNewMinionList();
		_minionList.spawnSingleMinionSync(18556);
		super.onSpawn();
	}

	@Override
	public void doDie(L2Character killer)
	{
		if(_spawner != null)
		{
			_spawner.cancel(true);
		}
		super.doDie(killer);
	}

	public class MinionSpawner implements Runnable
	{
		public void run()
		{
			try
			{
				if(!OlArioshInstance.this.isDead() && OlArioshInstance.this.getTotalSpawnedMinionsInstances() == 0)
				{
					if(OlArioshInstance.this.getMinionList() == null)
					{
						OlArioshInstance.this.new MinionMaintainTask().run();
					}
					OlArioshInstance.this.getMinionList().spawnSingleMinionSync(18556);
					Functions.npcSayCustomMessage(OlArioshInstance.this, "OlAriosh.helpme");
				}
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
}