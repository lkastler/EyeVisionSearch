package eyevisionsearch.logic.strategies;

import java.util.HashMap;

/**
 * provides Strategy implementations stored in "eyevisionsearch.logic.strategies.impl"
 * 
 * @author lkastler
 *
 */
public class StrategyProvider {

	private final String STRATEGIES_IMPL = StrategyProvider.class.getPackage().getName() + ".impl";

	private static StrategyProvider instance;
	
	private HashMap<String, Class<Strategy>> strategies;
	
	/**
	 * returns singleton instance of StrategyProvider
	 * @return
	 */
	public static StrategyProvider getInstance() {
		if(instance == null)
			instance = new StrategyProvider();
		return instance;
	}
	
	private StrategyProvider() {
		strategies = new HashMap<String, Class<Strategy>>();
	}
	/**
	 * returns an instance of Strategy implementation specified by given name
	 * @param name name of Strategy implementation
	 * @return instance of specified Strategy implementation
	 * @throws ClassNotFoundException thrown if implementation could not befound
	 * @throws InstantiationException thrown if instance could not be created
	 * @throws IllegalAccessException thrown if acces is illegal
	 */
	@SuppressWarnings("unchecked")
	public Strategy loadStrategy(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(strategies.get(name) == null) {
			ClassLoader loader = Strategy.class.getClassLoader();
			
			strategies.put(name,(Class<Strategy>) loader.loadClass(STRATEGIES_IMPL + "." + name));
		}
		return strategies.get(name).newInstance();
	}
}
