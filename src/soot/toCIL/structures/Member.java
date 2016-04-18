package soot.toCIL.structures;

import soot.SootField;

public class Member extends Variable{

	private String modifier;
	private boolean isFinal;
	private boolean isStatic;
	private String cilField;
	
	public Member(String cilField, String variableName, String variableType, boolean isFinal, boolean isStatic) {
		super(variableName, variableType, isFinal);
		this.isFinal = isFinal;
		this.isStatic = isStatic;
		this.cilField = cilField;
	}

	public String getModifier() {
		return modifier;
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
