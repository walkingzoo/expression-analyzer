package neu.sxc.expression.test;

import java.util.Calendar;
import java.util.Date;

import neu.sxc.expression.syntax.function.Function;
import neu.sxc.expression.tokens.Valuable;

public class CurrentDate extends Function {
	public CurrentDate() {
		super("getDate");
	}
	public int getArgumentNum() {
		return 0;
	}
	@Override
	protected Object executeFunction(Valuable[] arguments) {
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		return date;
	}
}
