package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

/**
 * represents a CIL Instruction. Every Concrete Class implements this Interface which realises a CIL Instruction
 * @author Usman
 *
 */
public interface Instruction {
	public Stmt getStmt();
	public void setLabel(Label label);
	public String getLabel();
	public String getLabelWithoutSemicolon();
	public String getInstruction();
	public Label getLabelObject();
}
