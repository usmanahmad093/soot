package soot.toCIL.structures;

public abstract class Variable {
	protected String variableName;
	protected String variableType;
	protected boolean isFinal;
	
	public Variable(String variableName, String variableType, boolean isFinal) {
		this.variableName = variableName;
		this.variableType = variableType;
		this.isFinal = isFinal;
	}
	
	public abstract String getVariableName();
	public abstract String getVariableType();
	public abstract boolean isFinal();
	
	
}
