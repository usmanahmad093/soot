package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Shr implements Instruction {

	private Stmt stmt;
	private Label label;
	
	public Shr(Stmt stmt) {
		this.stmt = stmt;
	}
	
	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "shr";
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
