package neu.sxc.expression.tokens;

import neu.sxc.expression.syntax.function.Function;

public final class FunctionToken extends TerminalToken {

	private final Function function;
	
	public FunctionToken(TokenBuilder builder) {
		super(builder);
		function = builder.getFunction();
	}
	
	public Function getFunction() {
		return function;
	}
	
	public TokenType getTokenType() {
		return TokenType.FUNCTION;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		return target instanceof FunctionToken;
	}

}
