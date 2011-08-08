package l2p.gameserver.skills.effects;

import l2p.gameserver.model.L2Effect;
import l2p.gameserver.model.instances.L2NpcInstance;
import l2p.gameserver.skills.Env;

public class EffectUnAggro extends L2Effect
{
	public EffectUnAggro(Env env, EffectTemplate template)
	{
		super(env, template);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		if(_effected.isNpc())
		{
			((L2NpcInstance) _effected).setUnAggred(true);
		}
	}

	@Override
	public void onExit()
	{
		super.onExit();
		if(_effected.isNpc())
		{
			((L2NpcInstance) _effected).setUnAggred(false);
		}
	}

	@Override
	public boolean onActionTime()
	{
		return false;
	}
}