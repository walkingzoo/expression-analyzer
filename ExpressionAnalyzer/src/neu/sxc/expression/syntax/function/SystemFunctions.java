package neu.sxc.expression.syntax.function;

import java.util.HashMap;
import java.util.Map;

public class SystemFunctions {
	
	private static Map<String, Function> systemFunctions = new HashMap<String, Function>();
	
	public static Function getFunction(String functionName) {
		return systemFunctions.get(functionName);
	}
	
	public static boolean hasFunction(String functionName) {
		return systemFunctions.keySet().contains(functionName);
	}
	
	private static void registerFunction(Function function) {
		systemFunctions.put(function.getName(), function);
	}
	
	static {
		registerFunction(new Max());
		registerFunction(new Abs());
		registerFunction(new Judge());
	}
}
