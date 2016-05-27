package soot.toCIL.structures;

public abstract class Variable {
	protected String variableName;
	protected String variableType;
	
	public Variable(String variableName, String variableType) {
		this.variableName = variableName;
		this.variableType = variableType;
	}
	
	public abstract String getVariableName();
	public abstract String getVariableType();
	protected abstract String getCILRepresentation();
	
	
}
