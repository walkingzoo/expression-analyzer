package neu.sxc.expression.syntax.operator;

/**
 * 一元操作符
 * @author shanxuecheng
 *
 */
public abstract class UnaryOperator extends Operator {

	public UnaryOperator(String operatorName) {
		super(operatorName);
	}

	public final int getArgumentNum() {
		return 1;
	}

}
