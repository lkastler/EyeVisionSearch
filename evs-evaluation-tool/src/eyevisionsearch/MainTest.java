package eyevisionsearch;

public class MainTest {

	public MainTest(){}
	
	@org.junit.Test 
	public void test() throws Exception {
		Main m = new Main("config.json");
		m.execute();
		
	}
}
