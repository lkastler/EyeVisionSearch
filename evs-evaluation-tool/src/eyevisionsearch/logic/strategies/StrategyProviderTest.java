package eyevisionsearch.logic.strategies;

import static org.junit.Assert.*;
import eyevisionsearch.logic.strategies.impl.DurationStrategy;

/**
 * test strategies
 * 
 * @author lkastler
 *
 */
public class StrategyProviderTest {
	public StrategyProviderTest() {}
	
	@org.junit.Test
	public void test_class_loader() throws Exception {
		 Strategy s = StrategyProvider.getInstance().loadStrategy("DurationStrategy");
		 assertEquals(s.getName(), new DurationStrategy().getName());
	}
}
