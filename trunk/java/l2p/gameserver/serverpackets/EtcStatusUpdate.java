package l2p.gameserver.serverpackets;

import l2p.gameserver.model.L2Player;

public class EtcStatusUpdate extends L2GameServerPacket
{
	private int IncreasedForce, WeightPenalty, MessageRefusal, DangerArea;
	private int weaponPenalty, armorPenalty, CharmOfCourage, DeathPenaltyLevel, ConsumedSouls;
	private boolean can_writeImpl = false;

	public EtcStatusUpdate(L2Player player)
	{
		if(player == null || player.getActiveClass() == null)
		{
			return;
		}
		IncreasedForce = player.getIncreasedForce();
		WeightPenalty = player.getWeightPenalty();
		MessageRefusal = player.getMessageRefusal() || player.getNoChannel() != 0 || player.isBlockAll() ? 1 : 0;
		DangerArea = player.isInDangerArea() ? 1 : 0;
		weaponPenalty = player.getWeaponPenalty();
		armorPenalty = player.getArmorPenalty();
		CharmOfCourage = player.isCharmOfCourage() ? 1 : 0;
		DeathPenaltyLevel = player.getDeathPenalty() == null ? 0 : player.getDeathPenalty().getLevel();
		ConsumedSouls = player.getConsumedSouls();
		can_writeImpl = true;
	}

	@Override
	protected final void writeImpl()
	{
		if(!can_writeImpl)
		{
			return;
		}
		writeC(0xf9); //Packet type
		writeD(IncreasedForce); // skill id 4271, 7 lvl
		writeD(WeightPenalty); // skill id 4270, 4 lvl
		writeD(MessageRefusal); //skill id 4269, 1 lvl
		writeD(DangerArea); // skill id 4268, 1 lvl
		writeD(weaponPenalty); // weapon grade penalty, skill 6209 in epilogue, skill id 4267, 1 lvl at off c4 server scripts
		writeD(armorPenalty); // armor grade penalty, skill 6213 in epilogue
		writeD(CharmOfCourage); //Charm of Courage, "Prevents experience value decreasing if killed during a siege war".
		writeD(DeathPenaltyLevel); //Death Penalty max lvl 15, "Combat ability is decreased due to death."
		writeD(ConsumedSouls);
	}
}