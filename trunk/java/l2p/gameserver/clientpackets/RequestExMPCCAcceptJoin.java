package l2p.gameserver.clientpackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.L2CommandChannel;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.model.base.Transaction;
import l2p.gameserver.model.base.Transaction.TransactionType;
import l2p.gameserver.serverpackets.SystemMessage;

public class RequestExMPCCAcceptJoin extends L2GameClientPacket {
	@SuppressWarnings("unused")
	private int _response, _unk;

	/*
	 * format: chdd
	 */
	@Override
	public void readImpl() {
		_response = _buf.hasRemaining() ? readD() : 0;
		_unk = _buf.hasRemaining() ? readD() : 0;
	}

	@Override
	public void runImpl() {
		L2Player activeChar = getClient().getActiveChar();
		if(activeChar == null) {
			return;
		}
		Transaction transaction = activeChar.getTransaction();
		if(transaction == null) {
			return;
		}
		if(!transaction.isValid() || !transaction.isTypeOf(TransactionType.CHANNEL)) {
			transaction.cancel();
			activeChar.sendPacket(Msg.TIME_EXPIRED, Msg.ActionFail);
			return;
		}
		L2Player requestor = transaction.getOtherPlayer(activeChar);
		transaction.cancel();
		if(!requestor.isInParty() || !activeChar.isInParty() || activeChar.getParty().isInCommandChannel()) {
			requestor.sendPacket(Msg.NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL);
			return;
		}
		if(_response == 1) {
			if(activeChar.isTeleporting()) {
				activeChar.sendPacket(Msg.YOU_CANNOT_JOIN_A_COMMAND_CHANNEL_WHILE_TELEPORTING);
				requestor.sendPacket(Msg.NO_USER_HAS_BEEN_INVITED_TO_THE_COMMAND_CHANNEL);
				return;
			}
			if(requestor.getParty().isInCommandChannel()) {
				requestor.getParty().getCommandChannel().addParty(activeChar.getParty());
			} else if(L2CommandChannel.checkAuthority(requestor)) {
				L2CommandChannel channel = new L2CommandChannel(requestor); // Создаём Command Channel
				requestor.sendPacket(Msg.THE_COMMAND_CHANNEL_HAS_BEEN_FORMED);
				channel.addParty(activeChar.getParty()); // Добавляем приглашенную партию
			} else if (requestor.getInventory().getItemByItemId(L2CommandChannel.STRATEGY_GUIDE_ID) != null) {
				requestor.getInventory().destroyItemByItemId(L2CommandChannel.STRATEGY_GUIDE_ID, 1, false);
				requestor.sendPacket(SystemMessage.removeItems(L2CommandChannel.STRATEGY_GUIDE_ID, 1));
				L2CommandChannel channel = new L2CommandChannel(requestor); // Создаём Command Channel
				requestor.sendPacket(Msg.THE_COMMAND_CHANNEL_HAS_BEEN_FORMED);
				channel.addParty(activeChar.getParty()); // Добавляем приглашенную партию
			}
		} else requestor.sendPacket(new SystemMessage(SystemMessage.S1_HAS_DECLINED_THE_CHANNEL_INVITATION).addString(activeChar.getName()));
	}
}