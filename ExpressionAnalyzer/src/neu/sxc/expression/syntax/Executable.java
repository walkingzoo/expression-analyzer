package neu.sxc.expression.syntax;

import neu.sxc.expression.tokens.Valuable;

public interface Executable {
	
	public int getArgumentNum();
	
	public Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException;
	
}
