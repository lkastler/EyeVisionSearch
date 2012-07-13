package eyevisionsearch.json;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Test case
 *  
 * @author lkastler
 */
public class JSONParserTest {
	/**
	 * empty constructor for JUnit
	 */
	public JSONParserTest() {}
	
	/**
	 * test case string parsing to array
	 * @throws Exception
	 */
	@org.junit.Test 
	public void Parsing_String_Array() throws Exception {
		String s = new JSONArray().toString();
		
		JSONParser parse = new JSONParser(s);
		
		assertEquals(parse.getType(), JSONParser.Type.JsonArray);
		assertEquals(parse.toString() , s);	
	}
	
	/**
	 * test case string parsing to array
	 * @throws Exception
	 */
	@org.junit.Test(expected=JSONException.class)
	public void Parsing_String_Array_with_Exception() throws Exception {
		String s = new JSONArray().toString();
		
		JSONParser parse = new JSONParser(s);
		
		assertEquals(parse.getType(), JSONParser.Type.JsonArray);
		parse.getJsonArray();
		parse.getJsonObject();
	}
	
	/**
	 * 	test case string parsing to Object
	 * @throws Exception
	 */
	@org.junit.Test
	public void Parsing_String_Object() throws Exception {
		String s = new JSONObject().toString();
		
		JSONParser parse = new JSONParser(s);
		
		assertEquals(parse.getType(), JSONParser.Type.JsonObject);
		assertEquals(parse.toString(), s);
		
	}
	
	/**
	 * test case string parsing to array
	 * @throws Exception
	 */
	@org.junit.Test(expected=JSONException.class)
	public void Parsing_String_Object_with_Exception() throws Exception {
		String s = new JSONObject().toString();
		
		JSONParser parse = new JSONParser(s);
		
		assertEquals(parse.getType(), JSONParser.Type.JsonObject);
		parse.getJsonObject();
		parse.getJsonArray();
	}
}
