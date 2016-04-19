package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Member;

public class FieldInit implements Instruction{
	private Member member;
	
	public FieldInit(Member member) {
		// TODO Auto-generated constructor stub
		this.member = member;
	}
	
	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return null;
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
		
		return ".field private " + member.getVariableType() + " " + member.getVariableName();
	}

}
