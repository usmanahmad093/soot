package soot.toCIL;

import soot.Type;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;

public class JimpleCodeDetector {
	private static JimpleCodeDetector instance = new JimpleCodeDetector();

	private JimpleCodeDetector() {
	}

	public static JimpleCodeDetector getInstance() {
		return instance;
	}

	public boolean checkStaticInvokeExpr(InvokeExpr invokeExpr) {

		if (invokeExpr instanceof StaticInvokeExpr) {

			if (!isWrapperClassType(invokeExpr) && isValueOfMethod(invokeExpr.getMethod().getName())) {
				return true;
			}

		}

		return false;
	}

	public boolean isWrapperClassType(InvokeExpr invokeExpr) {
		final String otherClassToSearch = "class";

		Type classType = invokeExpr.getMethod().getDeclaringClass().getType();
		String className = Converter.getInstance().getTypeInString(classType);
		String pattern = (className.length() > 4) ? className.substring(0, 5) : className;

		if (pattern.equals(otherClassToSearch)) {
			return true;
		}

		return false;
	}

	public boolean isValueOfMethod(String methodName) {
		final String methodNameToSearch = "valueOf";

		return (methodName.equals(methodNameToSearch)) ? true : false;
	}

	public boolean checkVirtualInvokeExpr(InvokeExpr invokeExpr) {

		if (invokeExpr instanceof VirtualInvokeExpr) {

			String methodName = invokeExpr.getMethod().getName();

			if (!isWrapperClassType(invokeExpr) && isTypeValueMethod(methodName)) {
				return true;
			}

		}

		return false;
	}

	public boolean isTypeValueMethod(String methodName) {
		final String BOOLEANVALUE_METHOD = "intValue";
		final String BYTEVALUE_METHOD = "byteValue";
		final String CHARVALUE_METHOD = "charValue";
		final String DOUBLEVALUE_METHOD = "doubleValue";
		final String FLOATVALUE_METHOD = "floatValue";
		final String INTVALUE_METHOD = "intValue";
		final String LONGVALUE_METHOD = "longValue";
		final String SHORTVALUE_METHOD = "shortValue";

		if (methodName.equals(BOOLEANVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(BYTEVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(CHARVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(DOUBLEVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(FLOATVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(INTVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(LONGVALUE_METHOD)) {
			return true;
		} else if (methodName.equals(SHORTVALUE_METHOD)) {

		}

		return false;
	}

	public boolean isCILObject(Value v) {
		final String patternToSearch = "class";

		String type = Converter.getInstance().getTypeInString(v.getType());
		String pattern = (type.length() > 4) ? type.substring(0, 5) : type;

		if (pattern.equals(patternToSearch)) {
			return true;
		}

		return false;

	}

}
