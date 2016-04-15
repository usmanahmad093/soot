package soot.toCIL;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xml.internal.serialize.Method;

import jdk.nashorn.internal.codegen.CompilerConstants.Call;
import jdk.nashorn.internal.runtime.StoredScript;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.baf.StoreInst;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.toCIL.instructions.AddInstruction;
import soot.toCIL.instructions.And;
import soot.toCIL.instructions.Ceq;
import soot.toCIL.instructions.Cgt;
import soot.toCIL.instructions.Clt;
import soot.toCIL.instructions.Div;
import soot.toCIL.instructions.Entrypoint;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.Ldarg;
import soot.toCIL.instructions.LoadInstruction;
import soot.toCIL.instructions.LocalsInit;
import soot.toCIL.instructions.Mul;
import soot.toCIL.instructions.Newobj;
import soot.toCIL.instructions.Or;
import soot.toCIL.instructions.Rem;
import soot.toCIL.instructions.Shl;
import soot.toCIL.instructions.Shr;
import soot.toCIL.instructions.ShrUn;
import soot.toCIL.instructions.StoreInstruction;
import soot.toCIL.instructions.Sub;
import soot.toCIL.instructions.Xor;
import soot.toCIL.structures.Class;
import soot.toCIL.structures.Member;
import soot.toCIL.structures.OtherVariables;
import soot.toCIL.structures.Parameter;
import soot.util.Chain;

/*
 * Only for Test purposes
 */
public class CILDemoMode {
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";
	private Class refClass;
	int hashmapSize = 0;
	public static final String STRANGE_TYPE = "byte";

	private CILDemoMode() {
	}

	public void showJimpleBody(SootClass clazz) {
		for (SootMethod method : clazz.getMethods()) {
			Body b = method.retrieveActiveBody();
			System.out.println(b.toString());;
		}
		
	}

	public static CILDemoMode getInstance() {
		return new CILDemoMode();
	}

	public void printCILCode(StringBuilder sbForTextFile) throws IOException {

		ArrayList<soot.toCIL.structures.Method> allMethods = refClass.getMethods();

		sbForTextFile.append(refClass.getStartBody() + "\n");
		System.out.println(refClass.getStartBody());
		printFields(refClass, sbForTextFile);
		String mainMethodName = Scene.v().getMainMethod().getName();

		for (soot.toCIL.structures.Method m : allMethods) {
			
			ArrayList<Instruction> allInstructions = m.getInstructions();

			sbForTextFile.append(m.getStartBody() + "\n");
			System.out.println(m.getStartBody());
			if(m.getMethodName().equals(mainMethodName)) {
				Entrypoint entrypointInstr = new Entrypoint();
				sbForTextFile.append(entrypointInstr.getInstruction() + "\n");
				System.out.println(entrypointInstr.getInstruction());
			}

			sbForTextFile.append(".maxstack " + m.getMaxStack() + "\n");
			System.out.println(".maxstack " + m.getMaxStack());
			for (Instruction instr : allInstructions) {

				sbForTextFile.append(instr.getLabel() + instr.getInstruction() + "\n");
				System.out.println(instr.getLabel() + instr.getInstruction());
			}

			sbForTextFile.append(m.getEndBody() + "\n");
			System.out.println(m.getEndBody());

			sbForTextFile.append("\n");
			System.out.println("");
		}

		sbForTextFile.append(refClass.getEndBody() + "\n");
		System.out.println(refClass.getEndBody());

	}

	private void printFields(Class c, StringBuilder sbForTextFile) {
		// TODO Auto-generated method stub
		ArrayList<Member> allMembers = c.getMembers();

		for (Member member : allMembers) {

			sbForTextFile.append(".field ");
			sbForTextFile.append(member.getModifier() + " ");
			sbForTextFile.append(member.getVariableType() + " ");
			sbForTextFile.append(member.getVariableName());
			sbForTextFile.append("\n");

			System.out.print(".field ");
			System.out.print(member.getModifier() + " ");
			System.out.print(member.getVariableType() + " ");
			System.out.print(member.getVariableName());
			System.out.print("\n");

		}

	}





	public void transformJimpleToCIL(SootClass clazz) throws ClassNotFoundException {

		String className = clazz.getName();
		String superClass;
		if (clazz.hasSuperclass())
			superClass = (clazz.getSuperclass().getName().equals(Object.class.getName()) ? OBJECT_CLASS
					: clazz.getSuperclass().getName());
		else
			superClass = OBJECT_CLASS;

		Chain<SootField> sootFields = clazz.getFields();
		ArrayList<Member> allMembers = new ArrayList<>();

	
		for (SootField sootField : sootFields) {
			String fieldName = sootField.getName();
			String type = Converter.getInstance().getTypeInString(sootField.getType());

			Member member = new Member(sootField, fieldName, type, false, false);
			allMembers.add(member);
		}

		// TODO: Modifier abrufen
		String modifier = "private";
		refClass = new Class(modifier, className, superClass, allMembers);

		for (SootMethod method : clazz.getMethods()) {
			
		
			
			
			soot.toCIL.structures.Method cilMethod;
			if (method.isStaticInitializer()) {

			}
			if (method.isConstructor()) {
				cilMethod = new soot.toCIL.structures.Method(method, refClass);
			} else {
				cilMethod = new soot.toCIL.structures.Method(method, refClass);
			}

			cilMethod.setMethodName(method.getName());
			cilMethod.setReturnType(Converter.getInstance().getTypeInString(method.getReturnType()));

			if (method.isConcrete()) {
				Body body = method.retrieveActiveBody();
				Chain<Local> allLocals = body.getLocals();
				List<Type> allTypes = method.getParameterTypes();

				addVariables(allLocals, cilMethod);
				addParams(allTypes, cilMethod);
				cilMethod.setRightCILMethod();

				transformAndAddInstructions(body.getUnits(), cilMethod);
				detectMaxStack(cilMethod);
			}
			
			

			refClass.addMethod(cilMethod);
		}

	}

	

	private void detectMaxStack(soot.toCIL.structures.Method cilMethod) {
		int countStackElements = 0;
		int maxStack = 0;

		ArrayList<Instruction> allInstructions = cilMethod.getInstructions();

		if (cilMethod.getReturnType().equals("void")) {
			maxStack = 8;
		}

		for (Instruction instr : allInstructions) {

			if (instr instanceof AddInstruction) {
				countStackElements -= 2;
			} else if (instr instanceof Newobj) {
				countStackElements++;
			} else if (instr instanceof And) {
				countStackElements -= 2;
			} else if (instr instanceof Ceq) {
				countStackElements -= 2;
			} else if (instr instanceof Cgt) {
				countStackElements -= 2;
			} else if (instr instanceof Clt) {
				countStackElements -= 2;
			} else if (instr instanceof Div) {
				countStackElements -= 2;
			} else if (instr instanceof Ldarg) {
				countStackElements++;
			} else if (instr instanceof LoadInstruction) {
				countStackElements++;
			} else if (instr instanceof Mul) {
				countStackElements -= 2;
			} else if (instr instanceof Or) {
				countStackElements -= 2;
			} else if (instr instanceof Rem) {
				countStackElements -= 2;
			} else if (instr instanceof Shl) {
				countStackElements -= 2;
			} else if (instr instanceof Shr) {
				countStackElements -= 2;
			} else if (instr instanceof ShrUn) {
				countStackElements -= 2;
			} else if (instr instanceof StoreInstruction) {
				countStackElements--;
			} else if (instr instanceof Sub) {
				countStackElements -= 2;
			} else if (instr instanceof Xor) {
				countStackElements -= 2;
			} else if (instr instanceof soot.toCIL.instructions.Call) {
				soot.toCIL.instructions.Call callInstr = (soot.toCIL.instructions.Call) instr;
				
				String returnType = callInstr.getReturnType();
				
				if(!returnType.equals("void")) {
					countStackElements++;
				}
				
			}

			if (maxStack < countStackElements) {
				maxStack = countStackElements;
			}
		}

		cilMethod.setMaxStack(maxStack);

	}
	

	private void addVariables(Chain<Local> allLocals, soot.toCIL.structures.Method cilMethod) throws ClassNotFoundException {
		ArrayList<OtherVariables> allVariables = new ArrayList<>();

		// Local Variables
		for (Local l : allLocals) {

			String type = Converter.getInstance().getTypeInString(l.getType());

			// TODO: wie kann ich den modfier einer lokalen Variable abrufen?
			OtherVariables var = new OtherVariables(l.getName(), type, false);
			allVariables.add(var);
		}

		cilMethod.setVariables(allVariables);
	}

	private void addParams(List<Type> allTypes, soot.toCIL.structures.Method cilMethod) throws ClassNotFoundException {
		ArrayList<Parameter> allParameters = new ArrayList<>();
		HashMap<Integer, Integer> allTypeIndexes = new HashMap<>();

		int counterParam = 0;

		for (Type t : allTypes) {
			String type = Converter.getInstance().getTypeInString(t);

			Parameter param = new Parameter("param" + String.valueOf(counterParam), type, false, counterParam);
			allParameters.add(param);
			allTypeIndexes.put(t.getNumber(), counterParam);

			counterParam++;
		}

		cilMethod.setParameters(allParameters);
		cilMethod.setTypeIndexes(allTypeIndexes);
	}

	private void transformAndAddInstructions(PatchingChain<Unit> allUnits, soot.toCIL.structures.Method cilMethod) {
		ArrayList<OtherVariables> allVariables = cilMethod.getAllVariables();
		StmtVisitor stmtV = new StmtVisitor(cilMethod, refClass);
		
		if(cilMethod.getMethodName().equals("testVirtualInvokeStmt()")) {
			int test = 0;
		}

		if (allVariables.size() != 0) {
			LocalsInit initInstruction = new LocalsInit(allVariables);
			stmtV.buildInstruction(initInstruction);
		}

		for (Unit u : allUnits) {
			u.apply(stmtV);
		}

		ArrayList<Instruction> allInstructions = stmtV.getInstructions();

		allInstructions = LabelAssigner.getInstance().AssignLabelsToInstructions(allInstructions);
		cilMethod.setInstructions(allInstructions);
	}

}
