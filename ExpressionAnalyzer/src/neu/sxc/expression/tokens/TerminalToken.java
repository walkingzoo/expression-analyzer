package neu.sxc.expression.tokens;

public abstract class TerminalToken implements Token {

	private final int line;
	
	private final int column;
	
	private final String text;
	
	public TerminalToken(TokenBuilder builder) {
		line = builder.getLine();
		column = builder.getColumn();
		text = builder.getText();
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String getText() {
		return text;
	}
	
	public abstract boolean equalsInGrammar(TerminalToken target);
}
