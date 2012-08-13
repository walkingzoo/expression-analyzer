package neu.sxc.expression.lexical;

import static neu.sxc.expression.lexical.LexicalConstants.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import neu.sxc.expression.lexical.dfa.DFADefinition;
import neu.sxc.expression.lexical.dfa.DFAEndStateCode;
import neu.sxc.expression.lexical.dfa.DFAMidState;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.syntax.function.SystemFunctions;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.VariableToken;
import neu.sxc.expression.utils.DataCache;
import neu.sxc.expression.utils.ExpressionUtil;

/**
 * 词法分析
 * @author shanxuecheng
 *
 */
public class LexicalAnalyzer {
	
	/**
	 * 有限自动机
	 */
	private DFADefinition DFA = DFADefinition.getDFA();
	
	/**
	 * 当前正在识别的Token
	 */
	private TerminalToken curToken;
	
	/**
	 * 存放当前Token的字符
	 */
	private StringBuilder curWord;
	
	private int curLine = 0;
	
	private int nextScanColumn = 0;
	
	private List<TerminalToken> tokens;
	
	/**
	 * 表达式中涉及的函数
	 */
	private Map<String, Function> functionTable;
	
	public LexicalAnalyzer() {}
	
	public List<TerminalToken> getTokens() {
		return tokens;
	}
	
	public List<TerminalToken> analysis(String expression) throws LexicalException {
		return analysis(expression, null);
	}
	
	public List<TerminalToken> analysis(String expression, Map<String, Function> functionTable) throws LexicalException {
		
		if(expression == null || expression.length() == 0)
			throw new LexicalException("Invalid empty expression.");
		
		Scanner scanner = new Scanner(expression);
		this.functionTable = functionTable;
		prepareLexicalAnalyzer();
		try {
			doAnalysis(scanner);
		} finally {
			scanner.close();
		}
		return tokens;
	}
	
	private void prepareLexicalAnalyzer() {
		curToken = null;
		curWord = new StringBuilder();
		curLine = 0;
		nextScanColumn = 0;
		tokens = new ArrayList<TerminalToken>();
	}
	
	/**
	 * 词法分析
	 * @return
	 * @throws LexicalException 
	 */
	private void doAnalysis(Scanner scanner) throws LexicalException {
		char[] curLineCharArray;	//字符数组用于存放当前行
		char inputChar;
		DFAMidState curMidState = null;		//当前到达的中间状态
		DFAMidState nextMidsState = null;
		DFAEndStateCode endStateCode = null;	//结束状态
		
		while(scanner.hasNextLine()) {
			curLineCharArray = nextLine(scanner).toCharArray();//读取下一行
			curLine++;
			nextScanColumn = 0;
			while(escapeBlank(curLineCharArray) < curLineCharArray.length) {
				//设置当前状态到开始状态，准备识别下一个Token
				curMidState = DFA.getDFAStartState();
				curWord = new StringBuilder();
				curToken = null;
				//识别出一个token，或者遇到词法错误时，跳出循环
				while(true) {
					if(nextScanColumn < curLineCharArray.length) {
						inputChar = curLineCharArray[nextScanColumn]; //取下一字符
						nextMidsState = curMidState.getNextMidState(inputChar);
						if(nextMidsState != null) {
							//下一中间状态不空，追加该字符到当前Token
							curMidState = nextMidsState;
							curWord.append(inputChar);
							nextScanColumn++;
						} else {
							endStateCode = curMidState.goToEndStateWithInput(inputChar);
							if(endStateCode != null) {
								//一个token识别结束（当前输入的字符不追加到curWord）
								actAtEndState(endStateCode);
								break;
							} else
								//发生词法错误
								throw new LexicalException(curMidState, curLine, nextScanColumn);
						}
					} else {
						//在行尾如果curMidState存在到结束状态的路由，说明当前Token正确结束，否则存在词法错误
						if(curMidState.hasRouteToEndState()) {
							actAtEndState(curMidState.getNextEndStateCode());
						} else {
							throw new LexicalException(curMidState, curLine, nextScanColumn);
						}
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 在结束状态执行动作，识别出一个Token
	 * @param endStateCode
	 * @throws LexicalException 
	 */
	private void actAtEndState(DFAEndStateCode endStateCode) throws LexicalException {
		String curWordText = curWord.toString();
		int wordStartColumn = nextScanColumn - curWordText.length();
		switch(endStateCode) {
		case NUMBER_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex(new BigDecimal(curWordText)))
							.buildConst();
			break;
		case ID_END:
			//依次判断是否为布尔常量、关键字、函数名，如果都不是，则判断为变量
			if("true".equals(curWordText) || "TRUE".equals(curWordText)
					|| "false".equals(curWordText) || "FALSE".equals(curWordText)) {
				//识别布尔常量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).dataType(DataType.BOOLEAN)
								.index(DataCache.getBooleanIndex(Boolean.valueOf(curWordText)))
								.buildConst();
			} else if(KEY_WORDS.contains(curWordText)) {
				//识别关键字
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildKey();
			} else if(hasFunction(curWordText)) {
				//是否为函数名
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).function(findFunction(curWordText)).buildFunction();
			} else
				//变量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildVariable();
			break;
		case SINGLE_DELIMITER_END:
			//判断是否为合法的单字符界符，否则词法错误
			if(SINGLE_DELIMITERS.contains(curWordText))
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).buildDelimiter();
			else
				throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			break;
		case DOUBLE_DELIMITER_END:
			if(DOUBLE_DELIMITERS.contains(curWordText)) {	//判断是否为合法的双字符界符
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildDelimiter();
			} else {
				//取第一个字符，如果是合法的单字符界符，当前列扫描位置减1，下次扫描从第二个界符开始
				String firstDelimiter = curWordText.substring(0, 1);
				if(SINGLE_DELIMITERS.contains(firstDelimiter)) {
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(firstDelimiter).buildDelimiter();
					nextScanColumn--;
				} else
					throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			}
			break;
		case DATE_END:
			Calendar date = null;
			DateFormat dateFormate;
			try {
				if(curWordText.matches(DATE_PATTERN)) {
					//日期格式yyyy-MM-dd
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else if(curWordText.matches(ACCURATE_DATE_PATTERN)) {
					//日期格式yyyy-MM-dd HH:mm:ss
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else {
					throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
										curLine, wordStartColumn);
				}
				if(date != null)
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).dataType(DataType.DATE)
									.index(DataCache.getDateIndex(date)).buildConst();
			} catch (ParseException e) {
				throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
						curLine, wordStartColumn);
			}
			break;
		case CHAR_END:
			char ch;
			if(curWordText.length() == 3)
				ch = curWordText.toCharArray()[1];
			else	//识别转义字符
				ch = ExpressionUtil.getEscapedChar(curWordText.toCharArray()[2]);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.CHARACTER)
							.index(DataCache.getCharIndex(ch)).buildConst();
			break;
		case STRING_END:
			String str = curWordText.substring(1, curWordText.length()-1);
			str = ExpressionUtil.transformEscapesInString(str);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.STRING)
							.index(DataCache.getStringIndex(str)).buildConst();
			break;
		}
		if(curToken != null) {
			//添加到Token序列
			tokens.add((TerminalToken)curToken);
			findVariableToBeAssigned();
		}
	}
	
	/**
	 * 判断新识别出的token是否是被赋值的变量
	 */
	private void findVariableToBeAssigned() {
		int size = tokens.size();
		if(size < 2)
			return;
		TerminalToken first = tokens.get(size-2);
		TerminalToken second = tokens.get(size - 1);
		if(!second.equalsInGrammar(ASSIGN_TOKEN))
			return;
		if(first instanceof VariableToken)
			((VariableToken)first).setToBeAssigned(true);
	}
	
	private int escapeBlank(char[] curLineCharArray) {
		while(nextScanColumn < curLineCharArray.length 
				&& ((Character)curLineCharArray[nextScanColumn]).toString()
				.matches(BLANK_PATTERN) ) 
			nextScanColumn++;
			
		return nextScanColumn;
	}
	
	/**
	 * @return 下一行，去掉注释
	 */
	private String nextLine(Scanner scanner){
		return discardComment(scanner.nextLine());
	}
	
	private String discardComment(String target) {
		Pattern commentPattern = Pattern.compile("##.*");
		Matcher matcher = commentPattern.matcher(target);
		return matcher.replaceFirst("");
	}
	
	/**
	 * 判断函数是否存在
	 * @param functionName
	 * @return
	 */
	private boolean hasFunction(String functionName) {
		return hasCustomizedFunction(functionName)
				|| SystemFunctions.hasFunction(functionName);
	}
	
	/**
	 * 查找函数，先判断函数是否为自定义函数，如果不是再判断是否为系统函数
	 * @param functionName
	 * @return
	 */
	private Function findFunction(String functionName) {
		if(hasCustomizedFunction(functionName))
			return functionTable.get(functionName);
		else
			return SystemFunctions.getFunction(functionName);
	}
	
	/**
	 * 判断函数是否为自定义函数
	 * @param functionName
	 * @return
	 */
	private boolean hasCustomizedFunction(String functionName) {
		if(functionTable == null || functionTable.size() == 0)
			return false;
		return functionTable.keySet().contains(functionName);
	}
}
