package soot.toCIL.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import polyglot.ext.param.types.Param;
import soot.Local;
import soot.PatchingChain;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.toCIL.Converter;
import soot.toCIL.LabelAssigner;
import soot.toCIL.StmtVisitor;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.LocalsInit;
import soot.util.Chain;

/**
 * This class holds the structure of an CIL Method
 * 
 * @author ahmad
 *
 */
public class Method implements Body {
	private Class refClass;
	private int maxStack;
	private String methodName;
	private String modifier[];
	private String returnType;
	private ArrayList<OtherVariables> allVariables;
	private ArrayList<Parameter> allParameters;
	private int labelID;
	private ArrayList<Instruction> allInstructions;
	private SootMethod sootMethod;
	private String startBody;
	private HashMap<Integer, Integer> allTypeIndexes; // Integer = number of
														// Type, Integer = index

	public Method(SootMethod sootMethod, Class refClass) {
		allVariables = new ArrayList<>();
		allParameters = new ArrayList<>();
		allTypeIndexes = new HashMap<>();
		allInstructions = new ArrayList<>();
		labelID = 0;
		this.refClass = refClass;
		this.methodName = sootMethod.getName();
		this.sootMethod = sootMethod;
		this.returnType = Converter.getInstance().getTypeInString(sootMethod.getReturnType());
		startBody = "";
	}

	public void setRightCILMethod() {
		String params = "";
		boolean firstParam = true;

		for (Variable v : allParameters) {
			String comma = (firstParam == true) ? "" : ", ";
			params += comma;
			params += v.variableType + " ";
			params += v.variableName;

			firstParam = false;
		}
		
		
		if(sootMethod.isConstructor()) {
			if(sootMethod.isPublic()) {
				 startBody = ".method public hidebysig specialname rtspecialname instance " + returnType + " "
							+ ".ctor" + "(" + params + ") cil managed" + " {";
			} else if (sootMethod.isPrivate()) {
				 startBody = ".method private hidebysig specialname rtspecialname instance " + returnType + " "
							+ ".ctor" + "(" + params + ") cil managed" + " {";
			}
		} else {
			String staticornot =(sootMethod.isStatic())? "static": "instance";
			
			if (sootMethod.isPublic()) {
				startBody = ".method public hidebysig " + staticornot + " " + returnType + " "
						+ methodName + "(" + params + ") cil managed {";
			} else if(sootMethod.isPrivate()) {
				startBody = ".method private hidebysig " + staticornot + " " + returnType + " "
						+ methodName + "(" + params + ") cil managed {";
			} else if (sootMethod.isProtected()) {
				startBody = ".method family hidebysig " + staticornot + " " + returnType + " "
						+ methodName + "(" + params + ") cil managed {";
			}
		}
	}

	public boolean isConstructor() {
		return sootMethod.isConstructor();
	}
	
	public boolean isStatic() {
		return sootMethod.isStatic();
	}

	public Class getRefClass() {
		return refClass;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public void setVariables(ArrayList<OtherVariables> allVariables) {
		this.allVariables = allVariables;
	}

	public void setParameters(ArrayList<Parameter> allParameters) {
		this.allParameters = allParameters;
	}

	public void setInstructions(ArrayList<Instruction> allInstructions) {
		this.allInstructions = allInstructions;
	}

	public void setTypeIndexes(HashMap<Integer, Integer> allTypeIndexes) {
		this.allTypeIndexes = allTypeIndexes;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setModifier(String[] modifier) {
		this.modifier = modifier;
	}

	public void setReturnType(String returnType) throws ClassNotFoundException {
		this.returnType = returnType;
	}

	public String getMethodName() {
		return methodName;
	}

	public String[] getModifier() {
		return modifier;
	}

	public String getReturnType() {
		return returnType;
	}

	public ArrayList<OtherVariables> getAllVariables() {
		return allVariables;
	}

	public ArrayList<Parameter> getAllParameters() {
		return allParameters;
	}

	public ArrayList<Instruction> getInstructions() {
		return allInstructions;
	}

	public HashMap<Integer, Integer> getTypeIndexes() {
		return allTypeIndexes;
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
			if (variable.isFinal() == v.isFinal) {
				if (variable.getVariableName().equals(v.getVariableName())) {
					if (variable.getVariableType().equals(v.getVariableType())) {
						return variable;
					}
				}
			}

			index++;
		}

		return null;
	}

	public Integer getIndexBySootType(Type t) {

		return allTypeIndexes.get(t.getNumber());
	}

}
