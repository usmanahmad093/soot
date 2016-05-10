package soot.toCIL.instructions;





import soot.jimple.Stmt;
import soot.toCIL.Converter;
import soot.toCIL.structures.Constant;
import soot.toCIL.structures.Label;
import soot.toCIL.structures.Type;

public class Newarr implements Instruction {
	private Stmt stmt;
	private Label label;
	private Type type;
	private String typeInString;

	public Newarr(Stmt stmt, Type type, String typeInString) {
		final int REMOVEBRACKETS = 2;
		final int REMOVECLASSPATTERN = 6;
		
		this.stmt = stmt;
		this.type = type;
		
		//Remove '[]':
		
		if (Converter.getInstance().isClassType()) {
		  typeInString = typeInString.substring(REMOVECLASSPATTERN, typeInString.length() - REMOVEBRACKETS);
		}
		  
	    this.typeInString = typeInString;
	}

	@Override
	public String getInstruction() {

		switch (type) {
		case INT:
			return "newarr [mscorlib] System.Int32";

		case DOUBLE:
			return "newarr [mscorlib]System.Double";

		case FLOAT:
			return "newarr [mscorlib]System.Single";

		case LONG:
			return "newarr [mscorlib]System.Int64";

		case STRING:
			return "newarr [mscorlib]System.String";

		case BOOLEAN:
			return "newarr [mscorlib]System.Boolean";

		case BYTE:
			return "newarr [mscorlib]System.Byte";

		case CHAR:
			return "newarr [mscorlib]System.Char";

		case SHORT:
			return "newarr [mscorlib]System.Int16";

		default:
			return "newarr " + typeInString;
		}
	}

	@Override
	public Stmt getStmt() {
		// TODO Auto-generated method stub
		return stmt;
	}

	@Override
	public void setLabel(Label label) {
		// TODO Auto-generated method stub
		this.label = label;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return label.getLabel() + ": ";
	}

}
