package neu.sxc.expression.lexical.dfa;

import java.util.HashMap;
import java.util.Map;

public class DFAMidState {
	private DFAMidStateCode midStateCode = null;
	
	private Map<String,DFAMidState> nextMidStateMap = new HashMap<String,DFAMidState>();
	
	private RouteToEndState routeToEndState;
	
	private String errorMessage = "Lexical error.";
	
	public DFAMidState(DFAMidStateCode midStateCode) {
		this.midStateCode = midStateCode;
	}
	
	public void setNextMidState(String pattern,DFAMidState nextState) {
		nextMidStateMap.put(pattern, nextState);
	}
	
	public DFAMidState getNextMidState(Character inputChar) {
		DFAMidState nextState = null;
		for(String pattern : nextMidStateMap.keySet()){
			if(inputChar.toString().matches(pattern)){
				nextState = nextMidStateMap.get(pattern);
				break;
			}
		}
		return nextState;
	}
	
	public void setRouteToEndState(String route, DFAEndStateCode endStateCode) {
		routeToEndState = new RouteToEndState(route, endStateCode);
	}
	
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(routeToEndState == null)
			return null;
		return routeToEndState.goToEndStateWithInput(inputChar);
	}
	
	public DFAEndStateCode getNextEndStateCode() {
		if(routeToEndState == null)
			return null;
		return routeToEndState.getEndStateCode();
	}
	
	public boolean hasRouteToEndState() {
		return routeToEndState != null;
	}
	
	public DFAMidStateCode getMidStateCode() {
		return midStateCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
