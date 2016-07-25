package soot.toCIL;

import soot.Local;
import soot.Unit;
import soot.Value;
import soot.JastAddJ.ThrowStmt;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JThrowStmt;

/**
 * checks all special cases. If a special case occurs: the return argument is true, else: false
 * If a speical Case occurs: specific CIL Instruction won't be created, else: it will be created
 * @author Usman
 *
 */
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

	public static boolean isCatchSection(Stmt stmt) {
		if (stmt instanceof IdentityStmt) {
			if (((IdentityStmt) stmt).getRightOp() instanceof CaughtExceptionRef) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isEndCatchSection(Stmt stmt) {
		if (stmt instanceof JThrowStmt) {
				return true;
		}
		
		return false;
	}

}
