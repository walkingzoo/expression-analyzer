package neu.sxc.expression.tokens;

import neu.sxc.expression.syntax.Executable;

/**
 * 可执行动作，包括函数和操作符的执行
 * @author shanxuecheng
 *
 */
public final class ExecutionToken implements Token {
	
	private Executable executable;
	
	public ExecutionToken(TokenBuilder builder) {
		this.executable = builder.getExecutable();
	}
	
	public ExecutionToken(Executable executable) {
		this.executable = executable;
	}
	
	public Executable getExecutable() {
		return executable;
	}

	public final TokenType getTokenType() {
		return TokenType.EXECUTION;
	}

}
