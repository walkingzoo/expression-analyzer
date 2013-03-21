package neu.sxc.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.RoundingMode;
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
 * 
 * @author shanxuecheng
 * 调用setVariableValue、removeVariable、addFunction、removeFunction后，
 * 为更新词法分析结果，需要重新调用lexicalAnalysis()，
 * 或者直接调用reParseAndEvaluate()，重新执行词法分析并计算结果
 */
public class Expression {
	private String expression;
	
	/**
	 * Token序列
	 */
	private List<TerminalToken> tokens;
	
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
	 * 词法分析器
	 */
	private LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	
	/**
	 * 语法分析器
	 */
	private SyntaxAnalyzer syntaxAnalyzer= new SyntaxAnalyzer();
	
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
	 * 获取所有变量名，调用此方法的前提是已进行词法分析
	 * @return
	 * @throws LexicalException
	 */
	public Set<String> getVariableNames() throws LexicalException {
		if(this.tokens == null) 
			throw new RuntimeException("The 'tokens' is null, Please go for lexical analysis by invoking 'lexicalAnalysis()' first.");
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
	
	/**
	 * 删除变量
	 * @param name
	 */
	public void removeVariable(String name) {
		variableTable.remove(name);
	}
	
	/**
	 * 新增函数
	 * @param function
	 */
	public void addFunction(Function function) {
		function.checkFunctionDefinition();
		functionTable.put(function.getName(), function);
	}
	
	public Function getFunction(String functionName) {
		return functionTable.get(functionName);
	}
	
	public Map<String, Function> getFunctionTable() {
		return functionTable;
	}
	
	/**
	 * 删除函数
	 * @param functionName
	 */
	public void removeFunction(String functionName) {
		functionTable.remove(functionName);
	}
	
	public Valuable getFinalResult() {
		return finalResult;
	}
	
	/**
	 * 词法分析，初始化Token序列
	 * @throws LexicalException
	 * @return token序列
	 */
	public List<TerminalToken> lexicalAnalysis() throws LexicalException {
		tokens = lexicalAnalyzer.analysis(expression, functionTable);
		return tokens;
	}
	
	/**
	 * 解析表达式，调用此方法的前提是已进行词法分析
	 * @return 解析结果
	 * @throws SyntaxException 语法错误异常
	 */
	public Valuable evaluate() throws SyntaxException {
		if(this.tokens == null) 
			throw new RuntimeException("The 'tokens' is null, Please go for lexical analysis by invoking 'lexicalAnalysis()' first.");
		
		//语法分析，返回最终结果
		finalResult = syntaxAnalyzer.analysis(tokens, variableTable);
		//更新变量值
		variableTable = syntaxAnalyzer.getVariableTable();
		return finalResult;
	}
	
	/**
	 * 解析表达式，重新执行词法分析，然后计算表达式
	 * @return 解析结果
	 * @throws LexicalException 词法错误异常
	 * @throws SyntaxException 语法错误异常
	 */
	public Valuable reParseAndEvaluate() throws LexicalException, SyntaxException {
		lexicalAnalysis();
		return evaluate();
	}
	
	public void clear() {
		tokens = null;
		finalResult = null;
		variableTable.clear();
		functionTable.clear();
	}
	
	public void clearTokens() {
		tokens = null;
	}
	
	public void clearVariableTable() {
		variableTable.clear();
	}
	
	public void clearFunctionTable() {
		functionTable.clear();
	}
}