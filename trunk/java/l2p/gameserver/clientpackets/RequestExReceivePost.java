package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.MailParcelController;
import l2p.gameserver.serverpackets.ExReplyReceivedPost;
import l2p.gameserver.serverpackets.ExShowReceivedPostList;

/**
 * Шлется клиентом при согласии принять письмо в {@link ExReplyReceivedPost}. Если письмо с оплатой то создателю письма шлется запрошенная сумма.
 */
public class RequestExReceivePost extends L2GameClientPacket
{
	private int postId;

	@Override
	public void readImpl()
	{
		postId = readD();
	}

	@Override
	public void runImpl()
	{
		if(!Config.MailAllow)
		{
			return;
		}
		L2Player cha = getClient().getActiveChar();
		if(cha == null)
		{
			return;
		}
		MailParcelController.getInstance().receivePost(postId, cha);
		cha.sendPacket(new ExShowReceivedPostList(cha));
	}
}