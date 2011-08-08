package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.serverpackets.ExNoticePostArrived;
import l2p.gameserver.serverpackets.ExShowReceivedPostList;

/**
 * Отсылается при нажатии на кнопку "почта", "received mail" или уведомление от {@link ExNoticePostArrived}, запрос входящих писем.
 * В ответ шлется {@link ExShowReceivedPostList}
 */
public class RequestExRequestReceivedPostList extends L2GameClientPacket
{
	@Override
	public void readImpl()
	{
	}

	@Override
	public void runImpl()
	{
		L2Player cha = getClient().getActiveChar();
		if(cha != null)
		{
			if(!Config.MailAllow)
			{
				cha.sendMessage("Почта отключена.");
				return;
			}
			if(cha.isInTransaction())
			{
				return;
			}
			cha.sendPacket(new ExShowReceivedPostList(cha));
		}
	}
}