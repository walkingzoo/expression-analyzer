package neu.sxc.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import neu.sxc.expression.syntax.Executable;
import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.utils.DataCache;


public class TokenBuilder {
	
	private int line = -1;
	
	private int column = -1;
	
	private String text;
	
	private DataType dataType;
	
	private int index = -1;
	
	private Function function;
	
	private Executable executable;
	
	private Control control;
	
	private boolean toBeAssigned = false;
	
	public TokenBuilder() {}
	
	public static TokenBuilder getBuilder() {
		return new TokenBuilder();
	}
	
	public TokenBuilder line(int val) {
		line = val;
		return this;
	}
	
	public int getLine() {
		return line;
	}
	
	public TokenBuilder column(int val) {
		column = val;
		return this;
	}
	
	public int getColumn() {
		return column;
	}
	
	public TokenBuilder text(String val) {
		text = val;
		return this;
	}
	
	public String getText() {
		return text;
	}
	
	public TokenBuilder dataType(DataType val) {
		dataType = val;
		return this;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public TokenBuilder index(int val) {
		index = val;
		return this;
	}
	
	public int getIndex() {
		return index;
	}
	
	public TokenBuilder control(Control val) {
		control = val;
		return this;
	}
	
	public Control getControl() {
		return control;
	}
	
	public TokenBuilder function(Function val) {
		function = val;
		return this;
	}
	
	public Function getFunction() {
		return function;
	}
	
	public TokenBuilder executable(Executable val) {
		executable = val;
		return this;
	}
	
	public Executable getExecutable() {
		return executable;
	}
	
	public TokenBuilder toBeAssigned(boolean val) {
		toBeAssigned = val;
		return this;
	}
	
	public boolean isToBeAssigned() {
		return toBeAssigned;
	}
	
	public NonterminalToken buildNT() {
		return new NonterminalToken();
	}
	
	public ExecutionToken buildExecution() {
		return new ExecutionToken(this);
	}
	
	public ControlToken buildController() {
		return new ControlToken(this);
	}
	
	public DelimiterToken buildDelimiter() {
		return new DelimiterToken(this);
	}
	
	public KeyToken buildKey() {
		return new KeyToken(this);
	}
	
	public FunctionToken buildFunction() {
		return new FunctionToken(this);
	}
	
	public ConstToken buildConst() {
		return new ConstToken(this);
	}
	
	public VariableToken buildVariable() {
		return new VariableToken(this);
	}
	
	public RuntimeValue buildRuntimeValue() {
		return new RuntimeValue(this);
	}
	
	public static RuntimeValue buildRuntimeValue(Object value) {
		RuntimeValue runtimeValue = null;
		if(value == null) {
			throw new RuntimeException("Ilegal value : null");
		} else if(value instanceof Integer) {
			runtimeValue = getBuilder().dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex( new BigDecimal((Integer)value)) )
							.buildRuntimeValue();
		} else if (value instanceof Double) {
			runtimeValue = getBuilder().dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex( BigDecimal.valueOf((Double)value)) )
							.buildRuntimeValue();
		} else if (value instanceof BigDecimal) {
			runtimeValue = getBuilder().dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex((BigDecimal)value))
							.buildRuntimeValue();
		} else if (value instanceof String) {
			runtimeValue = getBuilder().dataType(DataType.STRING)
							.index(DataCache.getStringIndex((String)value))
							.buildRuntimeValue();
		} else if (value instanceof Character) {
			runtimeValue = getBuilder().dataType(DataType.CHARACTER)
							.index(DataCache.getCharIndex((Character)value))
							.buildRuntimeValue();
		} else if(value instanceof Boolean) {
			runtimeValue = getBuilder().dataType(DataType.BOOLEAN)
							.index(DataCache.getBooleanIndex((Boolean)value))
							.buildRuntimeValue();
		} else if (value instanceof Calendar) {
			runtimeValue = getBuilder().dataType(DataType.DATE)
							.index(DataCache.getDateIndex((Calendar)value))
							.buildRuntimeValue();
		} else
			throw new RuntimeException("Ilegal value : " + value);
		return runtimeValue;
	}
}
