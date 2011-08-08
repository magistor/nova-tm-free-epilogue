package l2p.gameserver.skills.effects;

import l2p.gameserver.model.L2Effect;
import l2p.gameserver.skills.Env;

public final class EffectRoot extends L2Effect
{
	public EffectRoot(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startRooted();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopRooting();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}