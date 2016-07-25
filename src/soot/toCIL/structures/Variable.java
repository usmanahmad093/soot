package soot.toCIL.structures;

/**
 * realizes a CIL Variable. a variable could be a Field or a local Variable
 * @author Usman
 *
 */
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
