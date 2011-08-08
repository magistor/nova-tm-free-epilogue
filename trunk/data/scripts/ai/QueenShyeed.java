package ai;

import l2p.gameserver.ai.Fighter;
import l2p.gameserver.instancemanager.ZoneManager;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Zone;
import l2p.gameserver.model.L2Zone.ZoneType;

/**
* Ai для Queen Syeed в Stakato nest 
* При смерти меняет зонатор(Выключает одну зону включает другую...)
*/
public class QueenShyeed extends Fighter
{	
	private L2Zone _zone;
	private L2Zone _zone1;
	
	public QueenShyeed(L2Character actor)
	{
		super(actor);
	}
	
	public final L2Zone getZoneBuff()
	{
		if (_zone == null)
			_zone = ZoneManager.getInstance().getZoneById(ZoneType.dummy, 999222, true);
		return _zone;
	}

	
	public final L2Zone getZoneDebuff()
	{
		if (_zone1 == null)
			_zone1 = ZoneManager.getInstance().getZoneById(ZoneType.damage, 999223, false);
		return _zone1;
	}
	
	@Override
	protected void onEvtSpawn()
	{
		getZoneBuff().setActive(false);
		getZoneDebuff().setActive(true);
		super.onEvtSpawn();
	}
	
	@Override
	protected void onEvtDead(L2Character killer)
	{
		getZoneDebuff().setActive(false);
		getZoneBuff().setActive(true);
		super.onEvtDead(killer);
	}
}