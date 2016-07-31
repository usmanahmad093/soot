package soot.toCIL.instructions;

import java.util.ArrayList;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class ConsolePrintln implements Instruction {

	private Label label;
	private Stmt stmt;
	private ArrayList<String> paramTypes;
	private static String BYTE = "int8";

	public ConsolePrintln(Stmt stmt, ArrayList<String> paramTypes) {
		this.stmt = stmt;
		this.paramTypes = paramTypes;
	}

	@Override
	public String getInstruction() {
		String statement = "call void [mscorlib]System.Console::WriteLine(";

		boolean firstArgument = true;

		for (String argumentType : paramTypes) {
			argumentType = (argumentType.equals(BYTE))?"int32":argumentType;
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
