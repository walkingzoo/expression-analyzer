package neu.sxc.expression.tokens;

/**
 * 流程控制类型
 * @author shanxuecheng
 *
 */
public enum Control {
	/**
	 * if条件
	 */
	IF_CONDITION,
	
	/**
	 * else条件
	 */
	ELSE_CONDITION,
	
	/**
	 * 新建上下文
	 */
	NEW_CONTEXT,
	
	/**
	 * 上下文结束
	 */
	END_CONTEXT,
	
	/**
	 * if语句结束
	 */
	END_IF
}
