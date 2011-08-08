package l2p.gameserver.modules.community;

/**
 * User: Shaitan
 * Date: 01.11.2010
 * Time: 18:47:23
 */
public interface mICommunityHandler
{
	public void useHandler(int objectId, String command) throws InterruptedException;

	public String[] getHandlerList();
}