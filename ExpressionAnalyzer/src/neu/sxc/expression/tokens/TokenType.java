package neu.sxc.expression.tokens;

public enum TokenType {
	NT,//非终结符
	EXECUTION,//动作
	CONTROLLER,//流程控制
	KEY,//关键字
	DELIMITER,//界符
	FUNCTION,//函数
	CONST,//常量
	VARIABLE,//变量
	RUNTIME_VALUE//运行时结果
}
