package soot.toCIL;

import java.util.ArrayList;


import soot.SootClass;
import soot.toCIL.structures.CILModifiers;

/**
 * generates a Class Definition in CIL
 * @author Usman
 *
 */
public class CILClassBuilder {
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";
	private static ArrayList<SootClass> baseInterfaces = new ArrayList<>();

	
	/**
	 * generates a class definition in CIL
	 * @param sootClass is a Soot Class
	 * @return class definition in CIL
	 */
	public static String buildCILClassHeader(SootClass sootClass) {
		baseInterfaces = new ArrayList<>();
		fillInterfaces(sootClass);

		StringBuilder sb = new StringBuilder();
		String[] modifiers = CILModifierBuilder.ModifierBuilder(sootClass.getModifiers());
		sb.append(".class ");
		String finalOrNot = "";
		int interfaceIndex = 0;


		String className = sootClass.getName();
		String superClass;
		if (sootClass.hasSuperclass())
			superClass = (sootClass.getSuperclass().getName().equals(Object.class.getName()) ? OBJECT_CLASS
					: sootClass.getSuperclass().getName());
		else
			superClass = OBJECT_CLASS;

		if (sootClass.isFinal()) {
			finalOrNot = CILModifiers.CLASS_FINAL;
		}

		if (sootClass.isInterface()) {

			sb.append("interface ");
			sb.append(modifiers[CILModifiers.C_ACCESS]);
			sb.append(" abstract auto ansi ");
			sb.append(className);
			sb.append(" ");


		} else {

			sb.append(modifiers[CILModifiers.C_ACCESS]);
			sb.append(" ");
			sb.append(modifiers[CILModifiers.C_ABSTRACT]);
			sb.append(" auto ansi ");
			sb.append(modifiers[CILModifiers.C_FINAL]);
			sb.append(finalOrNot);
			sb.append(" beforefieldinit ");
			sb.append(className);
			sb.append(" extends ");
			sb.append(superClass);
			sb.append(" ");

		}
		
		/*
		 * If The SootClass implements one or several Interfaces... keyword "implemented" has to be printed
		 */
		if (baseInterfaces.size() != 0) {
			sb.append("implements ");
		}

		/*
		 * Print Interfaces which are implemented by the SootClass
		 */
		for (SootClass sootInterface : baseInterfaces) {
			String name = sootInterface.getName();

			sb.append(name);
			String comma = ((interfaceIndex + 1)== (baseInterfaces.size())) ? "" : ", ";
			
			
			sb.append(comma);
			
			interfaceIndex++;

		
		}

		sb.append(" {");

		return sb.toString();
	}

	/**
	 * This Method fills the Interfaces in ArrayList which are implemented by the current SootClass
	 * @param sootClass is a Soot Class
	 */
	private static void fillInterfaces(SootClass sootClass) {
		SootClass baseClass = null;

		if (sootClass.getInterfaceCount() != 0) {
			baseInterfaces.add(sootClass.getInterfaces().getFirst());
			baseClass = sootClass.getInterfaces().getFirst();
			fillInterfaces(baseClass);
		}
		
	}
}
