package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Stsfld implements Instruction {
	private Label label;
	private Stmt stmt;
	private String returnType;
	private String className;
	private String attributeName;

	public Stsfld(Stmt stmt, String returnType, String className, String attributeName) {
		this.stmt = stmt;
		this.returnType = returnType;
		this.className = className;
		this.attributeName = attributeName;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "stsfld " + returnType + " " + className + "::" + attributeName;
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
