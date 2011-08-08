package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.serverpackets.ExReplyReceivedPost;
import l2p.gameserver.serverpackets.ExShowReceivedPostList;

/**
 * Запрос информации об полученном письме. Появляется при нажатии на письмо из списка {@link ExShowReceivedPostList}.
 */
public class RequestExRequestReceivedPost extends L2GameClientPacket
{
	private int postId;

	@Override
	public void readImpl()
	{
		postId = readD(); // id письма
	}

	@Override
	public void runImpl()
	{
		if(!Config.MailAllow)
		{
			return;
		}
		L2Player cha = getClient().getActiveChar();
		if(cha != null)
		{
			cha.sendPacket(new ExReplyReceivedPost(cha, postId));
		}
	}
}