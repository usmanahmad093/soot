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

public class CILMethodBuilder {
	

	
	public static String buildCILMethodHeader(SootMethod sootMethod, ArrayList<Parameter> allParameters) {
		String returnType = Converter.getInstance().getTypeInString(sootMethod.getReturnType());
		String params = getParamsInString(allParameters);
		StringBuilder sb = new StringBuilder();
		String[]modifiers = CILModifierBuilder.ModifierBuilder(sootMethod.getModifiers());
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
			if (isOverrideMethod(sootMethod, sootMethod.getDeclaringClass().getInterfaces(), superClass, interfaces)) {
				sb.append(" hidebysig newslot ");
				sb.append("virtual final instance ");
			} else {
				
				if (sootMethod.isAbstract()) {
					sb.append(" hidebysig newslot ");
					sb.append(modifiers[CILModifiers.C_ABSTRACT]);
					sb.append(" virtual ");
				} else {
					sb.append(" hidebysig ");
				}
				
				sb.append(modifiers[CILModifiers.C_STATICORINSTANCE]);
				sb.append(" ");
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
	
	private static boolean isOverrideMethod(SootMethod askedMethod, Chain<SootClass> allInterfaces, SootClass superClass, Chain<SootClass> interfaces) {
		
		List<SootMethod> allMethods = superClass.getMethods();
		
		
		for(SootMethod m: allMethods) {
			if (superClass.isAbstract() && askedMethod.getSubSignature().equals(m.getSubSignature())) {
				return true;
			}
		}
		
		for(SootClass i: allInterfaces) {
			allMethods = i.getMethods();
			
			for(SootMethod interfaceMethod: allMethods) {
				String subsignatureInterface = interfaceMethod.getSubSignature();
				String subsignatureAskedMethod = askedMethod.getSubSignature();
				
				if (subsignatureAskedMethod.equals(subsignatureInterface)) {
					return true;
				}
			}
			
		}
		
		
		return false;
	}
	
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
