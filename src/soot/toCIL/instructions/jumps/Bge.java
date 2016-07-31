package soot.toCIL.instructions.jumps;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class Bge implements Instruction {

	private Stmt stmt;
	private Label label;
	private Label targetLabel;
	
	public Bge(Label targetLabel, Stmt stmt) {
		this.targetLabel = targetLabel;
		this.stmt = stmt;
	}
	
	@Override
	public String getInstruction() {
		return "bge " + targetLabel.getLabel();
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
