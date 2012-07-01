package neu.sxc.expression.syntax.function;

import neu.sxc.expression.syntax.ArgumentsMismatchException;
import neu.sxc.expression.syntax.Executable;
import neu.sxc.expression.tokens.DataType;
import neu.sxc.expression.tokens.TokenBuilder;
import neu.sxc.expression.tokens.Valuable;

public abstract class Function implements Executable{

	private final String functionName;
	
	private final DataType[] argumentsDataType;
	
	public Function(String functionName) {
		this.functionName = functionName;
		this.argumentsDataType = new DataType[0];
		checkFunctionDefinition();
	}
	
	public Function(String functionName, DataType[] argumentsDataType) {
		this.functionName = functionName;
		this.argumentsDataType = argumentsDataType;
		checkFunctionDefinition();
	}
	
	public String getName() {
		return functionName;
	}
	
	public final Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException {
		if(getArgumentNum() < 0) {
			for(Valuable argument : arguments) 
				if(argument.getDataType() != argumentsDataType[0])
					throw new ArgumentsMismatchException(arguments, toString());
		} else if(getArgumentNum() == arguments.length) {
			int argumentNum = getArgumentNum(); 
			for(int i=0; i<argumentNum; i++) {
				if(argumentsDataType[i] == DataType.ANY) {
					continue;
				} else if (argumentsDataType[i] != arguments[i].getDataType()){
					throw new ArgumentsMismatchException(arguments, toString());
				}
			}
		} else {
			throw new ArgumentsMismatchException(arguments, toString());
		}
		
		Object result = executeFunction(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	protected abstract Object executeFunction(Valuable[] arguments);
	
	private void checkFunctionDefinition() {
		if(getArgumentNum() >= 0){
			if(argumentsDataType.length != getArgumentNum()) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		} else {
			if(argumentsDataType.length != 1) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		}
	}
	
	@Override
	public final String toString() {
		StringBuilder signature = new StringBuilder();
		signature.append(functionName).append('(');
		if(getArgumentNum() >= 0) {
			int argumentNum = getArgumentNum();
			for(int i=0; i<argumentNum; i++) {
				if(i == argumentNum-1)
					signature.append(argumentsDataType[i].name());
				else
					signature.append(argumentsDataType[i].name()).append(',');
			}
		} else {
			signature.append(argumentsDataType[0].name()).append("...");
		}
		signature.append(')');
		return signature.toString();
	}

}
