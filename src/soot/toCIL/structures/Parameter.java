package soot.toCIL.structures;

public class Parameter extends Variable {
	private int index;

	public Parameter(int index, String variableName, String variableType, boolean isFinal) {
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

	@Override
	protected String getCILRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getParamIndex() {
		return index;
	}

}
