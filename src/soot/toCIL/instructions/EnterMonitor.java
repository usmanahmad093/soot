package soot.toCIL.instructions;

import soot.jimple.Stmt;
import soot.toCIL.structures.Label;

public class EnterMonitor implements Instruction {
	private Stmt stmt;
	private Label label;
	

	public EnterMonitor(Stmt stmt) {
		this.stmt = stmt;
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

	@Override
	public String getInstruction() {
		// TODO Auto-generated method stub
		return "call void [mscorlib]System.Threading.Monitor::Enter(object) ";
	}
}
