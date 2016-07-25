package soot.toCIL.structures;



/**
 * represents a CIL Field 
 * @author Usman
 *
 */
public class Member extends Variable{

	private String cilField;
	
	public Member(String cilField, String variableName, String variableType) {
		super(variableName, variableType);
		this.cilField = cilField;
	}

	@Override
	public String getVariableName() {
		return variableName;
	}

	@Override
	public String getVariableType() {
		return variableType;
	}
	
	public String getCILRepresentation() {
		return cilField;
	}


}
