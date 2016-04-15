package soot.toCIL.instructions.jumps;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class Blt implements Instruction {

	private Stmt stmt;
	private Label label;
	private Label targetLabel;
	
	public Blt(Label targetLabel, Stmt stmt) {
		this.targetLabel = targetLabel;
		this.stmt = stmt;
	}
	
	@Override
	public String getInstruction() {
		return "blt " + targetLabel.getLabel();
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
