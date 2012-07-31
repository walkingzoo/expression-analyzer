package neu.sxc.expression.syntax.operator;

/**
 * 二元操作符
 * @author shanxuecheng
 *
 */
public abstract class BinaryOperator extends Operator {

	public BinaryOperator(String operator) {
		super(operator);
	}

	public final int getArgumentNum() {
		return 2;
	}

}
