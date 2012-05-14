package neu.sxc.expression.tokens;

public final class VariableToken extends ValueToken {
	
	private boolean toBeAssigned;

	public VariableToken(TokenBuilder builder) {
		super(builder);
		toBeAssigned = builder.isToBeAssigned();
	}
	
	public boolean isToBeAssigned() {
		return toBeAssigned;
	}
	
	public void setToBeAssigned(boolean toBeAssigned) {
		this.toBeAssigned = toBeAssigned;
	}
	
	public TokenType getTokenType() {
		return TokenType.VARIABLE;
	}
	
	public void assignWith(Valuable value) {
		setDataType(value.getDataType());
		setIndex(value.getIndex());
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		if(!(target instanceof VariableToken))
			return false;
		return this.isToBeAssigned() == ((VariableToken)target).isToBeAssigned();
	}

}
