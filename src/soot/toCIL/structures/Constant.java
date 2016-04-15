package soot.toCIL.structures;

public class Constant {
	private Type type;
	private String value;
	
	public Constant(Type type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
}
