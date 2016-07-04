package soot.toCIL;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class StelemRef implements Instruction {
	private Stmt stmt;
	private Label label;
	
	
	public StelemRef(Stmt stmt) {
		this.stmt = stmt;
	}
	
	@Override
	public String getInstruction() {
		return "stelem.ref";
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
