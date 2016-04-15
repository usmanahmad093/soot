package soot.toCIL.structures;

import soot.SootField;

public class Member extends Variable {

	private String modifier;
	private boolean isFinal;
	private boolean isStatic;
	
	public Member(SootField sootField, String variableName, String variableType, boolean isFinal, boolean isStatic) {
		super(variableName, variableType, isFinal);
		this.isFinal = isFinal;
		this.isStatic = isStatic;
		this.modifier = getModifierInString(sootField);
	}
	
	private String getModifierInString(SootField sootField) {
		String modifier = "";
		
		if (sootField.isPrivate()) {
			modifier = "private";
		} else if (sootField.isProtected()) {
			modifier = "family";
		} else if (sootField.isPublic()) {
			modifier = "public";
		}
		
		
		return modifier;
	}

	public String getModifier() {
		return modifier;
	}
	
	public boolean isStatic() {
		return isStatic;
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
}
