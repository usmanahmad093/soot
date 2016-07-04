package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Ldelem implements Instruction {
	private Stmt stmt;
	private Label label;
	
	public Ldelem(Stmt stmt) {
		this.stmt = stmt;
	}
	
	@Override
	public String getInstruction() {
		return "ldelem.i4";
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
