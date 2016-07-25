/* Soot - a J*va Optimization Framework
 * Copyright (C) 2008 Ben Bellamy 
 * 
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.toCIL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jf.dexlib2.Opcode;

import com.sun.org.apache.bcel.internal.generic.StoreInstruction;

import soot.DoubleType;
import soot.FloatType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.EqExpr;
import soot.jimple.Expr;
import soot.jimple.ExprSwitch;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.OrExpr;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.SubExpr;
import soot.jimple.UshrExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.toCIL.instructions.AddInstruction;
import soot.toCIL.instructions.And;
import soot.toCIL.instructions.Call;
import soot.toCIL.instructions.Callctor;
import soot.toCIL.instructions.Callvirt;
import soot.toCIL.instructions.CastInstruction;
import soot.toCIL.instructions.Ceq;
import soot.toCIL.instructions.Cgt;
import soot.toCIL.instructions.Clt;
import soot.toCIL.instructions.ConsolePrintln;
import soot.toCIL.instructions.Div;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.Isinst;
import soot.toCIL.instructions.Ldarg;
import soot.toCIL.instructions.Ldlen;
import soot.toCIL.instructions.LoadInstruction;
import soot.toCIL.instructions.Mul;
import soot.toCIL.instructions.Neg;
import soot.toCIL.instructions.Newarr;
import soot.toCIL.instructions.Newobj;
import soot.toCIL.instructions.Nop;
import soot.toCIL.instructions.Or;
import soot.toCIL.instructions.Pop;
import soot.toCIL.instructions.ReadKey;
import soot.toCIL.instructions.Rem;
import soot.toCIL.instructions.Shl;
import soot.toCIL.instructions.Shr;
import soot.toCIL.instructions.ShrUn;
import soot.toCIL.instructions.Stelem;
import soot.toCIL.instructions.Sub;
import soot.toCIL.instructions.Xor;
import soot.toCIL.instructions.jumps.Beq;
import soot.toCIL.instructions.jumps.Bge;
import soot.toCIL.instructions.jumps.Bgt;
import soot.toCIL.instructions.jumps.Ble;
import soot.toCIL.instructions.jumps.Blt;
import soot.toCIL.instructions.jumps.Bne;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.LocalVariables;
import soot.toCIL.structures.Method;
import soot.toCIL.structures.Parameter;


/**
 * determines the Jimple Expression Type with the help of this Visitor Class
 * @author Usman
 *
 */
public class ExprVisitor implements ExprSwitch {

	private ConstantVisitor constantV;
	private Stmt originStmt;
	private StmtVisitor stmtV;
	private Stmt target;
	private boolean trueComparision;
	private Method m;

	private ArrayList<Instruction> CILInstructionsForCMPExpr = new ArrayList<>();

	public ExprVisitor(StmtVisitor stmtV, ConstantVisitor constantV, Method m) {
		this.stmtV = stmtV;
		this.constantV = constantV;
		this.trueComparision = false;
		this.m = m;
	}

	public ArrayList<Instruction> getCILInstructionsForCMPExpr() {
		return CILInstructionsForCMPExpr;
	}

	public void setOriginStmt(Stmt originStmt) {
		this.originStmt = originStmt;
	}

	public void setTarget(Stmt target) {
		this.target = target;
	}

	private void setTrueComparision(boolean v) {
		this.trueComparision = v;
	}

	public boolean isTrueComparision() {
		return trueComparision;
	}

	@Override
	public void caseAddExpr(AddExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		AddInstruction addInstr = new AddInstruction(originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(addInstr);
	}

	@Override
	public void caseAndExpr(AndExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		And andInstruction = new And(originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(andInstruction);

	}

	// TODO: Bsp java code: boolean var = (a >b) System.out.println(var);

	@Override
	public void caseCmpExpr(CmpExpr v) {

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction ldVariable1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction ldVariable2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Cgt cgtInstruction = new Cgt(originStmt);
		// Brtrue Label
		Pop popInstruction = new Pop(originStmt);
		Ceq ceqInstruction = new Ceq(originStmt);
		// BrTrue label
		LoadInstruction loadMinusOne = new LoadInstruction(
				new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT, "0xFFFFFF"), null, originStmt);
		// Br Label
		LoadInstruction LoadZiro = new LoadInstruction(
				new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT, "0"), null, originStmt);
		Mul mulInstruction = new Mul(originStmt);

		LoadZiro = (LoadInstruction) LabelAssigner.getInstance().AssignLabelToInstruction(LoadZiro);

		CILInstructionsForCMPExpr.add(ldVariable1);
		CILInstructionsForCMPExpr.add(ldVariable2);
		CILInstructionsForCMPExpr.add(cgtInstruction);
		CILInstructionsForCMPExpr.add(popInstruction);
		CILInstructionsForCMPExpr.add(ceqInstruction);
		CILInstructionsForCMPExpr.add(loadMinusOne);
		CILInstructionsForCMPExpr.add(LoadZiro);
		CILInstructionsForCMPExpr.add(mulInstruction);

	}

	@Override
	public void caseCmpgExpr(CmpgExpr v) {
		System.out.println("CmpgExpr");

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadOperand1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadOperand2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Cgt cgtInstr = new Cgt(originStmt);

		stmtV.buildInstruction(loadOperand1);
		stmtV.buildInstruction(loadOperand2);
		stmtV.buildInstruction(cgtInstr);
	}

	@Override
	public void caseCmplExpr(CmplExpr v) {
		System.out.println("CmplExpr");

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		soot.toCIL.structures.Constant constant = new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT,
				"-1");

		LoadInstruction loadOperand1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadOperand2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		LoadInstruction loadConstant = new LoadInstruction(constant, null, originStmt);
		Mul mulInstr = new Mul(originStmt);

		Clt cltInstr = new Clt(originStmt);

		stmtV.buildInstruction(loadOperand1);
		stmtV.buildInstruction(loadOperand2);
		stmtV.buildInstruction(cltInstr);
		stmtV.buildInstruction(loadConstant);
		stmtV.buildInstruction(mulInstr);

	}

	@Override
	public void caseDivExpr(DivExpr v) {
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Div divInstruction = new Div(originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(divInstruction);

	}

	@Override
	public void caseEqExpr(EqExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		String instruction = "";

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Beq beqInstruction = new Beq(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(beqInstruction);
	}

	@Override
	public void caseNeExpr(NeExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Bne bneInstruction = new Bne(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(bneInstruction);
	}

	@Override
	public void caseGeExpr(GeExpr v) {
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Bge bgeInstruction = new Bge(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(bgeInstruction);
	}

	@Override
	public void caseGtExpr(GtExpr v) {

		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Bgt bgtInstruction = new Bgt(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(bgtInstruction);
	}

	@Override
	public void caseLeExpr(LeExpr v) {
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstr1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstr2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Ble bleInstruction = new Ble(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstr1);
		stmtV.buildInstruction(loadInstr2);
		stmtV.buildInstruction(bleInstruction);
	}

	@Override
	public void caseLtExpr(LtExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Blt bltInstruction = new Blt(targetLabel, originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(bltInstruction);
	}

	@Override
	public void caseMulExpr(MulExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Mul mulInstruction = new Mul(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(mulInstruction);

	}

	@Override
	public void caseOrExpr(OrExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Or orInstruction = new Or(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(orInstruction);
	}

	@Override
	public void caseRemExpr(RemExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Rem remInstruction = new Rem(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(remInstruction);
	}

	@Override
	public void caseShlExpr(ShlExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Shl shlInstruction = new Shl(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(shlInstruction);
	}

	@Override
	public void caseShrExpr(ShrExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Shr shrInstruction = new Shr(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(shrInstruction);
	}

	@Override
	public void caseUshrExpr(UshrExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		ShrUn shrUnInstruction = new ShrUn();

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(shrUnInstruction);
	}

	@Override
	public void caseSubExpr(SubExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Sub subInstruction = new Sub(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(subInstruction);
	}

	@Override
	public void caseXorExpr(XorExpr v) {
		// TODO Auto-generated method stub
		// TODO Über die stmtV expression folgende Parameter übergeben:
		// v.getOp1(), v.getOp2()
		Value operand1 = v.getOp1();
		Value operand2 = v.getOp2();

		LoadInstruction loadInstruction1 = stmtV.BuildLoadInstruction(operand1, originStmt);
		LoadInstruction loadInstruction2 = stmtV.BuildLoadInstruction(operand2, originStmt);
		Xor xorInstruction = new Xor(originStmt);

		stmtV.buildInstruction(loadInstruction1);
		stmtV.buildInstruction(loadInstruction2);
		stmtV.buildInstruction(xorInstruction);
	}

	@Override
	public void caseInterfaceInvokeExpr(InterfaceInvokeExpr v) {

		String methodName = v.getMethod().getName();
		String returnType = null;

		returnType = Converter.getInstance().getTypeInString(v.getMethod().getReturnType());
		List<Value> allArguments = v.getArgs();
		ArrayList<String> allArgumentTypes = new ArrayList<>();

		Value leftValue = v.getBase();
		stmtV.buildInstruction(stmtV.BuildLoadInstruction(leftValue, originStmt));
		for (Value value : allArguments) {
			LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
			stmtV.buildInstruction(loadInstr);

			allArgumentTypes.add(Converter.getInstance().getTypeInString(value.getType()));
		}

		String className = v.getMethod().getDeclaringClass().getName();

		Callvirt callvirtInstruction = new Callvirt(returnType, className, methodName, originStmt, allArgumentTypes);
		stmtV.buildInstruction(callvirtInstruction);

	}

	@Override
	public void caseSpecialInvokeExpr(SpecialInvokeExpr v) {
		Value leftValue = v.getBase();

		String returnType = null;
		returnType = Converter.getInstance().getTypeInString(v.getMethod().getReturnType());
		List<Value> allArguments = v.getArgs();
		ArrayList<String> allArgumentTypes = new ArrayList<>();
		ArrayList<LoadInstruction> loadInstrForPassingValues = new ArrayList<>();

		for (Value value : allArguments) {
			LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
			// stmtV.buildInstruction(loadInstr);
			loadInstrForPassingValues.add(loadInstr);

			allArgumentTypes.add(Converter.getInstance().getTypeInString(value.getType()));
		}

		// if (!m.isConstructor()) {

		LocalVariables localVariable = m.getLocalVariableByValue((Local) leftValue);

		if (localVariable.assignedByThisRef()) {
			// String className = v.getMethod().getDeclaringClass().getName();
			// className = (className.equals(Object.class.getName())) ?
			// "[mscorlib]System.Object" : className;
			RefType type = v.getMethod().getDeclaringClass().getType();
			String className = Converter.getInstance().getTypeInString(type);
			Ldarg ldargInstruction = new Ldarg(0, originStmt);
			Callctor callInstruction = new Callctor(returnType, className, originStmt, allArgumentTypes);

			stmtV.buildInstruction(ldargInstruction);

			for (LoadInstruction loadInstr : loadInstrForPassingValues) {
				stmtV.buildInstruction(loadInstr);
			}

			stmtV.buildInstruction(callInstruction);

		} else {
			RefType type = v.getMethod().getDeclaringClass().getType();
			String className = Converter.getInstance().getTypeInString(type);

			Newobj newobjInstruction = new Newobj(returnType, className, allArgumentTypes, originStmt);

			for (LoadInstruction loadInstr : loadInstrForPassingValues) {
				stmtV.buildInstruction(loadInstr);
			}

			stmtV.buildInstruction(newobjInstruction);

			soot.toCIL.instructions.StoreInstruction storeInstr = null;
			try {
				storeInstr = stmtV.BuildStoreInstruction(leftValue, originStmt);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stmtV.buildInstruction(storeInstr);

		}

	}

	@Override
	public void caseStaticInvokeExpr(StaticInvokeExpr v) {
		// TODO Auto-generated method stub
		String methodName = v.getMethod().getName();

		String returnType = Converter.getInstance().getTypeInString(v.getMethod().getReturnType());

		List<Value> allArguments = v.getArgs();
		ArrayList<String> allArgumentTypes = new ArrayList<>();

		for (Value value : allArguments) {
			LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
			stmtV.buildInstruction(loadInstr);

			allArgumentTypes.add(Converter.getInstance().getTypeInString(value.getType()));
		}

		String className = v.getMethod().getDeclaringClass().getName();
		Call call = new Call(className, returnType, methodName, originStmt, allArgumentTypes);
		stmtV.buildInstruction(call);

	}

	@Override
	public void caseVirtualInvokeExpr(VirtualInvokeExpr v) {

		if (!SpecialCasesToConsider.isPrintStreamMethod(v)) {

			if (m.getMethodName().equals("TestClassesWithInterfaces")) {
				int debug = 0;
			}

			Value leftValue = v.getBase();

			String methodName = v.getMethod().getName();
			String askedType = Converter.getInstance().getTypeInString(v.getMethod().getReturnType());
			List<Value> allArguments = v.getArgs();
			ArrayList<String> allArgumentTypes = new ArrayList<>();

			stmtV.buildInstruction(stmtV.BuildLoadInstruction(leftValue, originStmt));
			for (Value value : allArguments) {
				LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
				stmtV.buildInstruction(loadInstr);

				allArgumentTypes.add(Converter.getInstance().getTypeInString(value.getType()));
			}

			String className = v.getMethod().getDeclaringClass().getName();
			Callvirt callvirtInstruction = new Callvirt(askedType, className, methodName, originStmt, allArgumentTypes);
			stmtV.buildInstruction(callvirtInstruction);

		} else {
			List<Value> allArguments = v.getArgs();
			ArrayList<String> allArgumentTypes = new ArrayList<>();
			
			for (Value value : allArguments) {
				LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
				stmtV.buildInstruction(loadInstr);

				allArgumentTypes.add(Converter.getInstance().getTypeInString(value.getType()));
			}
			ConsolePrintln consolePrintlnInstr = new ConsolePrintln(originStmt, allArgumentTypes);
			Nop nop = new Nop(originStmt);
			Pop popInstruction = new Pop(originStmt);
			ReadKey readkeyInstr = new ReadKey(originStmt);
			
			stmtV.buildInstruction(consolePrintlnInstr);
			stmtV.buildInstruction(nop);
			stmtV.buildInstruction(readkeyInstr);
			stmtV.buildInstruction(popInstruction);
		}

	}

	// Nicht so wichig
	@Override
	public void caseDynamicInvokeExpr(DynamicInvokeExpr v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caseCastExpr(CastExpr v) {
		Type castType = v.getCastType();
		Value operand = v.getOp();
		soot.toCIL.structures.Type cilType = Converter.getInstance().getTypeInEnum(castType);
		String classType = null;

		if (!Converter.getInstance().isClassType()) {
			classType = Converter.getInstance().getClassType(castType);
		}

		LoadInstruction loadValueInstr = stmtV.BuildLoadInstruction(operand, originStmt);
		CastInstruction castInstruction = new CastInstruction(originStmt, cilType, classType);

		stmtV.buildInstruction(loadValueInstr);
		stmtV.buildInstruction(castInstruction);
	}

	// TODO: statt instanceof = is in C#
	@Override
	public void caseInstanceOfExpr(InstanceOfExpr v) {
		Type type = v.getType();
		String cilType = Converter.getInstance().getTypeInString(type);
		Value op = v.getOp();

		LoadInstruction loadOperand = stmtV.BuildLoadInstruction(op, originStmt);
		Isinst isinstInstruction = new Isinst(originStmt, cilType);

		soot.toCIL.structures.Constant cilConstant = new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.NULL,
				"null");
		LoadInstruction loadNullConstant = new LoadInstruction(cilConstant, null, originStmt);
		Ceq ceqInstr = new Ceq(originStmt);

		stmtV.buildInstruction(loadOperand);
		stmtV.buildInstruction(isinstInstruction);
		stmtV.buildInstruction(loadNullConstant);
		stmtV.buildInstruction(ceqInstr);
	}

	// TODO: Testing
	@Override
	public void caseNewArrayExpr(NewArrayExpr v) {
		final int oneDimensional = 1;
		Value arraySize = v.getSize();
		String typeInString = Converter.getInstance().getTypeInString(v.getType());
		soot.toCIL.structures.Type type = Converter.getInstance().getTypeInEnum(v.getType());
		LoadInstruction loadInstr = stmtV.BuildLoadInstruction(arraySize, originStmt);
		Newarr newarrInstr = new Newarr(originStmt, type, typeInString, oneDimensional, oneDimensional);

		stmtV.buildInstruction(loadInstr);
		stmtV.buildInstruction(newarrInstr);
	}

	@Override
	public void caseNewMultiArrayExpr(NewMultiArrayExpr v) {

		/*
		 * TODO List<Value> arraySizes = v.getSizes(); HashMap<Integer,
		 * ArrayList<LoadInstruction>> executedLoadInstructions =
		 * HashMapInitialization(arraySizes);
		 * 
		 * Type sootType = v.getType(); String typeInString =
		 * Converter.getInstance().getTypeInString(v.getType());
		 * soot.toCIL.structures.Type type =
		 * Converter.getInstance().getTypeInEnum(v.getType()); AssignStmt
		 * assignStmt = (AssignStmt) originStmt; Value multiArray =
		 * assignStmt.getLeftOp();
		 * 
		 * soot.toCIL.instructions.StoreInstruction storeIntoArray = null; try {
		 * storeIntoArray = stmtV.BuildStoreInstruction(multiArray, originStmt);
		 * } catch (ClassNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 * 
		 * Value firstDimensionSize = arraySizes.get(0); LoadInstruction
		 * loadArray = stmtV.BuildLoadInstruction(multiArray, originStmt);
		 * LoadInstruction loadFirstDSize =
		 * stmtV.BuildLoadInstruction(firstDimensionSize, originStmt); Newarr
		 * firstArrInitialization = new Newarr(originStmt, type, typeInString,
		 * arraySizes.size() - 1, arraySizes.size());
		 * 
		 * stmtV.buildInstruction(loadFirstDSize);
		 * stmtV.buildInstruction(firstArrInitialization);
		 * stmtV.buildInstruction(storeIntoArray);
		 * 
		 * for (int key : executedLoadInstructions.keySet()) {
		 * ArrayList<LoadInstruction> instructions =
		 * executedLoadInstructions.get(key); soot.toCIL.structures.Constant
		 * constant = new
		 * soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT,
		 * String.valueOf(key)); LoadInstruction loadDimension = new
		 * LoadInstruction(constant, null, originStmt);
		 * 
		 * for (LoadInstruction loadSize : instructions) { Newarr newarr = new
		 * Newarr(originStmt, type, typeInString, arraySizes.size() - (key + 2),
		 * arraySizes.size()); StelemRef stelemInstr = new
		 * StelemRef(originStmt);
		 * 
		 * stmtV.buildInstruction(loadArray);
		 * stmtV.buildInstruction(loadDimension);
		 * stmtV.buildInstruction(loadSize); stmtV.buildInstruction(newarr);
		 * stmtV.buildInstruction(stelemInstr); } }
		 */
	}

	@Override
	public void caseNewExpr(NewExpr v) {
		return;
	}

	// TODO: Testen
	@Override
	public void caseLengthExpr(LengthExpr v) {
		// TODO Auto-generated method stub
		System.out.println("Length Expr");

		Value value = v.getOp();

		LoadInstruction loadInstr = stmtV.BuildLoadInstruction(value, originStmt);
		Ldlen ldlenInstr = new Ldlen(originStmt);

		stmtV.buildInstruction(loadInstr);
		stmtV.buildInstruction(ldlenInstr);
	}

	/*
	 * TODO: I Can't Invoke This Case!!
	 */
	@Override
	public void caseNegExpr(NegExpr v) {
		Value source = v.getOp();

		LoadInstruction loadInstr = stmtV.BuildLoadInstruction(source, originStmt);
		Neg negInstr = new Neg(originStmt);

		stmtV.buildInstruction(loadInstr);
		stmtV.buildInstruction(negInstr);

	}

	@Override
	public void defaultCase(Object obj) {
		// TODO Auto-generated method stub
		// rsub-int and rsub-int/lit8 aren't generated, since we cannot detect
		// there usage in jimple
		throw new Error("unknown Object (" + obj.getClass() + ") as Expression: " + obj);
	}

}
