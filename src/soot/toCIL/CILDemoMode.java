package soot.toCIL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.net.ssl.internal.www.protocol.https.Handler;

import javassist.compiler.ast.Expr;
import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Stmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.toolkits.thread.mhp.stmt.BeginStmt;
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
import soot.toCIL.instructions.trysection.BeginTrySection;
import soot.toCIL.instructions.trysection.EndTrySection;
import soot.toCIL.structures.CILModifiers;
import soot.toCIL.structures.Class;
import soot.toCIL.structures.LocalVariables;
import soot.toCIL.structures.Member;
import soot.toCIL.structures.Method;
import soot.toCIL.structures.Parameter;
import soot.util.Chain;

/*
 * Only for Test purposes
 */
public class CILDemoMode {
	private Class refClass;
	int hashmapSize = 0;
	public static final String STRANGE_TYPE = "byte";
	

	int test;

	private CILDemoMode() {
	}

	public void showJimpleBody(SootClass clazz) {

		System.out.println("Class: " + clazz.getName() + " Superclass: " + clazz.getSuperclass());
		for (SootMethod method : clazz.getMethods()) {
			if (method.isConcrete()) {
				Body b = method.retrieveActiveBody();
				System.out.println(method.getName());

				Chain<Trap> allTraps = b.getTraps();
				
				//if (allTraps.size() != 0) {
					System.out.println(b.toString());
				//}

				for (Trap t : allTraps) {
					System.out.println("Beginunit: " + t.getBeginUnit().toString());
					System.out.println("Endunit: " + t.getEndUnit().toString());
					System.out.println("Exception: " + t.getException().toString());
					System.out.println("Typ: " + t.getException().getType());
					System.out.println("Handler: " + t.getHandlerUnit().toString());
					
					JIdentityStmt u = (JIdentityStmt) t.getHandlerUnit();
					CaughtExceptionRef ref = (CaughtExceptionRef) u.getRightOp();
					
					
					
				
					System.out.println("");
				}

			} else {
				System.out.println(method.getSignature() + " " + method.getName() + " ();");
			}
		}

	}

	public void showClass(SootClass clazz) {
		System.out.println(clazz.toString());
	}

	public static CILDemoMode getInstance() {
		return new CILDemoMode();
	}

	/*
	 * This Method fills the CIL Constructs into the StringBuilder
	 */
	public void printCILCode(StringBuilder sbForTextFile) throws IOException {

		ArrayList<soot.toCIL.structures.Method> allMethods = refClass.getMethods();

		sbForTextFile.append(refClass.getStartBody() + "\n");
		System.out.println(refClass.getStartBody());
		printFields(refClass, sbForTextFile);

		for (soot.toCIL.structures.Method m : allMethods) {

			ArrayList<Instruction> allInstructions = m.getInstructions();

			sbForTextFile.append(m.getStartBody() + "\n");
			System.out.println(m.getStartBody());
			if (m.isMain()) {
				Entrypoint entrypointInstr = new Entrypoint();
				sbForTextFile.append(entrypointInstr.getInstruction() + "\n");
				System.out.println(entrypointInstr.getInstruction());
			}

			if (m.getMaxStack() != 0) {
				sbForTextFile.append(".maxstack " + m.getMaxStack() + "\n");
				System.out.println(".maxstack " + m.getMaxStack());

			}
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

	/*
	 * This Method fills the cil attributes into the StringBuilder
	 */

	private void printFields(Class c, StringBuilder sbForTextFile) {
		// TODO Auto-generated method stub
		ArrayList<Member> allMembers = c.getMembers();

		for (Member member : allMembers) {

			sbForTextFile.append(member.getCILRepresentation());
			sbForTextFile.append("\n");

			System.out.print(member.getCILRepresentation());
			System.out.print("\n");

		}

	}

	/**
	 * Only For Testing purposes
	 */
	public void demoInnerClass(SootClass clazz) {
		if (clazz.isInnerClass()) {
			System.out.println("INNER CLASS");
		}
	}

	/*
	 * This Method converts the Jimple Code in CIL Code
	 */
	public void transformJimpleToCIL(SootClass clazz) throws ClassNotFoundException {

		Chain<SootField> sootFields = clazz.getFields();
		ArrayList<Member> allMembers = new ArrayList<>();

		for (SootField sootField : sootFields) {
			String cilFieldRepresenation = CILFieldBuilder.buildCILField(sootField);
			String fieldName = sootField.getName();
			String type = Converter.getInstance().getTypeInString(sootField.getType());

			type = (CILModifierBuilder.isVolatile(sootField.getModifiers()) ? (type + " " + CILModifiers.VOLATILE)
					: type);

			Member member = new Member(cilFieldRepresenation, fieldName, type);
			allMembers.add(member);
		}

		refClass = new Class(allMembers);
		String startBody = CILClassBuilder.buildCILClassHeader(clazz);
		refClass.setStartBody(startBody);

		for (SootMethod sootMethod : clazz.getMethods()) {

			soot.toCIL.structures.Method cilMethod;
			
			

			cilMethod = new soot.toCIL.structures.Method(sootMethod, refClass);

			List<Type> allTypes = sootMethod.getParameterTypes();
			addParams(allTypes, cilMethod);

			String cilMethodHeader = CILMethodBuilder.buildCILMethodHeader(sootMethod, cilMethod.getAllParameters());
			cilMethod.setStartBody(cilMethodHeader);

			if (sootMethod.isConcrete()) {
				Body body = sootMethod.retrieveActiveBody();
				Chain<Trap> allTraps = body.getTraps();
				cilMethod.setTraps(allTraps);

				Chain<Local> allLocals = body.getLocals();
				

				addVariables(allLocals, cilMethod);

				transformAndAddInstructions(body.getUnits(), cilMethod, allTraps);
				detectMaxStack(cilMethod);
			}

			refClass.addMethod(cilMethod);
		}

	}

	/*
	 * This function detects how many Values should be maximal pushed into Stack
	 */

	private void detectMaxStack(soot.toCIL.structures.Method cilMethod) {
		int countStackElements = 0;
		int maxStack = 0;

		ArrayList<Instruction> allInstructions = cilMethod.getInstructions();

		if (cilMethod.isVoid()) {
			maxStack = 8;
		}

		for (Instruction instr : allInstructions) {

			if (instr instanceof AddInstruction) {
				countStackElements--;
			} else if (instr instanceof Newobj) {
				countStackElements++;
			} else if (instr instanceof And) {
				countStackElements--;
			} else if (instr instanceof Ceq) {
				countStackElements--;
			} else if (instr instanceof Cgt) {
				countStackElements--;
			} else if (instr instanceof Clt) {
				countStackElements--;
			} else if (instr instanceof Div) {
				countStackElements--;
			} else if (instr instanceof Ldarg) {
				countStackElements++;
			} else if (instr instanceof LoadInstruction) {
				countStackElements++;
			} else if (instr instanceof Mul) {
				countStackElements--;
			} else if (instr instanceof Or) {
				countStackElements--;
			} else if (instr instanceof Rem) {
				countStackElements--;
			} else if (instr instanceof Shl) {
				countStackElements--;
			} else if (instr instanceof Shr) {
				countStackElements--;
			} else if (instr instanceof ShrUn) {
				countStackElements--;
			} else if (instr instanceof StoreInstruction) {
				countStackElements--;
			} else if (instr instanceof Sub) {
				countStackElements--;
			} else if (instr instanceof Xor) {
				countStackElements--;
			}

			if (maxStack < countStackElements) {
				maxStack = countStackElements;
			}
		}

		cilMethod.setMaxStack(maxStack);

	}

	/*
	 * Add Local Variables into the Collection
	 */
	private void addVariables(Chain<Local> allLocals, Method cilMethod) throws ClassNotFoundException {
		ArrayList<LocalVariables> allVariables = new ArrayList<>();
		final String SPECIALCASE = "array";

		// Local Variables
		for (Local l : allLocals) {

			String type = Converter.getInstance().getTypeInString(l.getType());

			// TODO: wie kann ich den modfier einer lokalen Variable abrufen?
			String name = l.getName();
			if (l.getName().equals(SPECIALCASE)) {
				name = "'" + name + "'";
			}
			LocalVariables var = new LocalVariables(name, type);
			allVariables.add(var);
		}

		cilMethod.setVariables(allVariables);
	}

	/*
	 * Add Parameters into the Collection
	 */
	private void addParams(List<Type> allTypes, soot.toCIL.structures.Method cilMethod) throws ClassNotFoundException {
		ArrayList<Parameter> allParameters = new ArrayList<>();

		int counterParam = 0;

		for (Type t : allTypes) {
			String type = Converter.getInstance().getTypeInString(t);

			Parameter param = new Parameter(counterParam, "param" + String.valueOf(counterParam), type);
			allParameters.add(param);

			counterParam++;
		}

		cilMethod.setParameters(allParameters);
	}

	private void transformAndAddInstructions(PatchingChain<Unit> allUnits, soot.toCIL.structures.Method cilMethod, Chain<Trap> allTraps) {
		ArrayList<LocalVariables> allVariables = cilMethod.getAllVariables();
		StmtVisitor stmtV = new StmtVisitor(cilMethod);
		final boolean successfull = true;
		
		// If Variables declared, add LocalsInit Instruction
		if (allVariables.size() != 0) {
			LocalsInit initInstruction = new LocalsInit(allVariables);
			stmtV.buildInstruction(initInstruction);
		}

		for (Unit u : allUnits) {
			
			Stmt stmt = (Stmt) u;
			
			
			
			if (cilMethod.getTrapBeginUnitByStmt(stmt) != null) {
				BeginTrySection tryInstruction = new BeginTrySection(stmt);
				cilMethod.addInstruction(tryInstruction);
			}
			
			u.apply(stmtV);
			
			if (cilMethod.getTrapEndUnitByStmt(stmt) != null) {
				EndTrySection endTrySection = new EndTrySection(stmt);
				cilMethod.addInstruction(endTrySection);
			}
		}

		ArrayList<Instruction> allInstructions = stmtV.getInstructions();

		allInstructions = LabelAssigner.getInstance().AssignLabelsToInstructions(allInstructions);
		cilMethod.setInstructions(allInstructions);
	}

	

}
