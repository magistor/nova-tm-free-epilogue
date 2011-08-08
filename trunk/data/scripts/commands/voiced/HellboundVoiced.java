package commands.voiced;

import l2p.Config;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.ScriptFile;
import l2p.gameserver.handler.IVoicedCommandHandler;
import l2p.gameserver.handler.VoicedCommandHandler;
import l2p.gameserver.instancemanager.HellboundManager;
import l2p.gameserver.model.L2Player;
import l2p.util.Files;

/**
 * L2NOVA Team
 * Created by IntelliJ IDEA.
 * User: Nosferatus
 * Date: 29.01.11
 * Time: 5:00
 * http://nova-tm.ru/
 */
public class HellboundVoiced extends Functions implements IVoicedCommandHandler, ScriptFile {

	//	Голосовая команда .hellbound
	//	Предназначена для вывода информации о текущем состоянии острова

	private String[] _commandList = new String[]{"hellbound"};

	public boolean useVoicedCommand(String command, L2Player activeChar, String args) {

		if (command.equals("hellbound")){

			HellboundManager.getInstance().checkLevel();
			boolean _isOpen = HellboundManager.getInstance().checkIsOpen();
			Integer Level = HellboundManager.getInstance().getLevel();
			Long Trust = HellboundManager.getInstance().getPoints();

			String dialog = Files.read("data/scripts/commands/voiced/hellbound.htm", activeChar);
			String status = "";
			if (_isOpen)
				status = "Open.";
			else
				status = "Closed.";
			dialog = dialog.replaceFirst("%status%", status);
			dialog = dialog.replaceFirst("%level%", Level.toString());
			dialog = dialog.replaceFirst("%trust%", Trust.toString());
			show(dialog, activeChar);
			return true;

		}
		return true;
	}

	public String[] getVoicedCommandList(){
		return _commandList;
	}

	public void onLoad(){
		if(Config.HELLBOUND_VOICED_COMMAND)
			VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}

	public void onReload()
	{}

	public void onShutdown()
	{}
}
