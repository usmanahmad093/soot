package soot.toCIL;

import java.io.PrintStream;

import soot.jimple.VirtualInvokeExpr;


/**
 * checks all special cases. If a special case occurs: the return argument is true, else: false
 * @author Usman
 *
 */
public class SpecialCasesToConsider {
	
	
	public static boolean isPrintStreamMethod (VirtualInvokeExpr v) {
		String askedType = Converter.getInstance().getTypeInString(v.getMethod().getDeclaringClass().getType());
		String checkPrintStream = "class " + PrintStream.class.getName();
		
		if (askedType.equals(checkPrintStream)) {
			return true;
		}
		
		return false;
	}

}
