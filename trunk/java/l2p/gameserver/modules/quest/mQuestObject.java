package l2p.gameserver.modules.quest;

public class mQuestObject
{
	private int idQuest;
	private int idItem;
	private int countItem;

	public mQuestObject(int idQuest, int idItem, int countItem)
	{
		this.idQuest = idQuest;
		this.idItem = idItem;
		this.countItem = countItem;
	}

	public int getIdQuest()
	{
		return idQuest;
	}

	public int getIdItem()
	{
		return idItem;
	}

	public int getCountItem()
	{
		return countItem;
	}
}