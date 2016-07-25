package soot.toCIL;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.toCIL.structures.CILModifiers;
import soot.toCIL.structures.Parameter;
import soot.toCIL.structures.Variable;
import soot.util.Chain;

/**
 * generates a CIL Methoddefinition
 * @author Usman
 *
 */
public class CILMethodBuilder {

	/**
	 * generates a CIL Methoddefinition
	 * @param sootMethod is a Soot Method
	 * @param allParameters contains all CIL Parameters
	 * @return CIL Methoddefinition in String Format
	 */
	public static String buildCILMethodHeader(SootMethod sootMethod, ArrayList<Parameter> allParameters) {
		String returnType = Converter.getInstance().getTypeInString(sootMethod.getReturnType());
		String params = getParamsInString(allParameters);
		StringBuilder sb = new StringBuilder();
		String[] modifiers = CILModifierBuilder.ModifierBuilder(sootMethod.getModifiers());
		SootClass superClass = sootMethod.getDeclaringClass().getSuperclass();
		Chain<SootClass> interfaces = sootMethod.getDeclaringClass().getInterfaces();
		String methodName = "";

		sb.append(".method ");
		sb.append(modifiers[CILModifiers.C_ACCESS]);

		if (sootMethod.getDeclaringClass().isInterface()) {
			methodName = sootMethod.getName();
			sb.append(" hidebysig ");
			sb.append("newslot abstract virtual instance ");
		} else if (sootMethod.isConstructor()) {
			sb.append(" hidebysig ");
			methodName = ".ctor";
			sb.append("specialname rtspecialname instance ");

		} else if (sootMethod.isStaticInitializer()) {
			sb.append("public hidebysig ");
			methodName = ".cctor";
			sb.append("specialname rtspecialname static ");

		} else {
			methodName = sootMethod.getName();
			if (isVirtualMethod(sootMethod, superClass)) {
				sb.append(" hidebysig virtual ");
				
				if (sootMethod.isFinal()) {
					sb.append("final");
				}
				
				sb.append(" instance ");
			} else if (checkInterfaceMethod(sootMethod, interfaces)) {
				sb.append(" hidebysig newslot virtual final instance ");
			} else {

				if (sootMethod.isAbstract()) {
					sb.append(" hidebysig newslot ");
					sb.append(modifiers[CILModifiers.C_ABSTRACT]);
					sb.append(" virtual ");
				} else {
					String staticornot = (modifiers[CILModifiers.C_STATICORINSTANCE].equals(CILModifiers.STATIC)?CILModifiers.STATIC:"instance");
					
					sb.append(" hidebysig ");
					sb.append(staticornot);
					sb.append(" ");
				}

			
			}
		}

		sb.append(returnType);
		sb.append(" ");
		sb.append(methodName);
		sb.append("(");
		sb.append(params);
		sb.append(") cil managed {");

		return sb.toString();
	}

	/**
	 * checks if the Soot Method is a Virtual Method
	 * @param askedMethod is an asked Soot Method
	 * @param superClass 
	 * @return true: it is a Virtual Method;
	 *         false: it is not a Virtual Method
	 */
	private static boolean isVirtualMethod(SootMethod askedMethod, SootClass superClass) {

		List<SootMethod> allMethods = superClass.getMethods();

		for (SootMethod m : allMethods) {
			if (superClass.isAbstract() && askedMethod.getSubSignature().equals(m.getSubSignature())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * This Method checks if the SootMethod is an Interface Method. 
	 * @param askedMethod
	 * @param allInterfaces will contain all Interfaces after executing this method
	 */
	private static boolean checkInterfaceMethod(SootMethod askedMethod, Chain<SootClass> allInterfaces) {

		for (SootClass i : allInterfaces) {
			List<SootMethod>allMethods = i.getMethods();

			for (SootMethod interfaceMethod : allMethods) {
				String subsignatureInterface = interfaceMethod.getSubSignature();
				String subsignatureAskedMethod = askedMethod.getSubSignature();

				if (subsignatureAskedMethod.equals(subsignatureInterface)) {
					return true;
				}
			}
			
			/*
			 * Look at subsignatures in the Super InterfaceClasses
			 */
			Chain<SootClass> superInterfaceClasses = i.getInterfaces();
			if (superInterfaceClasses != null) {
				if (checkInterfaceMethod(askedMethod, superInterfaceClasses) == true) {
					return true;
				}
			}

		}

		return false;
	}

	

	/**
	 * 
	 * generates a Parameter List
	 * 
	 * @param allParameters contains all Parameters after executing this method
	 *        in String in order to fill the parameter list of the CIL Method
	 * @return parameter list in String
	 */
	public static String getParamsInString(ArrayList<Parameter> allParameters) {
		String params = "";
		boolean firstParam = true;

		for (Variable v : allParameters) {
			String comma = (firstParam == true) ? "" : ", ";
			params += comma;
			params += v.getVariableType() + " ";
			params += v.getVariableName();

			firstParam = false;
		}

		return params;
	}

}
