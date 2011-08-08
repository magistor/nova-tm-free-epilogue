package l2p.gameserver.skills.effects;

import l2p.gameserver.model.L2Effect;
import l2p.gameserver.skills.Env;

public final class EffectSalvation extends L2Effect
{
	public EffectSalvation(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		getEffected().setIsSalvation(true);
	}

	@Override
	public void onExit()
	{
		super.onExit();
		getEffected().setIsSalvation(false);
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}