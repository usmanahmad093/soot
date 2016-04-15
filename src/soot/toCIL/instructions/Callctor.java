package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class Callctor implements Instruction {
	
	private String returnType;
	private String className;
	private Label label;
	private Stmt stmt;
	private ArrayList<String> paramTypes;

	public Callctor(String returnType, String className, Stmt stmt, ArrayList<String> paramTypes) {
		this.returnType = returnType;
		this.className = className;
		this.stmt = stmt;
		this.paramTypes = paramTypes;
	}

	@Override
	public String getInstruction() {
		String statement = "call instance " + returnType + " " + className + "::.ctor(";

		boolean firstArgument = true;

		for (String argumentType : paramTypes) {
			String comma = (firstArgument) ? "" : ", ";
			statement += comma + argumentType;
			firstArgument = false;
		}

		statement += ")";

		return statement;
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
