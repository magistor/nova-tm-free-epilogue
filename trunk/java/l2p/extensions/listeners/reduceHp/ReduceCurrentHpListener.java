package l2p.extensions.listeners.reduceHp;

import l2p.extensions.listeners.MethodCollection;
import l2p.extensions.listeners.MethodInvokeListener;
import l2p.extensions.listeners.events.MethodEvent;
import l2p.gameserver.model.L2Character;
import l2p.gameserver.model.L2Skill;

/**
 * User: Death
 */
public abstract class ReduceCurrentHpListener implements MethodInvokeListener
{
	public final void methodInvoked(MethodEvent e)
	{
		Object[] args = e.getArgs();
		onReduceCurrentHp((L2Character) e.getOwner(), (Double) args[0], (L2Character) args[1], (L2Skill) args[2], (Boolean) args[3], (Boolean) args[4], (Boolean) args[5]);
	}

	/**
	 * Простенький фильтр. Фильтрирует по названии метода и аргументам.
	 * Ничто не мешает переделать при нужде :)
	 *
	 * @param event событие с аргументами
	 * @return true если все ок ;)
	 */
	public final boolean accept(MethodEvent event)
	{
		return event.getMethodName().equals(MethodCollection.ReduceCurrentHp);
	}

	public abstract void onReduceCurrentHp(L2Character actor, double damage, L2Character attacker, L2Skill skill, boolean awake, boolean standUp, boolean directHp);
}