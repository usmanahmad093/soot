package soot.toCIL.instructions;

import java.util.ArrayList;
import java.util.List;

import soot.Value;
import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Type;

public class Call implements Instruction{
	private String className;
	private String returnType;
	private String methodName;
	private Label label;
	private Stmt stmt;
	private ArrayList<String> paramTypes;
	
	public Call(String className, String returnType, String methodName, Stmt stmt, ArrayList<String> paramTypes) {
		this.className = className;
		this.returnType = returnType;
		this.methodName = methodName;
		this.stmt = stmt;
		this.paramTypes = paramTypes;
	}
	
	public String getReturnType() {
		return returnType;
	}

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		String statement =  "call " + returnType + " " + className + "::" + methodName + "(";
	    boolean firstArgument = true;
		
		for(String argumentType: paramTypes) {
			String comma = (firstArgument)? "": ", ";
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
