package l2p.gameserver.modules.community.teleport;

/**
 * User: Shaitan
 * Date: 09.12.2010
 * Time: 11:28:12
 */
public class TeleportPoint
{
	private String name;
	private String xyz;

	public TeleportPoint(String name, String xyz)
	{
		this.name = name;
		this.xyz = xyz;
	}

	public String getName()
	{
		return name;
	}

	public String getXYZ()
	{
		return xyz;
	}
}