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
import soot.Value;
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
	private int maxStack;
	private ArrayList<LocalVariables> allVariables;
	private ArrayList<Parameter> allParameters;
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
		this.sootMethod = sootMethod;
		startBody = "";
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

	public void setTypeIndexes(HashMap<Integer, Integer> allTypeIndexes) {
		this.allTypeIndexes = allTypeIndexes;
	}

	public ArrayList<LocalVariables> getAllVariables() {
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
	
	public void InsertEditedLocalVariable(LocalVariables localVariable) {
		allVariables.set(allVariables.indexOf(localVariable), localVariable);
	}

	public Integer getIndexBySootType(Type t) {

		return allTypeIndexes.get(t.getNumber());
	}

}
