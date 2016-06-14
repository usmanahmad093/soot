package soot.toCIL.instructions.catchsection;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class EndCatchSection implements Instruction {

	private Stmt stmt;
	private Label label;
	
	public EndCatchSection(Stmt stmt) {
		this.stmt = stmt;
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
		return "}";
	}

}
