package soot.toCIL.structures;

public class CILField extends Variable{
private String cilField;
	
	public CILField(String cilField, String variableName, String variableType) {
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
