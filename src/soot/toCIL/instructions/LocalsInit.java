package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.OtherVariables;

public class LocalsInit implements Instruction {

	private ArrayList<OtherVariables> localVariables;
	
	public LocalsInit(ArrayList<OtherVariables> localVariables) {
		// TODO Auto-generated constructor stub
		this.localVariables = (localVariables != null)? localVariables: new ArrayList<>();
	}
	
	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setLocalVariables(ArrayList<OtherVariables> localVariables) {
		this.localVariables = localVariables;
	}
	
	public ArrayList<OtherVariables> getLocalVariables() {
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
		
		for(OtherVariables v: localVariables) {
			String comma = (index == 0) ? "": ",";
			statement += comma + "[" + index.toString() + "] " + v.getVariableType() + " " + v.getVariableName(); 
			index++;
		}
		
		statement += ")";
		
		return statement;
	}

}
