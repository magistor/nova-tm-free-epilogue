package l2p.extensions.listeners.events;

/**
 * @author Death
 */
public interface MethodEvent
{
	public Object getOwner();

	public Object[] getArgs();

	public String getMethodName();
}
