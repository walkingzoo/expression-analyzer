package neu.sxc.expression.tokens;

/**
 * 流程控制符号
 * @author shanxuecheng
 *
 */
public class ControlToken implements Token {
	
	Control control;
	
	public ControlToken(TokenBuilder builder) {
		control = builder.getControl();
	}
	
	public ControlToken(Control control) {
		this.control = control;
	}

	public TokenType getTokenType() {
		return TokenType.CONTROLLER;
	}
	
	public Control getControl() {
		return control;
	}
}
