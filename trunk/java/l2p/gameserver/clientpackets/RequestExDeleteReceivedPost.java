package l2p.gameserver.clientpackets;

import l2p.Config;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.items.MailParcelController;
import l2p.gameserver.serverpackets.ExShowReceivedPostList;

/**
 * Запрос на удаление полученных сообщений. Удалить можно только письмо без вложения. Отсылается при нажатии на "delete" в списке полученных писем.
 */
public class RequestExDeleteReceivedPost extends L2GameClientPacket
{
	private int[] _list;

	@Override
	public void readImpl()
	{
		_list = new int[readD()]; // количество элементов для удаления
		for(int i = 0; i < _list.length; i++)
		{
			_list[i] = readD();
		} // уникальный номер письма
	}

	@Override
	public void runImpl()
	{
		if(!Config.MailAllow)
		{
			return;
		}
		if(_list.length == 0)
		{
			return;
		}
		MailParcelController.getInstance().deleteLetter(_list);
		L2Player cha = getClient().getActiveChar();
		if(cha != null)
		{
			cha.sendPacket(new ExShowReceivedPostList(cha));
		}
	}
}