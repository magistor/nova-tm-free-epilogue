package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.serverpackets.ExReplySentPost;
import l2p.gameserver.serverpackets.ExShowSentPostList;

/**
 * Запрос информации об отправленном письме. Появляется при нажатии на письмо из списка {@link ExShowSentPostList}.
 * В ответ шлется {@link ExReplySentPost}.
 */
public class RequestExRequestSentPost extends L2GameClientPacket
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
			cha.sendPacket(new ExReplySentPost(cha, postId));
		}
	}
}