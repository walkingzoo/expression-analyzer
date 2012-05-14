package neu.sxc.expression.lexical.dfa;

public class RouteToEndState {
	private String route;
	
	private DFAEndStateCode endStateCode;
	
	public RouteToEndState(String route, DFAEndStateCode endStateCode) {
		this.route = route;
		this.endStateCode = endStateCode;
	}

	public DFAEndStateCode getEndStateCode() {
		return endStateCode;
	}
	
	public String getRoute() {
		return route;
	}
	
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(inputChar.toString().matches(route))
			return endStateCode;
		return null;
	}
}
