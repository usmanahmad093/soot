package soot.toCIL.structures;

public class LocalVariables extends Variable {

	private boolean isThisRef;
	
	public LocalVariables(String variableName, String variableType, boolean isFinal) {
		super(variableName, variableType, isFinal);
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
	public boolean isFinal() {
		// TODO Auto-generated method stub
		return isFinal;
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
