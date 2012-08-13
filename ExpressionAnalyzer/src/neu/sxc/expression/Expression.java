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
 * @author shanxuecheng
 *
 */
public class Expression {
	private String expression;
	
	/**
	 * Token序列
	 */
	private List<TerminalToken> tokens = new ArrayList<TerminalToken>();
	
	/**
	 * 变量名及其对应的值
	 */
	private Map<String, Valuable> variableTable = new HashMap<String, Valuable>();
	
	/**
	 * 函数名及其对应的函数定义
	 */
	private Map<String, Function> functionTable = new HashMap<String, Function>();
	
	/**
	 * 表达式最终结果
	 */
	private Valuable finalResult;
	
	/**
	 * 除法运算默认采用的scale
	 */
	public static int DEFAULT_DIVISION_SCALE = 16;
	
	/**
	 * 除法运算默认使用的舍入方式
	 */
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
	
	/**
	 * 所有变量名，首先进行词法分析，然后返回所有变量名
	 * @return
	 * @throws LexicalException
	 */
	public Set<String> getVariableNames() throws LexicalException {
		lexicalAnalysis();
		Set<String> variableNames = new HashSet<String>();
		for(TerminalToken terminalToken : tokens)
			if(terminalToken.getTokenType() == TokenType.VARIABLE)
				variableNames.add(terminalToken.getText());
		return variableNames;
	}
	
	/**
	 * 设置变量值
	 * @param name 变量名
	 * @param value 变量值
	 */
	public void setVariableValue(String name, Object value) {
		RuntimeValue runtimeValue = TokenBuilder.buildRuntimeValue(value);
		variableTable.put(name, runtimeValue);
	}
	
	/**
	 * 获取变量值
	 * @param name 变量名
	 * @return
	 */
	public Valuable getVariableValue(String name) {
		return variableTable.get(name);
	}
	
	public Map<String, Valuable> getVariableTable() {
		return variableTable;
	}
	
	public void removeVariable(String name) {
		variableTable.remove(name);
	}
	
	/**
	 * 新增函数
	 * @param function
	 */
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
	
	/**
	 * 解析表达式
	 * @return 解析结果
	 * @throws LexicalException 词法错误异常
	 * @throws SyntaxException 语法错误异常
	 */
	public Valuable evaluate() throws LexicalException, SyntaxException {
		lexicalAnalysis();
		SyntaxAnalyzer sa = new SyntaxAnalyzer();
		finalResult = sa.analysis(getTokens(), variableTable);
		return finalResult;
	}
	
	/**
	 * 词法分析，生成符号序列
	 * @throws LexicalException
	 */
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