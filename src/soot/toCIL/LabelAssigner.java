package soot.toCIL;

import java.util.ArrayList;
import java.util.HashMap;

import soot.jimple.Stmt;
import soot.toCIL.instructions.Instruction;
import soot.toCIL.instructions.LocalsInit;
import soot.toCIL.structures.Label;

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

	public Label CreateTargetLabel(Stmt target) {
		Label targetLabel = allTargetlabels.get(target);

		if (targetLabel == null) {

			targetLabel = new Label("TargetLabel_" + String.valueOf(targetLabelID));
			allTargetlabels.put(target, targetLabel);
			targetLabelID++;
		}

		return targetLabel;
	}

	public Label getTarget(Stmt stmt) {

		Label lbl = allTargetlabels.get(stmt);

		return lbl;
	}

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

	public Instruction AssignLabelToInstruction(Instruction instr) {

		Label tmpTargetLabel = new Label();
		Label label = new Label();

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

		return instr;
	}
}
