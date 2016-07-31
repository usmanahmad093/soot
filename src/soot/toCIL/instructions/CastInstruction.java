package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Type;

public class CastInstruction implements Instruction {
	private Label label;
	private Stmt stmt;
	private Type type;
	private String classType;

	public CastInstruction(Stmt stmt, Type type, String classType) {
		this.stmt = stmt;
		this.type = type;
		this.classType = classType;
	}

	@Override
	public String getInstruction() {
		String cilInstruction = "";

		switch (type) {

		case BYTE:
			cilInstruction = "conv.i1";
			break;

		case DOUBLE:
			cilInstruction = "conv.r8";
			break;

		case FLOAT:
			cilInstruction = "conv.r4";
			break;

		case INT:
			cilInstruction = "conv.i4";
			break;

		case LONG:
			cilInstruction = "conv.i8";
			break;

		case SHORT:
			cilInstruction = "conv.i2";
			break;

		case OTHER:
			cilInstruction = "castclass " + classType;
			break;
		}

		return cilInstruction;
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
	
	@Override
	public String getLabelWithoutSemicolon() {
		// TODO Auto-generated method stub
		return label.getLabel();
	}
	
	@Override
	public Label getLabelObject() {
		// TODO Auto-generated method stub
		return label;
	}
}
