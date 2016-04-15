package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Type;

public class Newobj implements Instruction{

	private String returnType;
	private String className;
	private Stmt stmt;
	private Label label;
	private ArrayList<String> allArgumentTypes;
	
	public Newobj(String returnType, String className, ArrayList<String> allArgumentTypes, Stmt stmt) {
		this.returnType = returnType;
		this.className = className;
		this.stmt = stmt;
		this.allArgumentTypes = allArgumentTypes;
	}
	
	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		String statement = "newobj instance" + " " + returnType + " " + className + "::.ctor(";
		boolean firstArgument = true;
		
		for(String argumentType: allArgumentTypes) {
			String comma = (firstArgument)? "": ", ";
			statement += comma + argumentType;
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
	
	public String getClassName() {
		return className;
	}

}
