package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Stfld implements Instruction {
	
	private Label label;
	private Stmt stmt;
	private String returnType;
	private String className;
	private String attributeName;

	public Stfld(Stmt stmt, String returnType, String className, String attributeName) {
		this.stmt = stmt;
		this.returnType = returnType;
		this.className = className;
		this.attributeName = attributeName;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "stfld " + returnType + " " + className + "::" + attributeName;
	}

	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return stmt;
	}

	@Override
	public void setLabel(Label label) {
		// TODO Auto-generated method stub
		this.label = label;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return label.getLabel() + ": ";
	}

}
