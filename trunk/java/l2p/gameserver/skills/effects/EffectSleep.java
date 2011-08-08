package l2p.gameserver.skills.effects;

import l2p.gameserver.model.L2Effect;
import l2p.gameserver.skills.Env;

public final class EffectSleep extends L2Effect
{
	public EffectSleep(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		_effected.startSleeping();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		_effected.stopSleeping();
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}