package soot.toCIL;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;

public class SpecialcasesToIgnore {
	private static String PRINTSTREAM = "class java.io.PrintStream";

	public static boolean isPrintStreamVariable(Local l) {
		String type = Converter.getInstance().getTypeInString(l.getType());

		if (type.equals(PRINTSTREAM)) {
			return true;
		}

		return false;
	}

	public static boolean isPrintStreamAssignment(Unit u) {

		if (u instanceof AssignStmt) {

			AssignStmt stmt = (AssignStmt) u;
			Value v = stmt.getRightOp();

			String returnType = Converter.getInstance().getTypeInString(v.getType());

			if (returnType.equals(PRINTSTREAM)) {
				return true;
			}

		}

		return false;
	}
}
