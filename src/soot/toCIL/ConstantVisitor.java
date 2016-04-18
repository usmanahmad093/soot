package soot.toCIL;

import soot.jimple.AbstractConstantSwitch;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.toCIL.structures.Constant;
import soot.toCIL.structures.Type;


public class ConstantVisitor extends AbstractConstantSwitch {
	private StmtVisitor stmtV;
	private Stmt origStmt;
    private String instruction;
    private String constantType;
	
	public ConstantVisitor(StmtVisitor stmtV) {
		this.stmtV = stmtV;
	}
	
	public void setOriginStmt(Stmt origStmt) {
		this.origStmt = origStmt;
	}
	
	public void setInstruction(String instruction) {
    	this.instruction = instruction;
    }
	
	public void setConstantTypeInString(String constantType) {
		this.constantType = constantType;
	}
	
	public void caseStringConstant(StringConstant s) {
		Type type = Type.STRING;
		String value = String.valueOf(s.value);
		Constant constant = new Constant(type, value);
		stmtV.setConstant(constant);
	}
	
	public void caseLongConstant(LongConstant l) {
		Type type = Type.LONG;
		String value = String.valueOf(Long.toHexString(l.value));
		Constant constant = new Constant(type, value);
		stmtV.setConstant(constant);
		
	}
	
	public void caseDoubleConstant(DoubleConstant d) {
		Type type = Type.DOUBLE;
		String value = String.valueOf(d.value);
		Constant constant = new Constant(type, value);
		stmtV.setConstant(constant);
	}
	
	public void caseFloatConstant(FloatConstant f) {
		Type type = Type.FLOAT;
		String value = String.valueOf(f.value);
		Constant constant = new Constant(type, value);
		stmtV.setConstant(constant);
	}
	
	
	
	public void caseIntConstant(IntConstant i) {
		Type type = Type.INT;
		String value = String.valueOf(Integer.toHexString(i.value));
		Constant constant = new Constant(type, value);
		stmtV.setConstant(constant);
	}
	
	

	public void defaultCase(Object o) {
		// const* opcodes not used since there seems to be no point in doing so:
		// CONST_HIGH16, CONST_WIDE_HIGH16
		throw new Error("unknown Object (" + o.getClass() + ") as Constant: " + o);
	}
	
	
}
