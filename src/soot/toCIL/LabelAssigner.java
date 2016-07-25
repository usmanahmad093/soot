package soot.toCIL;

import java.util.ArrayList;
import java.util.HashMap;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.LocalsInit;
import soot.toCIL.structures.Label;

/**
 * assigns Label to every CIL Instructions
 * @author Usman
 *
 */
public class LabelAssigner {
	private static LabelAssigner instance = new LabelAssigner();
	private HashMap<Stmt, Label> allTargetlabels;
	private int targetLabelID;
	private int labelID;

	private LabelAssigner() {
		allTargetlabels = new HashMap<>();
		targetLabelID = 0;
		labelID = 0;
	}

	public static LabelAssigner getInstance() {
		return instance;
	}

	/**
	 * creates a Target Label for a specific CIL Instruction
	 * @param target: if the CIL Instruction Object contains this target parameter: target Label will be created and returned; 
	 * else: target label will be returned
	 * @return Target Label
	 */
	public Label CreateTargetLabel(Stmt target) {
		Label targetLabel = allTargetlabels.get(target);

		if (targetLabel == null) {

			targetLabel = new Label("TargetLabel_" + String.valueOf(targetLabelID));
			allTargetlabels.put(target, targetLabel);
			targetLabelID++;
		}

		return targetLabel;
	}

	/**
	 * fetches the target Label with the help of the jimple statement
	 * @param stmt: realises a jimple statement
	 * @return CIL Label
	 */
	public Label getTarget(Stmt stmt) {

		Label lbl = allTargetlabels.get(stmt);

		return lbl;
	}

	/**
	 * assign labels to CIL Instructions. If CIL Object contains a Target Jimple Statement: Target Label will be assigned,
	 * else: "normal" Labels will be assigned
	 * @param allInstructions contains CIL Instructions without Labels
	 * @return edited CIL Instructions
	 */
	public ArrayList<Instruction> AssignLabelsToInstructions(ArrayList<Instruction> allInstructions) {

		Label tmpTargetLabel = new Label();
		Label label = new Label();

		for (Instruction instr : allInstructions) {
			Stmt stmt = instr.getStmt();

			if (!(instr instanceof LocalsInit)) {
				label = getTarget(stmt);

				if (label != null) {
					if (!tmpTargetLabel.equals(label)) {
						tmpTargetLabel = label;
					} else {
						label = new Label("Label_" + String.valueOf(labelID));
						labelID++;
					}

				} else {
					label = new Label("Label_" + String.valueOf(labelID));
					labelID++;
				}

			}

			instr.setLabel(label);
			allInstructions.set(allInstructions.indexOf(instr), instr);

		}

		return allInstructions;
	}

	/**
	 * Assign a Label to the Instruction
	 * @param instr realises a CIL Instruction
	 * @return edited CIL Instruction
	 */
	public Instruction AssignLabelToInstruction(Instruction instr) {
		Label label = new Label();

		label = new Label("Label_" + String.valueOf(labelID));
		labelID++;

		instr.setLabel(label);

		return instr;
	}

}
