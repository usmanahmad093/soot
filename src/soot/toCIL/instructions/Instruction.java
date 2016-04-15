package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public interface Instruction {
	public Stmt getStmt();
	public void setLabel(Label label);
	public String getLabel();
	public String getInstruction();
}
