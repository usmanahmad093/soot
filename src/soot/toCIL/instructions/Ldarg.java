package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Ldarg implements Instruction{

	private int index;
	private Stmt stmt;
	private Label label;
	
	public Ldarg(int index, Stmt stmt) {
		this.index = index;
		this.stmt = stmt;
	}
	
	
	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "ldarg." + String.valueOf(index);
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
	
	public int getIndex() {
		return index;
	}

}
