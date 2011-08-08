package l2p.gameserver.serverpackets;

import l2p.Config;
import l2p.gameserver.model.L2Character;
import l2p.util.Location;

public class CharMoveToLocation extends L2GameServerPacket
{
	private int _objectId, _client_z_shift;
	private Location _current;
	private Location _destination;

	public CharMoveToLocation(L2Character cha)
	{
		_objectId = cha.getObjectId();
		_current = cha.getLoc();
		_destination = cha.getDestination();
		_client_z_shift = cha.isFlying() || cha.isInWater() ? 0 : Config.CLIENT_Z_SHIFT;
	}

	public CharMoveToLocation(int objectId, Location from, Location to)
	{
		_objectId = objectId;
		_current = from;
		_destination = to;
	}

	@Override
	protected final void writeImpl()
	{
		if(_destination == null)
		{
			return;
		}
		writeC(0x2f);
		writeD(_objectId);
		writeD(_destination.x);
		writeD(_destination.y);
		writeD(_destination.z + _client_z_shift);
		writeD(_current.x);
		writeD(_current.y);
		writeD(_current.z + _client_z_shift);
	}
}