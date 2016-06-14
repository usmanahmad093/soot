package soot.toCIL.instructions.catchsection;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class BeginCatchSection implements Instruction {

	private Stmt stmt;
	private Label label;
	private String type;
	
	public BeginCatchSection(Stmt stmt, String type) {
		this.stmt = stmt;
		this.type = type;
	}

	@Override
	public Stmt getStmt() {
		return stmt;
	}

	@Override
	public void setLabel(Label label) {
		label = new Label();
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label.getLabel();
	}

	@Override
	public String getInstruction() {
		return "catch " + type + " {";
	}

}
