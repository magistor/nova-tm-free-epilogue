package l2p.gameserver.modules.community;

/**
 * User: Shaitan
 * Date: 01.11.2010
 * Time: 7:50:50
 */
public class mCommunity
{
	private static mCommunity mCommunity = new mCommunity();

	public static mCommunity getInstance()
	{
		return mCommunity;
	}

	private mICommunity mICommunity;

	public void set(mICommunity mICommunity)
	{
		this.mICommunity = mICommunity;
	}

	public mICommunity get()
	{
		return mICommunity;
	}
}