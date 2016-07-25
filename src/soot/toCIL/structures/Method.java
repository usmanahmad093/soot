package soot.toCIL.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import polyglot.ext.param.types.Param;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.toCIL.Converter;
import soot.toCIL.LabelAssigner;
import soot.toCIL.StmtVisitor;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.LocalsInit;
import soot.util.Chain;

/**
 * This class holds the structure of an CIL Method with the help of an Interface named Body
 * 
 * @author ahmad
 *
 */
public class Method implements Body {
	private int maxStack;
	private ArrayList<LocalVariables> allVariables;
	private ArrayList<Parameter> allParameters;
	private ArrayList<Instruction> allInstructions;
	private SootMethod sootMethod;
	private String startBody;
	private HashMap<Stmt, Stmt> trapsBeginStmt;
	private HashMap<Stmt, Stmt> trapsEndStmt;

	public Method(SootMethod sootMethod, Class refClass) {
		allVariables = new ArrayList<>();
		allParameters = new ArrayList<>();
		allInstructions = new ArrayList<>();
		trapsBeginStmt = new HashMap<>();
		trapsEndStmt = new HashMap<>();
		this.sootMethod = sootMethod;
		startBody = "";
	}
	
	
	public String getClassName() {
		return sootMethod.getDeclaringClass().getName();
	}
	
	public void setTraps(Chain<Trap> allTraps) {
		for(Trap t: allTraps) {
			this.trapsBeginStmt.put((Stmt) t.getBeginUnit(), (Stmt) t.getBeginUnit());
			this.trapsEndStmt.put((Stmt) t.getEndUnit(), (Stmt) t.getEndUnit());
		}
	}
	
	
	
	public String getMethodName() {
		return sootMethod.getName();
	}

	public boolean isConstructor() {
		return sootMethod.isConstructor();
	}

	public boolean isMain() {
		return (sootMethod.isMain()) ? true : false;
	}

	public boolean isStatic() {
		return sootMethod.isStatic();
	}

	public boolean isVoid() {
		return (sootMethod.getReturnType() instanceof soot.VoidType) ? true : false;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public void setVariables(ArrayList<LocalVariables> allVariables) {
		this.allVariables = allVariables;
	}

	public void setParameters(ArrayList<Parameter> allParameters) {
		this.allParameters = allParameters;
	}

	public void setInstructions(ArrayList<Instruction> allInstructions) {
		this.allInstructions = allInstructions;
	}
	
	public void addInstruction(Instruction instruction) {
		this.allInstructions.add(instruction);
	}
	
	public void setInstruction(int index, Instruction instruction) {
		this.allInstructions.set(index, instruction);
	}


	public ArrayList<LocalVariables> getAllVariables() {
		return allVariables;
	}
	
	public void addLocalVariable(LocalVariables l) {
		allVariables.add(l);
	}

	public ArrayList<Parameter> getAllParameters() {
		return allParameters;
	}

	public ArrayList<Instruction> getInstructions() {
		return allInstructions;
	}

	public void setStartBody(String startBody) {
		this.startBody = startBody;
	}

	@Override
	public String getStartBody() {
		return startBody;
	}

	@Override
	public String getEndBody() {
		return "}";
	}

	public Variable searchforVariableAndGetIt(Variable v) {
		int index = 0;

		for (Variable variable : allVariables) {
				if (variable.getVariableName().equals(v.getVariableName())) {
					if (variable.getVariableType().equals(v.getVariableType())) {
						return variable;
					}
				}
			

			index++;
		}

		return null;
	}

	public LocalVariables getLocalVariableByValue(Local localVariable) {

		String variableName = localVariable.getName();
		String variableType = Converter.getInstance().getTypeInString(localVariable.getType());

		for (LocalVariables variable : allVariables) {
			if (variable.getVariableName().equals(variableName)) {
				if (variable.getVariableType().equals(variableType)) {
					return variable;
				}
			}
		}

		return null;
	}
	
	
	public Stmt getTrapBeginUnitByStmt(Stmt stmt) {	
		return trapsBeginStmt.get(stmt);
	}
	
	public Stmt getTrapEndUnitByStmt(Stmt stmt) {
		return trapsEndStmt.get(stmt);
	}

	
	public void InsertEditedLocalVariable(LocalVariables localVariable) {
		allVariables.set(allVariables.indexOf(localVariable), localVariable);
	}

	public Integer getIndexByParameterRef(ParameterRef paramRef) {
		int indexToSearch = paramRef.getIndex();
		
		
		return allParameters.get(indexToSearch).getParamIndex();
	}

}
