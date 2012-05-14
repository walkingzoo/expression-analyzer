package neu.sxc.expression.tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class NonterminalToken implements Token {
	
	private Map<TerminalToken[], Token[]> productions = new HashMap<TerminalToken[], Token[]>();
	
	public void addProduction(TerminalToken[] selectCollection, Token[] production) {
		productions.put(selectCollection, production);
	}
	
	public Token[] getProduction(TerminalToken target) {
		Set<TerminalToken[]> selectCollections = productions.keySet();
		for(TerminalToken[] selectCollection : selectCollections) {
			for(TerminalToken token : selectCollection)
				if(token.equalsInGrammar(target))
					return productions.get(selectCollection);
		}
		return null;
	}
	
	public TokenType getTokenType() {
		return TokenType.NT;
	}
	
}
