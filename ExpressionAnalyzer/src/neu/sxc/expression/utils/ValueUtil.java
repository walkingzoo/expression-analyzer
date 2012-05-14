package neu.sxc.expression.utils;

import java.math.BigDecimal;
import java.util.Calendar;

import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.Valuable;


public class ValueUtil {
	public static BigDecimal getNumberValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.NUMBER
				&& valuable.getIndex() >= 0)
			return DataCache.getBigDecimalValue(valuable.getIndex());
		return null;
	}
	
	public static String getStringValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.STRING
				&& valuable.getIndex() >= 0)
			return DataCache.getStringValue(valuable.getIndex());
		return null;
	}
	
	public static Character getCharValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.CHARACTER 
				&& valuable.getIndex() >= 0)
			return DataCache.getCharValue(valuable.getIndex());
		return null;
	}
	
	public static Calendar getDateValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.DATE 
				&& valuable.getIndex() >= 0)
			return DataCache.getDateValue(valuable.getIndex());
		return null;
	}
	
	public static Boolean getBooleanValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.BOOLEAN
				&& valuable.getIndex() >= 0)
			return valuable.getIndex()==0 ? false : true;
		return null;
	}
	
	public static Object getValue(Valuable valuable) {
		Object value = null;
		switch(valuable.getDataType()) {
		case NUMBER:
			value = getNumberValue(valuable);
			break;
		case STRING:
			value = getStringValue(valuable);
			break;
		case CHARACTER:
			value = getCharValue(valuable);
			break;
		case BOOLEAN:
			value = getBooleanValue(valuable);
			break;
		case DATE:
			value = getDateValue(valuable);
			break;
		}
		return value;
	}
}
