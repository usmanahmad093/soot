package soot.toCIL.structures;

/**
 * represents a Local Variable in CIL
 * @author Usman
 *
 */
public class LocalVariables extends Variable {

	private boolean isThisRef;
	
	public LocalVariables(String variableName, String variableType) {
		super(variableName, variableType);
		this.isThisRef = true;
	}

	@Override
	public String getVariableName() {
		// TODO Auto-generated method stub
		return variableName;
	}

	@Override
	public String getVariableType() {
		// TODO Auto-generated method stub
		return variableType;
	}



	@Override
	protected String getCILRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public void notAssignedByThisRef() {
		this.isThisRef = false;
	}
	
	public boolean assignedByThisRef() {
		return isThisRef;
	}

}
