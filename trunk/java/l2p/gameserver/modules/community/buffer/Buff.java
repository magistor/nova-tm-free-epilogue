package l2p.gameserver.modules.community.buffer;

/**
 * User: Shaitan
 * Date: 05.11.2010
 * Time: 19:23:11
 */
public class Buff
{
	private int id;
	private int level;

	public Buff(int id, int level)
	{
		this.id = id;
		this.level = level;
	}

	public int getId()
	{
		return id;
	}

	public int getLevel()
	{
		return level;
	}
}
