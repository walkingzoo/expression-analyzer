package neu.sxc.expression.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import neu.sxc.expression.test.Printer;


public class DataCache {
	private static List<BigDecimal> bigDecimalCache = Collections.synchronizedList(new ArrayList<BigDecimal>());

	private static List<String> stringCache = Collections.synchronizedList(new ArrayList<String>());

	private static List<Character> charCache = Collections.synchronizedList(new ArrayList<Character>());

	private static List<Calendar> dateCache = Collections.synchronizedList(new ArrayList<Calendar>());

	public static int getBigDecimalIndex(BigDecimal val) {
		int index = bigDecimalCache.indexOf(val);
		if (index < 0) {
			bigDecimalCache.add(val);
			return bigDecimalCache.size() - 1;
		}
		return index;
	}

	public static BigDecimal getBigDecimalValue(int index) {
		if (index < 0 || index >= bigDecimalCache.size())
			return null;
		return bigDecimalCache.get(index);
	}

	public static int getStringIndex(String val) {
		int index = stringCache.indexOf(val);
		if (index < 0) {
			stringCache.add(val);
			return stringCache.size() - 1;
		}
		return index;
	}

	public static String getStringValue(int index) {
		if (index < 0 || index >= stringCache.size())
			return null;
		return stringCache.get(index);
	}

	public static int getCharIndex(Character val) {
		int index = charCache.indexOf(val);
		if (index < 0) {
			charCache.add(val);
			return charCache.size() - 1;
		}
		return index;
	}

	public static Character getCharValue(int index) {
		if (index < 0 || index >= charCache.size())
			return null;
		return charCache.get(index);
	}

	public static int getDateIndex(Calendar val) {
		int index = dateCache.indexOf(val);
		if (index < 0) {
			dateCache.add(val);
			return dateCache.size() - 1;
		}
		return index;
	}

	public static Calendar getDateValue(int index) {
		if (index < 0 || index >= dateCache.size())
			return null;
		return dateCache.get(index);
	}

	public static int getBooleanIndex(Boolean val) {
		return val ? 1 : 0;
	}

	public static Boolean getBooleanValue(int index) {
		if (index < 0)
			return null;
		return index == 0 ? false : true;
	}

	public static void print() {
		if (bigDecimalCache.size() > 0) {
			Printer.println("Number:");
			Printer.println(DataCache.bigDecimalCache);
		}
		if (dateCache.size() > 0) {
			Printer.println("Date:");
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			List<Calendar> dates = DataCache.dateCache;
			for (Calendar date : dates)
				Printer.println(format.format(date.getTime()));
		}
		if (stringCache.size() > 0) {
			Printer.println("String:");
			Printer.println(DataCache.stringCache);
		}
		if (charCache.size() > 0) {
			Printer.println("Char:");
			Printer.println(DataCache.charCache);
		}
	}
}
