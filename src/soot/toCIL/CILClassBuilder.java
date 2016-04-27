package soot.toCIL;

import java.util.ArrayList;

import soot.Modifier;
import soot.SootClass;
import soot.SootField;
import soot.toCIL.structures.CILModifiers;
import soot.util.Chain;

public class CILClassBuilder {
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";

	public static String buildCILClass(SootClass sootClass) {
		StringBuilder sb = new StringBuilder();
		String[] modifiers = CILModifierBuilder.ModifierBuilder(sootClass.getModifiers());
		sb.append(".class ");
		String finalOrNot = "";
		boolean firstValue = true;
		boolean lastValue = false;
		int interfaceIndex = 0;
		
		Chain<SootClass> interfaces = sootClass.getInterfaces();
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
			sb.append(finalOrNot);
			sb.append(" beforefieldinit ");
			sb.append(className);
			sb.append(" extends ");
			sb.append(superClass);
			sb.append(" ");
			
		}
		
		for(SootClass i: interfaces) {
			String comma = (interfaceIndex == (interfaces.size()  - 1))? "":", ";
			String printImplements = (firstValue)?"implements ":"";
			sb.append(printImplements);
			sb.append(i.getName());
			sb.append(comma);
			
			firstValue = false;
			interfaceIndex++;
		}
		
		
		sb.append(" {");
		
		

		return sb.toString();
	}
}
