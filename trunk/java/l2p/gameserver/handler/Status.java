package l2p.gameserver.handler;

import javolution.text.TextBuilder;
import l2p.Config;
import l2p.extensions.scripts.Functions;
import l2p.extensions.scripts.Scripts;
import l2p.gameserver.Shutdown;
import l2p.gameserver.model.L2ObjectsStorage;
import l2p.gameserver.model.L2Player;
import l2p.gameserver.tables.FakePlayersTable;
import l2p.gameserver.taskmanager.MemoryWatchDog;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;

public class Status extends Functions implements IVoicedCommandHandler {
    private final String[] _commandList = new String[]{"status", "info", "serverdate"};

    public String[] getVoicedCommandList() {
        return _commandList;
    }

    public boolean useVoicedCommand(String command, L2Player activeChar, String target) {
        if (command.equals("status")) {
            TextBuilder ret = TextBuilder.newInstance();
            boolean en = !activeChar.isLangRus();
            int mtc = Shutdown.getInstance().getSeconds();
            if (mtc > 0) {
                if (en) {
                    ret.append("<br>Total online:  ");
                } else {
                    ret.append("<br>Онлайн сервера:  ");
                }
                ret.append(L2ObjectsStorage.getAllPlayersCount() + FakePlayersTable.getFakePlayersCount());
                if (en) {
                    ret.append("<br1>Time to restart: ");
                } else {
                    ret.append("<br1>До рестарта: ");
                }
                int numDays = mtc / 86400;
                mtc -= numDays * 86400;
                int numHours = mtc / 3600;
                mtc -= numHours * 3600;
                int numMins = mtc / 60;
                mtc -= numMins * 60;
                int numSeconds = mtc;
                if (numDays > 0) {
                    ret.append(numDays + "d ");
                }
                if (numHours > 0) {
                    ret.append(numHours + "h ");
                }
                if (numMins > 0) {
                    ret.append(numMins + "m ");
                }
                if (numSeconds > 0) {
                    ret.append(numSeconds + "s");
                }
            } else {
                ret.append("<br1>Restart task not launched.");
            }
            ret.append("<br><center><button value=\"");
            if (en) {
                ret.append("Refresh");
            } else {
                ret.append("Обновить");
            }
            ret.append("\" action=\"bypass -h user_status\" width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" /></center>");
            show(ret.toString(), activeChar);
            TextBuilder.recycle(ret);
            return true;
        } else if (command.equals("info")) {
            if (activeChar.getPlayerAccess().CanRestart) {
                File f = new File("./l2pserver.jar");
                RandomAccessFile af;
                try {
                    boolean en = !activeChar.isLangRus();
                    af = new RandomAccessFile(f, "r");
                    byte[] b = new byte[(int) f.length()];
                    af.readFully(b);
                    CRC32 c = new CRC32();
                    c.update(b);
                    TextBuilder ret = TextBuilder.newInstance();
                    ret.append("Version: ").append(Config.SERVER_VERSION.equalsIgnoreCase("${l2p.revision}") ? Config.SERVER_VERSION_UNSUPPORTED : Config.SERVER_VERSION);
                    ret.append("<br1>Checksum: ").append(Long.toHexString(c.getValue()).toUpperCase());
                    ret.append("<br1>Last modified: ").append(DateFormat.getDateTimeInstance().format(new Date(f.lastModified())));
                    ret.append("<br1>OS: ").append(System.getenv("OS"));
                    ret.append("<br1>User: ").append(System.getenv("USERNAME"));
                    ret.append("<br1>Jar Scripts: ").append(Scripts.JAR);
                    if (en) {
                        ret.append("<br1>Version: ").append(Config.SERVER_VERSION.equalsIgnoreCase("${l2p.revision}") ? Config.SERVER_VERSION_UNSUPPORTED : Config.SERVER_VERSION);
                    } else {
                        ret.append("<br1>Версия: ").append(Config.SERVER_VERSION.equalsIgnoreCase("${l2p.revision}") ? Config.SERVER_VERSION_UNSUPPORTED : Config.SERVER_VERSION);
                    }
                    if (en) {
                        ret.append("<br1>Memory Usage: ").append(MemoryWatchDog.getMemUsedPerc());
                        ret.append("<br>Total online:  ");
                    } else {
                        ret.append("<br1>Использование памяти: ").append(MemoryWatchDog.getMemUsedPerc());
                        ret.append("<br>Онлайн сервера:  ");
                    }
                    ret.append(L2ObjectsStorage.getAllPlayersCount() + FakePlayersTable.getFakePlayersCount());
                    show(ret.toString(), activeChar);
                    TextBuilder.recycle(ret);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (command.equals("serverdate")) {
            if (activeChar.getPlayerAccess().CanRestart) {
                activeChar.sendMessage("Server date: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " . " + Calendar.getInstance().get(Calendar.MONTH) + " . " + Calendar.getInstance().get(Calendar.YEAR));
                return true;
            }
        }
        return false;
    }
}