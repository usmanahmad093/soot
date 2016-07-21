package soot.toCIL.instructions;


import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class ReadKey implements Instruction{

	private Label label;
	private Stmt stmt;

	public ReadKey(Stmt stmt) {
		this.stmt = stmt;
	}

	@Override
	public String getInstruction() {
		return "call valuetype [mscorlib]System.ConsoleKeyInfo [mscorlib]System.Console::ReadKey()";
	}

	@Override
	public Stmt getStmt() {
		return stmt;
	}

	@Override
	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label.getLabel() + ": ";
	}
	
}