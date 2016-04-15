package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Ceq implements Instruction {
	private Label label;
	private Stmt stmt;
	
	public Ceq(Stmt stmt) {
		this.stmt = stmt;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "ceq";
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
