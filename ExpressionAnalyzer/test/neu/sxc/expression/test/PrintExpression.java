package neu.sxc.expression.test;

import java.util.List;
import java.util.Map.Entry;

import neu.sxc.expression.Expression;
import neu.sxc.expression.tokens.RuntimeValue;
import neu.sxc.expression.tokens.TerminalToken;
import neu.sxc.expression.tokens.Token;
import neu.sxc.expression.tokens.Valuable;
import neu.sxc.expression.tokens.ValueToken;
import neu.sxc.expression.utils.DataCache;


public class PrintExpression {
	public static void printExp(Expression exp) {
		
		printDatas();
		
		printTokens(exp.getTokens());
		
		if(exp.getFinalResult() != null) {
			Printer.println("----------Final Result-----------");
			printToken(exp.getFinalResult());
		}
		
		if(exp.getVariableTable().size()>0) {
			Printer.println("----------Variable Values-----------");
			for(Entry<String, Valuable> variable : exp.getVariableTable().entrySet()) {
				Printer.print(variable.getKey() + ":");
				printToken(variable.getValue());
			}
		}
	}
	
	public static void printDatas() {
		DataCache.print();
	}
	
	public static void printTokens(List<TerminalToken> tokens) {
		if(tokens.size() > 0) {
			Printer.println("----------Tokens-----------");
			for(Token token : tokens)
				printToken(token);
		}
	}
	
	private static void printToken(Token token) {
		Printer.print("[" + token.getTokenType().name() + "]");
		switch(token.getTokenType()){
		case CONST:
			ValueToken constToken = (ValueToken)token;
			Printer.print(", DateType:" + constToken.getDataType().name());
			Printer.println(", line:" + constToken.getLine() + ", column:" + constToken.getColumn() + ", text: " + constToken.getText()
					+ ", index: " + constToken.getIndex());
			break;
		case VARIABLE:
			ValueToken variableToken = (ValueToken)token;
			if(variableToken.getDataType() != null)
				Printer.print(", DateType:" + variableToken.getDataType().name());
			Printer.print(", line:" + variableToken.getLine() + ", column:" + variableToken.getColumn() + ", text: " + variableToken.getText());
			if(variableToken.getIndex() >= 0)
				Printer.println(", index: " + variableToken.getIndex());
			else
				Printer.println();
			break;
		case RUNTIME_VALUE:
			RuntimeValue resultToken = (RuntimeValue)token;
			Printer.print(", DateType:" + resultToken.getDataType().name());
			Printer.print(", index: " + resultToken.getIndex());
			Printer.println();
			break;
		default:
			TerminalToken terminaltoken = (TerminalToken)token;
			Printer.println(", line:" + terminaltoken.getLine() + ", column:" + terminaltoken.getColumn() + ", text: " + terminaltoken.getText());
			break;
		}
	}
}
