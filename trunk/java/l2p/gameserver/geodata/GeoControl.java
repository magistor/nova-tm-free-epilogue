package l2p.gameserver.geodata;

import l2p.gameserver.model.L2Territory;
import l2p.gameserver.model.Reflection;

import java.util.HashMap;

public interface GeoControl
{
	public abstract L2Territory getGeoPos();

	public abstract void setGeoPos(L2Territory value);

	public abstract HashMap<Long, Byte> getGeoAround();

	public abstract void setGeoAround(HashMap<Long, Byte> value);

	public abstract Reflection getReflection();

	public abstract boolean isGeoCloser();
}
