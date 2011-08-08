package l2p.gameserver.serverpackets;

public class ShowBoard extends L2GameServerPacket
{
	private String html;

	public ShowBoard(String html)
	{
		this.html = html;
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x7b);
		writeC(0x01);
		writeS("bypass _bbshome");
		writeS(""); //writeS("bypass _bbsgetfav"); // favorite
		writeS(""); //writeS("bypass _bbsloc"); // region
		writeS(""); //writeS("bypass _bbsclan"); // clan
		writeS(""); //writeS("bypass _bbsmemo"); // memo
		writeS(""); //writeS("bypass _bbsmail"); // mail
		writeS(""); //writeS("bypass _bbsfriends"); // friends
		writeS(""); //writeS("bypass bbs_add_fav"); // add fav.
		writeS(html);
	}

	@Override
	public final String getType()
	{
		return "[S] ShowBoard";
	}
}