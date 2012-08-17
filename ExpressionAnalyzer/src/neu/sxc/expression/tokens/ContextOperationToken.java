package neu.sxc.expression.tokens;

/**
 * 上下文操作符号
 * @author shanxuecheng
 *
 */
public class ContextOperationToken implements Token {
	
	ContextOperation control;
	
	public ContextOperationToken(TokenBuilder builder) {
		control = builder.getControl();
	}
	
	public ContextOperationToken(ContextOperation control) {
		this.control = control;
	}

	public TokenType getTokenType() {
		return TokenType.CONTEXT_OPERATION;
	}
	
	public ContextOperation getControl() {
		return control;
	}
}
