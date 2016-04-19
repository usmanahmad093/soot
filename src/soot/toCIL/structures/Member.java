package soot.toCIL.structures;



public class Member extends Variable{

	private boolean isFinal;
	private String cilField;
	
	public Member(String cilField, String variableName, String variableType, boolean isFinal, boolean isStatic) {
		super(variableName, variableType, isFinal);
		this.isFinal = isFinal;
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

	@Override
	public boolean isFinal() {
		return isFinal;
	}
	
	
	public String getCILRepresentation() {
		return cilField;
	}


}
