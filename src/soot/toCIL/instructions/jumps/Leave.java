package soot.toCIL.instructions.jumps;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class Leave implements Instruction {
	private String targetLabel;
	private Stmt stmt;
	private Label label;

	public Leave (String targetLabel, Stmt stmt) {
		this.targetLabel = targetLabel;
		this.stmt = stmt;
	}

	@Override
	public String getInstruction() {
		return "leave " + targetLabel;
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
	public String getLabelWithoutSemicolon() {
		// TODO Auto-generated method stub
		return label.getLabel();
	}
	
	@Override
	public Label getLabelObject() {
		// TODO Auto-generated method stub
		return label;
	}
}
