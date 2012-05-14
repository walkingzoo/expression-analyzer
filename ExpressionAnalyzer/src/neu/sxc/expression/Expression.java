package neu.sxc.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import neu.sxc.expression.lexical.LexicalAnalyzer;
import neu.sxc.expression.lexical.LexicalException;
import neu.sxc.expression.syntax.SyntaxAnalyzer;
import neu.sxc.expression.syntax.SyntaxException;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.tokens.RuntimeValue;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.TokenType;
import neu.sxc.expression.tokens.Valuable;

/**
 * 表达式
 * @author 单学成
 */
public class Expression {
	private String expression;
	
	private List<TerminalToken> tokens = new ArrayList<TerminalToken>();
	
	private Map<String, Valuable> variableTable = new HashMap<String, Valuable>();
	
	private Map<String, Function> functionTable = new HashMap<String, Function>();
	
	private Valuable finalResult;
	
	public static int DEFAULT_DIVISION_SCALE = 16;
	
	public static RoundingMode DEFAULT_DIVISION_ROUNDING_MODE = RoundingMode.HALF_UP;
	
	public Expression() {}
	
	public Expression(String expression) {
		setExpression(expression);
	}
	
	public Expression(InputStream source) throws IOException {
		setExpression(source);
	}
	
	public Expression(Reader source) throws IOException {
		setExpression(source);
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void setExpression(InputStream source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      	setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public void setExpression(Reader source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      	setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public String getExpression() {
		return expression;
	}
	
	public List<TerminalToken> getTokens() {
		return tokens;
	}
	
	public Set<String> getVariableNames() throws LexicalException {
		lexicalAnalysis();
		Set<String> variableNames = new HashSet<String>();
		for(TerminalToken terminalToken : tokens)
			if(terminalToken.getTokenType() == TokenType.VARIABLE)
				variableNames.add(terminalToken.getText());
		return variableNames;
	}
	
	public void setVariableValue(String name, Object value) {
		RuntimeValue runtimeValue = TokenBuilder.buildRuntimeValue(value);
		variableTable.put(name, runtimeValue);
	}
	
	public Valuable getVariableValue(String name) {
		return variableTable.get(name);
	}
	
	public Map<String, Valuable> getVariableTable() {
		return variableTable;
	}
	
	public void removeVariable(String name) {
		variableTable.remove(name);
	}
	
	public void addFunction(Function function) {
		functionTable.put(function.getName(), function);
	}
	
	public Function getFunction(String functionName) {
		return functionTable.get(functionName);
	}
	
	public Map<String, Function> getFunctionTable() {
		return functionTable;
	}
	
	public void removeFunction(String functionName) {
		functionTable.remove(functionName);
	}
	
	public Valuable getFinalResult() {
		return finalResult;
	}
	
	public Valuable evaluate() throws LexicalException, SyntaxException {
		lexicalAnalysis();
		SyntaxAnalyzer sa = new SyntaxAnalyzer();
		finalResult = sa.analysis(getTokens(), variableTable);
		return finalResult;
	}
	
	private void lexicalAnalysis() throws LexicalException {
		LexicalAnalyzer la = new LexicalAnalyzer();
		tokens = la.analysis(expression, functionTable);
	}
	
	public void clear() {
		tokens.clear();
		finalResult = null;
		variableTable.clear();
		functionTable.clear();
	}
	
	public void clearTokens() {
		tokens.clear();
	}
	
	public void clearVariableTable() {
		variableTable.clear();
	}
	
	public void clearFunctionTable() {
		functionTable.clear();
	}
}