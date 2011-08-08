package l2p.gameserver.serverpackets;

import l2p.util.GArray;

public class AbnormalStatusUpdate extends L2GameServerPacket
{
	public static final int INFINITIVE_EFFECT = -1;
	private GArray<Effect> _effects;

	class Effect
	{
		int skillId;
		int dat;
		int duration;

		public Effect(int skillId, int dat, int duration)
		{
			this.skillId = skillId;
			this.dat = dat;
			this.duration = duration;
		}
	}

	public AbnormalStatusUpdate()
	{
		_effects = new GArray<Effect>();
	}

	public void addEffect(int skillId, int dat, int duration)
	{
		_effects.add(new Effect(skillId, dat, duration));
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x85);
		writeH(_effects.size());
		for(Effect temp : _effects)
		{
			writeD(temp.skillId);
			writeH(temp.dat);
			writeD(temp.duration);
		}
	}
}