package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Pop implements Instruction{
	
	private Stmt stmt;
	private Label label;
	
	public Pop(Stmt stmt) {
		this.stmt = stmt;
	}

	@Override
	public Stmt getStmt() {
		return stmt;
	}

	@Override
	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label.getLabel() + ": ";
	}

	@Override
	public String getInstruction() {
		return "pop";
	}

}
