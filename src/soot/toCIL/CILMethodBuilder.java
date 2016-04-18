package soot.toCIL;

import java.util.ArrayList;

import soot.SootField;
import soot.SootMethod;
import soot.toCIL.structures.Parameter;
import soot.toCIL.structures.Variable;

public class CILMethodBuilder {
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	private static final String PROTECTED = "family";
	

	
	public static String buildCILMethodHeader(SootMethod sootMethod, ArrayList<Parameter> allParameters) {
		String cilMethodHeader = ".method";
		String returnType = Converter.getInstance().getTypeInString(sootMethod.getReturnType());
		String params = getParamsInString(allParameters);
		StringBuilder sb = new StringBuilder();
		
		
		if(sootMethod.isConstructor()) {
			String modifier = "";
			
			if (sootMethod.isPrivate()) {
				modifier = PRIVATE;
			} else if (sootMethod.isProtected()) {
				modifier = PROTECTED;
			} else if (sootMethod.isPublic()) {
				modifier = PUBLIC;
			}
			
			
			
			sb.append(cilMethodHeader);
			sb.append(" ");
			sb.append(modifier);
			sb.append(" ");
			sb.append("hidebysig specialname rtspecialname instance");
			sb.append(" ");
			sb.append(returnType);
			sb.append(" .ctor(");
			sb.append(params);
			sb.append(")");
			sb.append(" cil managed {");
			
			
		} 

		return sb.toString();
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
