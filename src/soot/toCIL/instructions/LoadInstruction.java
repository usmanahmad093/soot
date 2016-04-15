package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Constant;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.OtherVariables;
import soot.toCIL.structures.Variable;

public class LoadInstruction implements Instruction {

	private Constant constant;
	private Integer index;
	private Label label;
	private Stmt stmt;
	private Variable var;

	public LoadInstruction(Constant constant, Variable var, Stmt stmt) {
		this.constant = constant;
		this.var = var;
		this.stmt = stmt;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub

		if (constant != null) {
			switch (constant.getType()) {
			case STRING:
				return "ldstr \"" + constant.getValue() + "\"";

			case INT:
				return "ldc.i4 0x" + constant.getValue();

			case LONG:
				return "ldc.i8 0x" + constant.getValue();

			case FLOAT:
				return "ldc.r4 " + constant.getValue();

			case DOUBLE:
				return "ldc.r8 " + constant.getValue();

			}
		}

		return "ldloc.s " + var.getVariableName();
	}

	public boolean isVariable() {
		return (index != -1) ? true : false;
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
