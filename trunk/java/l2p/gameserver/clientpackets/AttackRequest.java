package l2p.gameserver.clientpackets;

import l2p.gameserver.model.L2Object;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;

public class AttackRequest extends L2GameClientPacket
{
	// cddddc
	private int _objectId;
	private int _attackId;

	@Override
	public void readImpl()
	{
		_objectId = readD();
		readD();
		readD();
		readD();
		_attackId = readC(); // 0 for simple click   1 for shift-click
	}

	@Override
	public void runImpl()
	{
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null)
		{
			return;
		}
		if(activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if(!activeChar.getPlayerAccess().CanAttack)
		{
			activeChar.sendActionFailed();
			return;
		}
		L2Object target = activeChar.getVisibleObject(_objectId);
		if(target == null && ((target = L2ObjectsStorage.getItemByObjId(_objectId)) == null || !activeChar.isInRange(target, 1000)))
		{
			activeChar.sendActionFailed();
			return;
		}
		if(activeChar.getAggressionTarget() != null && activeChar.getAggressionTarget() != target)
		{
			activeChar.sendActionFailed();
			return;
		}
		if(target.isPlayer() && (activeChar.isInVehicle() || target.isInVehicle()))
		{
			activeChar.sendActionFailed();
			return;
		}
		if(activeChar.getTarget() != target)
		{
			target.onAction(activeChar, _attackId == 1);
			return;
		}
		if(target.getObjectId() != activeChar.getObjectId() && activeChar.getPrivateStoreType() == L2Player.STORE_PRIVATE_NONE && !activeChar.isInTransaction())
		{
			target.onForcedAttack(activeChar, _attackId == 1);
		}
	}
}