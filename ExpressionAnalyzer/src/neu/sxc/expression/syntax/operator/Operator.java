package neu.sxc.expression.syntax.operator;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.syntax.Executable;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.Valuable;

public abstract class Operator implements Executable {

	private final String operatorName;

	public Operator(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = operate(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	protected abstract Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException;

}
