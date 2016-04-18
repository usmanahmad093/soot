package soot.toCIL.structures;

public class Parameter extends Variable {
	private int index;

	public Parameter(String variableName, String variableType, boolean isFinal, int index) {
		super(variableName, variableType, isFinal);
		this.index = index;
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
	
	public int getIndex() {
		return index;
	}

	@Override
	protected String getCILRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

}
