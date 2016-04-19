package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.LocalVariables;

public class StoreInstruction implements Instruction {

	private int index;
	private Stmt stmt;
	private Label label;
	private LocalVariables otherVariable;
	
	public StoreInstruction(LocalVariables otherVariable, Stmt stmt) {
		this.otherVariable = otherVariable;
		this.stmt = stmt;
	}
	
	
	
	
	public int getIndex() {
		return index;
	}
	
	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		
		return "stloc.s " + otherVariable.getVariableName();
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
