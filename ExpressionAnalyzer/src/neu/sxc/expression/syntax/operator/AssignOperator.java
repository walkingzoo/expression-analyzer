package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.tokens.Valuable;
import neu.sxc.expression.tokens.VariableToken;

public class AssignOperator extends BinaryOperator {

	public AssignOperator() {
		super("ASSIGN");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		VariableToken variable = (VariableToken) arguments[0];
		Valuable value = arguments[1];
		if (variable.getIndex() < 0) {
			variable.assignWith(value);
		} else if (variable.getDataType() == value.getDataType()) {
			variable.assignWith(value);
		} else
			throw new ArgumentsMismatchException(
					"Type mismatch in assignment: cannot convert from "
							+ value.getDataType().name() + " to "
							+ variable.getDataType().name() + ".");
		return variable.getValue();
	}

}
