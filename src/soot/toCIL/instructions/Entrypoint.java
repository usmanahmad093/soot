package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Entrypoint implements Instruction {

	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLabel(Label label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getLabelWithoutSemicolon() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Label getLabelObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return ".entrypoint";
	}

}
