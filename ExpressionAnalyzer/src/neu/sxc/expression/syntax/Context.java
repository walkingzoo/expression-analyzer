package neu.sxc.expression.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import neu.sxc.expression.tokens.Valuable;


public class Context {
	
	private boolean effective;
	
	private Map<String, Valuable> variableTable;
	
	private int startIndex = -1;
	
	public Context(boolean effective, Map<String, Valuable> variableTable, int startIndex) {
		this.effective = effective;
		this.variableTable = variableTable;
		this.startIndex = startIndex;
	}

	public boolean isEffective() {
		return effective;
	}
	
	public Map<String, Valuable> getVariableTable() {
		return this.variableTable;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public Valuable getVariableValue(String variableName) {
		return variableTable.get(variableName);
	}
	
	public void setVariableValue(String variableName, Valuable value) {
		variableTable.put(variableName, value);
	}
	
	public Context constructUpon(boolean effective, int startIndex) {
		Map<String, Valuable> variableTableUpon = new HashMap<String, Valuable>();
		variableTableUpon.putAll(variableTable);
		return new Context(effective, variableTableUpon, startIndex);
	}
	
	public void update(Context context) {
		Set<String> variableNames = variableTable.keySet();
		Set<String> targetVariableNames = context.getVariableTable().keySet();
		for(String variableName : variableNames) 
			if(targetVariableNames.contains(variableName))
				setVariableValue(variableName, context.getVariableValue(variableName));
	}
}
