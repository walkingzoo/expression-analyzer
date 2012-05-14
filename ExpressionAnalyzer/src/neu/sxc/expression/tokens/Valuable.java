package neu.sxc.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

public interface Valuable extends Token {
	
	public DataType getDataType();
	
	public int getIndex();
	
	public BigDecimal getNumberValue();
	
	public String getStringValue();
	
	public Character getCharValue();
	
	public Calendar getDateValue();
	
	public Boolean getBooleanValue();
	
	public Object getValue();
}
