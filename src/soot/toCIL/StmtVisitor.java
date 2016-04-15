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

import org.jf.dexlib2.Opcode;

import jdk.management.resource.internal.inst.InitInstrumentation;
import soot.Local;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
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
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NewExpr;
import soot.jimple.NopStmt;
import soot.jimple.ParameterRef;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.toCIL.instructions.Br;
import soot.toCIL.instructions.Brfalse;
import soot.toCIL.instructions.Brtrue;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.Ldarg;
import soot.toCIL.instructions.Ldfld;
import soot.toCIL.instructions.LoadInstruction;
import soot.toCIL.instructions.LocalsInit;
import soot.toCIL.instructions.Newobj;
import soot.toCIL.instructions.Pop;
import soot.toCIL.instructions.Ret;
import soot.toCIL.instructions.Stfld;
import soot.toCIL.instructions.StoreInstruction;
import soot.toCIL.structures.Class;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Member;
import soot.toCIL.structures.Method;
import soot.toCIL.structures.OtherVariables;
import soot.toCIL.structures.Parameter;
import soot.toCIL.structures.Variable;
import soot.toDex.LocalRegisterAssignmentInformation;
import soot.toDex.Register;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn11x;

public class StmtVisitor implements StmtSwitch {
	private ExprVisitor exprV;
	private ConstantVisitor constantV;
	private Method m;
	private ArrayList<Instruction> allInstructions;
	private soot.toCIL.structures.Constant constant;
	private Class refClass;
	private boolean isThisRefAvailable = false;

	public StmtVisitor(Method m, Class refClass) {
		constantV = new ConstantVisitor(this);
		exprV = new ExprVisitor(this, constantV, m);
		this.m = m;
		allInstructions = m.getInstructions();
		this.refClass = refClass;
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
			if (!m.isConstructor() && !(invokeExpr instanceof SpecialInvokeExpr) && !(returnType instanceof soot.VoidType)) {
				Pop popInstruction = new Pop(stmt);
				buildInstruction(popInstruction);
			} 
		}
	}

	public StoreInstruction BuildStoreInstruction(Value lhs, Stmt stmt) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		String instruction = "";
		StoreInstruction instr = null;

		if (lhs instanceof Local) {
			String lhsName = ((Local) lhs).getName();
			String type = Converter.getInstance().getTypeInString(((Local) lhs).getType());

			OtherVariables otherVariable = new OtherVariables(lhsName, type, false);
			OtherVariables localVar = (OtherVariables) m.searchforVariableAndGetIt(otherVariable);

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

		stfld = new Stfld(stmt, returnType, className, attributeName);

		return stfld;
	}

	private Ldfld buildLdfldInstruction(Value v, Stmt stmt) {
		Ldfld ldfld = null;
		SootField field = ((InstanceFieldRef) v).getField();
		String returnType = Converter.getInstance().getTypeInString(v.getType());
		String className = field.getDeclaringClass().getName();
		String attributeName = field.getName();

		ldfld = new Ldfld(stmt, returnType, className, attributeName);

		return ldfld;
	}

	public void buildInstruction(Instruction instruction) {

		if (instruction != null) {
			allInstructions.add(instruction);
		}
	}

	@Override
	public void caseAssignStmt(AssignStmt stmt) {

		constantV.setOriginStmt(stmt);
		exprV.setOriginStmt(stmt);
		
	

		Value lhs = stmt.getLeftOp();
		Value rhs = stmt.getRightOp();

		if (!(rhs instanceof NewExpr)) {
			if (lhs instanceof Local) {

				if (rhs instanceof Local || rhs instanceof Constant) {
					buildInstruction(BuildLoadInstruction(rhs, stmt));
				} else if (rhs instanceof InstanceFieldRef) {
					Instruction instr = null;
					
					if(m.isStatic()) {
						Value base = ((InstanceFieldRef)rhs).getBase();
						instr = BuildLoadInstruction(base, stmt);
					} else {
						instr = new Ldarg(0, stmt);
					}
					Ldfld ldfldInstr = buildLdfldInstruction(rhs, stmt);
					buildInstruction(instr);
					buildInstruction(ldfldInstr);
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

				} else {
					rhs.apply(exprV);
				}

				try {
					buildInstruction(BuildStoreInstruction(lhs, stmt));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (lhs instanceof InstanceFieldRef) {

				if (rhs instanceof Local || rhs instanceof Constant) {
					Instruction instruction = null;
					
					if(m.isStatic()) {
						Value base = ((InstanceFieldRef) lhs).getBase();
						instruction = BuildLoadInstruction(base, stmt);
					} else {
						instruction  = new Ldarg(0, stmt);
					}
					
	
					buildInstruction(instruction);
					buildInstruction(BuildLoadInstruction(rhs, stmt));
				} else if (rhs instanceof InstanceFieldRef) {
					
					Ldarg ldargInstr = new Ldarg(0, stmt);
					Ldarg ldargInstr2 = new Ldarg(0, stmt);
					Ldfld ldfldInstr = buildLdfldInstruction(rhs, stmt);

					buildInstruction(ldargInstr);
					buildInstruction(ldargInstr2);
					buildInstruction(ldfldInstr);
				} else {
					rhs.apply(exprV);
				}

				Stfld stfldInstr = buildStfldInstruction(lhs, stmt);
				buildInstruction(stfldInstr);
			}
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
			isThisRefAvailable = true;

			OtherVariables otherVariable = new OtherVariables(lhs.getName(),
					Converter.getInstance().getTypeInString(lhs.getType()), false);
			Variable var = m.searchforVariableAndGetIt(otherVariable);

			if (var != null) {
				ArrayList<OtherVariables> allVariables = m.getAllVariables();
				allVariables.remove(var);
				m.setVariables(allVariables);
				LocalsInit localsinitInstr = (m.getInstructions().get(0) instanceof LocalsInit)
						? (LocalsInit) m.getInstructions().get(0) : new LocalsInit(null);

				if (localsinitInstr.getLocalVariables().size() == 0) {
					allInstructions.remove(localsinitInstr);
				}
			}

		} else if (rhs instanceof ParameterRef) {
			ldargInstruction = BuildLdargInstruction(rhs, stmt);

			try {
				storeInstr = BuildStoreInstruction(lhs, stmt);
				buildInstruction(ldargInstruction);
				buildInstruction(storeInstr);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Ldarg BuildLdargInstruction(Value v, Stmt stmt) {

		Type type = ((ParameterRef) v).getType();
		Integer index = m.getIndexBySootType(type);
		index = (isThisRefAvailable) ? index + 1 : index;

		if (index != null) {
			return new Ldarg(index, stmt);
		}

		return null;
	}

	public LoadInstruction BuildLoadInstruction(Value rhs, Stmt stmt) {
		String instruction = "";
		LoadInstruction instr = null;

		if (rhs instanceof Local) {
			String rhsName = ((Local) rhs).getName();
			String type = Converter.getInstance().getTypeInString(((Local) rhs).getType());

			OtherVariables otherVariable = new OtherVariables(rhsName, type, false);
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

	}

	// TODO: Thread Synchronize
	@Override
	public void caseExitMonitorStmt(ExitMonitorStmt stmt) {
		// TODO Auto-generated method stub

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

	@Override
	public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {

	}

	// TODO:
	@Override
	public void caseNopStmt(NopStmt stmt) {
	}

	@Override
	public void caseRetStmt(RetStmt stmt) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	// TODO:
	@Override
	public void caseThrowStmt(ThrowStmt stmt) {
		// TODO Auto-generated method stub
		Value exception = stmt.getOp();
		constantV.setOriginStmt(stmt);

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
