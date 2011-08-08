package l2p.gameserver.modules.community.buffer;

import java.util.ArrayList;

public class OneScheme
{
	private String name;
	public ArrayList<Buff> buffs;

	public OneScheme(String name)
	{
		this.name = name;
		buffs = new ArrayList<Buff>();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}