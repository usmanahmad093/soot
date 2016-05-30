package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.LocalVariables;

public class LocalsInit implements Instruction {

	private ArrayList<LocalVariables> localVariables;
	
	public LocalsInit(ArrayList<LocalVariables> localVariables) {
		// TODO Auto-generated constructor stub
		this.localVariables = (localVariables != null)? localVariables: new ArrayList<>();
	}
	
	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setLocalVariables(ArrayList<LocalVariables> localVariables) {
		this.localVariables = localVariables;
	}
	
	public void addLocalVariable(LocalVariables localVar) {
		this.localVariables.add(localVar);
	}
	
	public ArrayList<LocalVariables> getLocalVariables() {
		return localVariables;
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
		// TODO Auto-generated method stub
		String statement = ".locals init (";
		Integer index = 0;
		
		for(LocalVariables v: localVariables) {
			String comma = (index == 0) ? "": ",";
			statement += comma + "[" + index.toString() + "] " + v.getVariableType() + " " + v.getVariableName(); 
			index++;
		}
		
		statement += ")";
		
		return statement;
	}

}
