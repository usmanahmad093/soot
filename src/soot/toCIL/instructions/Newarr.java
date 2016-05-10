package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Newarr implements Instruction {
	private Stmt stmt;
	private Label label;
	private String type;
	
	public Newarr(Stmt stmt, String type) {
		this.stmt = stmt;
		this.type = type;
	}
	
	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "newarr " + type;
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
