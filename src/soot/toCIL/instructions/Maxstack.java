package soot.toCIL.instructions;



import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Maxstack implements Instruction{
	private int maxStack;
	
	public Maxstack(int maxStack) {
		this.maxStack = maxStack;
	}
	
	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setMaxStack() {
		this.maxStack = maxStack;
	}

	@Override
	public void setLabel(Label label) {
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getInstruction() {
		
		return ".maxstack " + String.valueOf(maxStack);
	}
}
