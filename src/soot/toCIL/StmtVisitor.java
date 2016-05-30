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
import java.util.LinkedHashMap;
import java.util.List;

import org.jf.dexlib2.Opcode;

import jdk.management.resource.internal.inst.InitInstrumentation;
import soot.Local;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JArrayRef;
import soot.toCIL.instructions.Br;
import soot.toCIL.instructions.Brfalse;
import soot.toCIL.instructions.Brtrue;
import soot.toCIL.instructions.Ceq;
import soot.toCIL.instructions.Dup;
import soot.toCIL.instructions.EndFinally;
import soot.toCIL.instructions.EnterMonitor;
import soot.toCIL.instructions.ExitMonitor;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.Ldarg;
import soot.toCIL.instructions.Ldfld;
import soot.toCIL.instructions.Ldsfld;
import soot.toCIL.instructions.Leave;
import soot.toCIL.instructions.LoadInstruction;
import soot.toCIL.instructions.LocalsInit;
import soot.toCIL.instructions.Newobj;
import soot.toCIL.instructions.Nop;
import soot.toCIL.instructions.Pop;
import soot.toCIL.instructions.Ret;
import soot.toCIL.instructions.Stelem;
import soot.toCIL.instructions.Stfld;
import soot.toCIL.instructions.StoreInstruction;
import soot.toCIL.instructions.Stsfld;
import soot.toCIL.instructions.Throw;
import soot.toCIL.instructions.Volatile;
import soot.toCIL.instructions.jumps.Beq;
import soot.toCIL.structures.CILModifiers;
import soot.toCIL.structures.Class;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.LocalVariables;
import soot.toCIL.structures.Member;
import soot.toCIL.structures.Method;
import soot.toCIL.structures.Parameter;
import soot.toCIL.structures.Variable;

public class StmtVisitor implements StmtSwitch {
	private ExprVisitor exprV;
	private ConstantVisitor constantV;
	private Method m;
	private ArrayList<Instruction> allInstructions;
	private soot.toCIL.structures.Constant constant;
	private static final String cilSynchronizedLock = "'<>s_LockTaken0'";

	public StmtVisitor(Method m) {
		constantV = new ConstantVisitor(this);
		exprV = new ExprVisitor(this, constantV, m);
		this.m = m;
		allInstructions = m.getInstructions();
	}

	public ArrayList<Instruction> getInstructions() {
		return allInstructions;
	}

	public void setConstant(soot.toCIL.structures.Constant constant) {
		this.constant = constant;
	}

	@Override
	public void caseBreakpointStmt(BreakpointStmt stmt) {
		return;
	}

	@Override
	public void caseInvokeStmt(InvokeStmt stmt) {
		InvokeExpr invokeExpr = stmt.getInvokeExpr();

		if (!JimpleCodeDetector.getInstance().checkStaticInvokeExpr(invokeExpr)
				&& !JimpleCodeDetector.getInstance().checkVirtualInvokeExpr(invokeExpr)) {
			exprV.setOriginStmt(stmt);
			stmt.getInvokeExpr().apply(exprV);

			Type returnType = invokeExpr.getMethod().getReturnType();
			if (!m.isConstructor() && !(invokeExpr instanceof SpecialInvokeExpr)
					&& !(returnType instanceof soot.VoidType)) {
				Pop popInstruction = new Pop(stmt);
				buildInstruction(popInstruction);
			}
		}
	}

	public StoreInstruction BuildStoreInstruction(Value lhs, Stmt stmt) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		StoreInstruction instr = null;

		if (lhs instanceof Local) {
			String lhsName = ((Local) lhs).getName();
			String type = Converter.getInstance().getTypeInString(((Local) lhs).getType());

			LocalVariables otherVariable = new LocalVariables(lhsName, type);
			LocalVariables localVar = (LocalVariables) m.searchforVariableAndGetIt(otherVariable);

			if (localVar != null) {
				instr = new StoreInstruction(localVar, stmt);
			}

		}

		return instr;
	}

	private Stfld buildStfldInstruction(Value v, Stmt stmt) {
		
		Stfld stfld = null;
		String returnType = Converter.getInstance().getTypeInString(v.getType());
		SootField field = ((InstanceFieldRef) v).getField();
		String className = field.getDeclaringClass().getName();
		String attributeName = field.getName();

		if (CILModifierBuilder.isVolatile(field.getModifiers())) {
			Volatile volatileInstr = new Volatile(stmt);
			buildInstruction(volatileInstr);

			returnType = (returnType + " " + CILModifiers.VOLATILE);
		}

		stfld = new Stfld(stmt, returnType, className, attributeName);

		return stfld;
	}

	private Stsfld buildStsfldInstruction(Value v, Stmt stmt) {
		Stsfld stsfld = null;
		String returnType = Converter.getInstance().getTypeInString(v.getType());
		SootField field = ((StaticFieldRef) v).getField();
		String className = field.getDeclaringClass().getName();
		String attributeName = field.getName();

		if (CILModifierBuilder.isVolatile(field.getModifiers())) {
			Volatile volatileInstr = new Volatile(stmt);
			buildInstruction(volatileInstr);

			returnType = (returnType + " " + CILModifiers.VOLATILE);
		}

		stsfld = new Stsfld(stmt, returnType, className, attributeName);

		return stsfld;
	}

	private Ldfld buildLdfldInstruction(Value v, Stmt stmt) {
		Ldfld ldfld = null;
		SootField field = ((InstanceFieldRef) v).getField();
		String returnType = Converter.getInstance().getTypeInString(v.getType());
		String className = field.getDeclaringClass().getName();
		String attributeName = field.getName();

		if (CILModifierBuilder.isVolatile(field.getModifiers())) {
			Volatile volatileInstr = new Volatile(stmt);
			buildInstruction(volatileInstr);

			returnType = (returnType + " " + CILModifiers.VOLATILE);
		}

		ldfld = new Ldfld(stmt, returnType, className, attributeName);

		return ldfld;
	}

	private Ldsfld buildLdsfldInstruction(Value v, Stmt stmt) {
		Ldsfld ldsfld = null;
		SootField field = ((StaticFieldRef) v).getField();
		String returnType = Converter.getInstance().getTypeInString(v.getType());
		String className = field.getDeclaringClass().getName();
		String attributeName = field.getName();

		if (CILModifierBuilder.isVolatile(field.getModifiers())) {
			Volatile volatileInstr = new Volatile(stmt);
			buildInstruction(volatileInstr);

			returnType = (returnType + " " + CILModifiers.VOLATILE);
		}

		ldsfld = new Ldsfld(stmt, returnType, className, attributeName);

		return ldsfld;
	}

	public void buildInstruction(Instruction instruction) {

		if (instruction != null) {
			allInstructions.add(instruction);
		}
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		// constantV.setOriginStmt(stmt);
		// exprV.setOriginStmt(stmt);

		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		
		if (m.getMethodName().equals("testIfStmt")) {
			int debug = 0;
		}

		if (!(rhs instanceof NewExpr)) {
			if (lhs instanceof Local || lhs instanceof Constant) {
				buildRightSide(rhs, stmt);

				try {
					buildInstruction(BuildStoreInstruction(lhs, stmt));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (lhs instanceof ArrayRef) {
				ArrayRef arrayRef = (ArrayRef) lhs;
				Value indexValue = arrayRef.getIndex();
				Value localArray = arrayRef.getBase();

				LoadInstruction loadInstr = BuildLoadInstruction(localArray, stmt);
				LoadInstruction loadInstr2 = BuildLoadInstruction(indexValue, stmt);

				buildInstruction(loadInstr);
				buildInstruction(loadInstr2);
				buildRightSide(rhs, stmt);

				Stelem stelemInstr = new Stelem(stmt);
				buildInstruction(stelemInstr);
			} else if (lhs instanceof InstanceFieldRef) {
				Value baseValue = ((InstanceFieldRef) lhs).getBase();
				buildInstruction(BuildLoadInstruction(baseValue, stmt));
				buildRightSide(rhs, stmt);
				buildInstruction(buildStfldInstruction(lhs, stmt));
			} else if (lhs instanceof StaticFieldRef) {
				buildRightSide(rhs, stmt);
				buildInstruction(buildStsfldInstruction(lhs, stmt));
			}
		} else {
			Value leftValue = stmt.getLeftOp();

			LocalVariables localVariable = m.getLocalVariableByValue((Local) leftValue);
			localVariable.notAssignedByThisRef();
			m.InsertEditedLocalVariable(localVariable);
		}

	}

	public void buildRightSide(Value rhs, AssignStmt stmt) {
		exprV.setOriginStmt(stmt);
		
		if (rhs instanceof Local || rhs instanceof Constant) {
			buildInstruction(BuildLoadInstruction(rhs, stmt));
		} else if (rhs instanceof InvokeExpr) {
			InvokeExpr invokeExpr = (InvokeExpr) rhs;

			if (JimpleCodeDetector.getInstance().checkStaticInvokeExpr(invokeExpr)) {
				Value value = invokeExpr.getArg(0);
				buildInstruction(BuildLoadInstruction(value, stmt));
			} else if (JimpleCodeDetector.getInstance().checkVirtualInvokeExpr(invokeExpr)) {
				VirtualInvokeExpr virtualInvokeExpr = (VirtualInvokeExpr) invokeExpr;
				Value value = virtualInvokeExpr.getBase();
				buildInstruction(BuildLoadInstruction(value, stmt));
			} else {
				rhs.apply(exprV);
			}

		} else if (rhs instanceof InstanceFieldRef) {
			Value baseValue = ((InstanceFieldRef) rhs).getBase();
			buildInstruction(BuildLoadInstruction(baseValue, stmt));
			buildInstruction(buildLdfldInstruction(rhs, stmt));
		} else if (rhs instanceof StaticFieldRef) {
			buildInstruction(buildLdsfldInstruction(rhs, stmt));
		} else if (rhs instanceof ArrayRef) {
			ArrayRef arrayRef = (ArrayRef) rhs;
			Value indexValue = arrayRef.getIndex();
			Value localArray = arrayRef.getBase();

			LoadInstruction loadInstr = BuildLoadInstruction(localArray, stmt);
			LoadInstruction loadInstr2 = BuildLoadInstruction(indexValue, stmt);

			buildInstruction(loadInstr);
			buildInstruction(loadInstr2);

			Stelem stelemInstr = new Stelem(stmt);
			buildInstruction(stelemInstr);
		} else {
			rhs.apply(exprV);
		}
	}

	@Override
	public void caseIdentityStmt(IdentityStmt stmt) {

		Local lhs = (Local) stmt.getLeftOp();
		Value rhs = stmt.getRightOp();
		Ldarg ldargInstruction = null;
		StoreInstruction storeInstr = null;

		if (rhs instanceof ThisRef) {
			ldargInstruction = new Ldarg(0, stmt);
		} else if (rhs instanceof ParameterRef) {
			ldargInstruction = BuildLdargInstruction(rhs, stmt);
		}

		try {
			storeInstr = BuildStoreInstruction(lhs, stmt);
			buildInstruction(ldargInstruction);
			buildInstruction(storeInstr);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Ldarg BuildLdargInstruction(Value v, Stmt stmt) {

		ParameterRef ref = (ParameterRef) v;

		Integer index = m.getIndexByParameterRef(ref);

		index = (!m.isStatic()) ? index + 1 : index;

		if (index != null) {
			return new Ldarg(index, stmt);
		}

		return null;
	}

	public LoadInstruction BuildLoadInstruction(Value rhs, Stmt stmt) {
		LoadInstruction instr = null;

		if (rhs instanceof Local) {
			String rhsName = ((Local) rhs).getName();
			String type = Converter.getInstance().getTypeInString(((Local) rhs).getType());

			LocalVariables otherVariable = new LocalVariables(rhsName, type);
			Variable var = m.searchforVariableAndGetIt(otherVariable);

			if (var != null) {
				instr = new LoadInstruction(null, var, stmt);

			}
		} else if (rhs instanceof Constant) {
			rhs.apply(constantV);
			instr = new LoadInstruction(constant, null, stmt);
		}

		return instr;
	}

	// TODO: Thread Synchronize
	@Override
	public void caseEnterMonitorStmt(EnterMonitorStmt stmt) {
		// TODO Auto-generated method stub
		Value value = stmt.getOp();
		
		LocalVariables localVar = new LocalVariables(cilSynchronizedLock, "bool");
		
		m.addLocalVariable(localVar);
		LocalsInit localsInit = (LocalsInit) m.getInstructions().get(0);
		localsInit.addLocalVariable(localVar);
		m.setInstruction(0, localsInit);

		
		LoadInstruction loadBoolean = new LoadInstruction(new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT, "0"), null, stmt);
		StoreInstruction storeBoolean = new StoreInstruction(localVar, stmt);
		LoadInstruction loadObject = BuildLoadInstruction(value, stmt);
		EnterMonitor enterMonitorInstr = new EnterMonitor(stmt);
		
		buildInstruction(loadBoolean);
		buildInstruction(storeBoolean);
		buildInstruction(loadObject);
		buildInstruction(enterMonitorInstr);

	}

	// TODO: Thread Synchronize
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		// TODO Auto-generated method stub
		Value value = stmt.getOp();
		
		LocalVariables localVar = new LocalVariables(cilSynchronizedLock, "bool");
		Variable variable = m.searchforVariableAndGetIt(localVar);
		
		Leave leaveInstr = new Leave(null, stmt); //TODO Targetlabel später übergeben
		LoadInstruction loadInstr = new LoadInstruction(null, variable, stmt);
		LoadInstruction loadConstant = new LoadInstruction(new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT, "0"), null, stmt);
		Ceq ceqInstr = new Ceq(stmt);
		Brtrue brtrueInstr = new Brtrue(null, stmt); //TODO: Später
		LoadInstruction loadObject = BuildLoadInstruction(value, stmt);
		ExitMonitor exitMonitorStmt = new ExitMonitor(stmt);
		EndFinally endFinally = new EndFinally(stmt);
		
		buildInstruction(leaveInstr);
		buildInstruction(loadInstr);
		buildInstruction(loadConstant);
		buildInstruction(ceqInstr);
		buildInstruction(brtrueInstr);
		buildInstruction(loadObject);
		buildInstruction(exitMonitorStmt);
		buildInstruction(endFinally);
		
	}

	@Override
	public void caseGotoStmt(GotoStmt stmt) {
		// TODO Auto-generated method stub
		Stmt target = (Stmt) stmt.getTarget();
		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(target);
		Br brInstruction = new Br(targetLabel.getLabel(), stmt);
		buildInstruction(brInstruction);
	}

	@Override
	public void caseIfStmt(IfStmt stmt) {
		// TODO Auto-generated method stub
		Stmt target = stmt.getTarget();
		exprV.setOriginStmt(stmt);
		exprV.setTarget(target);
		stmt.getCondition().apply(exprV);
	}

	// Support: only for Int- Constants
	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
		List<IntConstant> intConstants = stmt.getLookupValues();
		Value v = stmt.getKey();
		List<Unit> allUnits = stmt.getTargets();
		Stmt defaultTarget = (Stmt) stmt.getDefaultTarget();

		for (IntConstant constant : intConstants) {
			soot.toCIL.structures.Constant cilConstant = new soot.toCIL.structures.Constant(
					soot.toCIL.structures.Type.INT, String.valueOf(constant.value));
			LoadInstruction loadKey = BuildLoadInstruction(v, stmt);
			LoadInstruction loadInstr = new LoadInstruction(cilConstant, null, stmt);
			Unit targetUnit = allUnits.get(intConstants.indexOf(constant));
			Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel((Stmt) targetUnit);
			Beq beqInstr = new Beq(targetLabel, stmt);

			buildInstruction(loadInstr);
			buildInstruction(loadKey);
			buildInstruction(beqInstr);
		}

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel(defaultTarget);
		Br brInstr = new Br(targetLabel.getLabel(), stmt);

		buildInstruction(brInstr);
	}

	@Override
	public void caseNopStmt(NopStmt stmt) {
		Nop nopInstr = new Nop(stmt);

		buildInstruction(nopInstr);
	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		return;
	}

	@Override
	public void caseReturnStmt(ReturnStmt stmt) {
		Value returnValue = stmt.getOp();
		LoadInstruction loadInstr = BuildLoadInstruction(returnValue, stmt);
		Ret retInstruction = new Ret(stmt);

		buildInstruction(loadInstr);
		buildInstruction(retInstruction);
	}

	@Override
	public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
		// TODO Auto-generated method stub
		Ret retInstruction = new Ret(stmt);
		buildInstruction(retInstruction);
	}

	@Override
	public void caseTableSwitchStmt(TableSwitchStmt stmt) {

		List<Unit> allTargets = stmt.getTargets();
		Unit defaultTarget = stmt.getDefaultTarget();
		Value key = stmt.getKey();

		int caseIndex = 0;

		for (Unit target : allTargets) {
			soot.toCIL.structures.Constant constant = new soot.toCIL.structures.Constant(soot.toCIL.structures.Type.INT,
					String.valueOf(caseIndex));
			LoadInstruction loadKey = BuildLoadInstruction(key, stmt);
			LoadInstruction loadInstr = new LoadInstruction(constant, null, stmt);
			Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel((Stmt) target);
			Beq beqInstruction = new Beq(targetLabel, stmt);

			buildInstruction(loadKey);
			buildInstruction(loadInstr);
			buildInstruction(beqInstruction);
		}

		Label targetLabel = LabelAssigner.getInstance().CreateTargetLabel((Stmt) defaultTarget);
		Br brInstruction = new Br(targetLabel.getLabel(), stmt);
		buildInstruction(brInstruction);
	}

	// TODO:
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		// TODO Auto-generated method stub
		Value exception = stmt.getOp();

		LoadInstruction loadException = BuildLoadInstruction(exception, stmt);
		Throw throwInstr = new Throw(stmt);

		buildInstruction(loadException);
		buildInstruction(throwInstr);
	}

	@Override
	public void defaultCase(Object obj) {
		// TODO Auto-generated method stub
		// not-int and not-long aren't implemented because soot converts "~x" to
		// "x ^ (-1)"
		// fill-array-data isn't implemented since soot converts "new int[]{x,
		// y}" to individual "array put" expressions for x and y
		throw new Error("unknown Object (" + obj.getClass() + ") as Stmt: " + obj);

	}

}
