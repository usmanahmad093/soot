package soot.toCIL.structures;

public class Label {
	private String labelName;
	
	public Label() {
		labelName = "";
	}
	
	public Label(String labelName) {
		this.labelName = labelName;
	}
	
	public String getLabel() {
		return labelName;
	}

}
