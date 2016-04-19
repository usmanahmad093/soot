package soot.toCIL;

import soot.RefType;

public class Converter {
	private static Converter instance = new Converter();
	
	
	private static final String STRING = "string";
	private static final String INT32 = "int32";
	private static final String INT64 = "int64";
	private static final String FLOAT32 = "float32";
	private static final String FLOAT64 = "float64";
	private static final String BOOL = "bool";
	private static final String CHAR = "char";
	private static final String UINT8 = "uint8";
	private static final String INT8 = "int8";
	private static final String INT16 = "int16";
	private static final String VOID = "void";
	
	

	private Converter() {
	}

	public static Converter getInstance() {
		return instance;
	}

	public String getTypeInString(soot.Type askedType) {

		if (askedType instanceof soot.RefType) {

			RefType refType = (soot.RefType) askedType;
			// TODO: refType.getClassName().equals("-");
			if (refType.getClassName().equals(String.class.getName())) {
				return STRING;

			} else if (refType.getClassName().equals(Integer.class.getName())) {
				return INT32;

			} else if (refType.getClassName().equals(Boolean.class.getName())) {
				return BOOL;
			} else if (refType.getClassName().equals(Character.class.getName())) {
				return CHAR;
			} else if (refType.getClassName().equals(Double.class.getName())) {
				return FLOAT64;
			} else if (refType.getClassName().equals(Float.class.getName())) {
				return FLOAT32;
			} else if (refType.getClassName().equals(Long.class.getName())) {
				return INT64;
			} else if (refType.getClassName().equals(Byte.class.getName())) {
				return INT8;
			} 

		} else if (askedType instanceof soot.IntType) {
			return INT32;
		} else if (askedType instanceof soot.BooleanType) {
			return BOOL;
		} else if (askedType instanceof soot.CharType) {
			return CHAR;
		} else if (askedType instanceof soot.DoubleType) {
			return FLOAT64;
		} else if (askedType instanceof soot.FloatType) {
			return FLOAT32;
		} else if (askedType instanceof soot.LongType) {
			return INT64;
		} else if (askedType instanceof soot.VoidType) {
			return VOID;
		} else if (askedType instanceof soot.ByteType) {
			return INT8;
		} else if(askedType instanceof soot.ShortType) {
			return INT16;
		} else if (askedType instanceof soot.ArrayType) {
			// TODO: arrayTypes überprüfen
			return "string[]";
		}

		return "class " + askedType.toString();
	}
	
}
