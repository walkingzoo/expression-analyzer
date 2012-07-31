package neu.sxc.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import neu.sxc.expression.utils.ValueUtil;

/**
 * 可取值符号
 * @author shanxuecheng
 *
 */
public abstract class ValueToken extends TerminalToken implements Valuable {
	
	/**
	 * 数据类型
	 */
	private DataType dataType;
	
	/**
	 * 在数据缓存中的索引
	 */
	private int index;
	
	public ValueToken(TokenBuilder builder) {
		super(builder);
		dataType = builder.getDataType();
		index = builder.getIndex();
	}
	
	protected void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	protected void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public BigDecimal getNumberValue() {
		return ValueUtil.getNumberValue(this);
	}
	
	public String getStringValue() {
		return ValueUtil.getStringValue(this);
	}
	
	public Character getCharValue() {
		return ValueUtil.getCharValue(this);
	}
	
	public Calendar getDateValue() {
		return ValueUtil.getDateValue(this);
	}
	
	public Boolean getBooleanValue() {
		return ValueUtil.getBooleanValue(this);
	}
	
	public Object getValue() {
		return ValueUtil.getValue(this);
	}
}
