package l2p.gameserver.serverpackets;

import l2p.gameserver.model.entity.SevenSigns;

/**
 * Seven Signs Info
 * <p/>
 * packet id 0x73
 * format: cc
 * <p/>
 * Пример пакета с оффа (828 протокол):
 * 73 01 01
 * <p/>
 * Возможные варианты использования данного пакета:
 * 0 0 - Обычное небо???
 * 1 1 - Dusk Sky
 * 2 2 - Dawn Sky???
 * 3 3 - Небо постепенно краснеет (за 10 секунд)
 * <p/>
 * Возможно и другие вариации, эффект не совсем понятен.
 * 1 0
 * 0 1
 *
 * @author SYS
 */
public class SSQInfo extends L2GameServerPacket
{
	private int _state = 0;

	public SSQInfo()
	{
		int compWinner = SevenSigns.getInstance().getCabalHighestScore();
		if(SevenSigns.getInstance().isSealValidationPeriod())
		{
			if(compWinner == SevenSigns.CABAL_DAWN)
			{
				_state = 2;
			}
			else if(compWinner == SevenSigns.CABAL_DUSK)
			{
				_state = 1;
			}
		}
	}

	public SSQInfo(int state)
	{
		_state = state;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x73);
		writeC(_state);
		writeC(0x01);
	}
}