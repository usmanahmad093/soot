package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Isinst implements Instruction {

	private Label label;
	private Stmt stmt;
	private String className;
	
	public Isinst(Stmt stmt, String className) {
		this.stmt = stmt;
		this.className = className;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "isinst " + className;
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
