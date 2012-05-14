package neu.sxc.expression.tokens;

public final class ConstToken extends ValueToken {

	public ConstToken(TokenBuilder builder) {
		super(builder);
	}
	
	public TokenType getTokenType() {
		return TokenType.CONST;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		return target instanceof ConstToken;
	}

}
