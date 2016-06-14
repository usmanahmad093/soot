package soot.toCIL;

import java.util.ArrayList;
import java.util.HashMap;

import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Stmt;
import soot.toCIL.instructions.EndFinally;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.structures.Label;

public class SemanticDetection {

	private static SemanticDetection instance = new SemanticDetection();
	private static boolean cilEnterMonitor = false;
	private static boolean cilExitMonitor = false;
	private static String exitMonitorStart = null;
	private static String exitMonitorEnd = null;

	private SemanticDetection() {

	}

	/*
	 * This Method detects if the List of converted CIL Instructions contains
	 * Enter/Exit Monitor Statement(s), if yes: Modify the CIL Instructions
	 */
	public static void DetectEnterAndExitMonitorStmt(ArrayList<Instruction> allInstructions) {
		ArrayList<Instruction> instrWithCorrectSemantic = new ArrayList<>(); // = Instruction with the correct semantic
		int max = allInstructions.size() - 1;

		for (int i = 0; i <= max; i++) {
			
			
			Stmt currentjimpleStmt = allInstructions.get(i).getStmt();
			Stmt followingJimpleStmt = (max > i + 1)? allInstructions.get(i + 1).getStmt(): null;


			if (currentjimpleStmt instanceof EnterMonitorStmt) {
				cilEnterMonitor = true;
			} else {
				cilEnterMonitor = false;
			}
			
			if (!(followingJimpleStmt instanceof ExitMonitorStmt && !cilEnterMonitor)) {
				instrWithCorrectSemantic.add(allInstructions.get(i));
			} 

		}
	}

}
