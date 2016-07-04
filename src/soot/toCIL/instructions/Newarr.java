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
	private int dimensions;

	public Newarr(Stmt stmt, Type type, String typeInString, int dimensions, int maxDimensions) {
		final int REMOVEBRACKETS = 2 * maxDimensions;
		final int REMOVECLASSPATTERN = 6;

		this.stmt = stmt;
		this.type = type;
		this.dimensions = dimensions;

		// Remove Brackets '[]':
		if (Converter.getInstance().isClassType()) {
			typeInString = typeInString.substring(REMOVECLASSPATTERN, typeInString.length() - REMOVEBRACKETS); //Remove 'class'
		} else {
			typeInString = typeInString.substring(0, typeInString.length() - REMOVEBRACKETS);
		}

		this.typeInString = typeInString;
	}

	@Override
	public String getInstruction() {
		final int lastDimension = 0;
		String brackets = "";

		// concat brackets
		for (int i = 0; i < dimensions; i++) {
			brackets += "[]";
		}

		switch (type) {
		case INT:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib] System.Int32";
			} else {
				return "newarr int32" + brackets;
			}

		case DOUBLE:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Double";
			} else {
				return "newarr float64" + brackets;
			}

		case FLOAT:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Single";
			} else {
				return "newarr float32" + brackets;
			}

		case LONG:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Int64";
			} else {
				return "newarr int64" + brackets;
			}

		case STRING:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.String";
			} else {
				return "newarr string" + brackets;
			}

		case BOOLEAN:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Boolean";
			} else {
				return "newarr bool" + brackets;
			}

		case BYTE:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Byte";
			} else {
				return "newarr int8" + brackets;
			}

		case CHAR:
			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Char";
			} else {
				return "newarr char" + brackets;
			}

		case SHORT:

			if (dimensions == lastDimension) {
				return "newarr [mscorlib]System.Int16";
			} else {
				return "newarr int16" + brackets;
			}

		default:
			if (dimensions == lastDimension) {
				return "newarr " + typeInString;
			} else {
				return "newarr " + typeInString + brackets;
			}
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
