package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Type;

public class Callvirt implements Instruction {

	private String returnType;
	private String className;
	private String methodName;
	private Label label;
	private Stmt stmt;
	private ArrayList<String> paramTypes;

	public Callvirt(String returnType, String className, String methodName, Stmt stmt, ArrayList<String> paramTypes) {
		this.returnType = returnType;
		this.className = className;
		this.methodName = methodName;
		this.stmt = stmt;
		this.paramTypes = paramTypes;
	}

	@Override
	public String getInstruction() {
		String statement = "callvirt instance " + returnType + " " + className + "::" + methodName + "(";

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
