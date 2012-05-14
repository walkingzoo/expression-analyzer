package neu.sxc.expression.syntax.function;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.syntax.Executable;
import neu.sxc.expression.tokens.Valuable;

public class FunctionRunner implements Executable {
	
	Function function;
	
	public FunctionRunner() {}
	
	public FunctionRunner(Function function) {
		this.function = function;
	}
	
	public void setFunction(Function function) {
		this.function = function;
	}

	public int getArgumentNum() {
		return function.getArgumentNum();
	}

	public Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException {
		return function.execute(arguments);
	}

}
