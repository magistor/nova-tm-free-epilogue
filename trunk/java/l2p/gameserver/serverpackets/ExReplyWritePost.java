package l2p.gameserver.serverpackets;

import l2p.Config;
import l2p.gameserver.clientpackets.RequestExSendPost;

/**
 * Запрос на отправку нового письма. Шлется в ответ на {@link RequestExSendPost}.
 */
public class ExReplyWritePost extends L2GameServerPacket
{
	private int _reply;

	/**
	 * @param i если 1 окно создания письма закрывается
	 */
	public ExReplyWritePost(int i)
	{
		if(!Config.MailAllow)
		{
			return;
		}
		_reply = i;
	}

	@Override
	protected void writeImpl()
	{
		writeC(EXTENDED_PACKET);
		writeH(0xB4);
		writeD(_reply); // 1 - закрыть окно письма, иное - не закрывать
	}
}