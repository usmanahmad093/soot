package soot.toCIL;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;

import soot.ArrayType;
import soot.RefType;
import soot.toCIL.structures.Type;

/**
 * Type Converter
 * @author Usman
 *
 */
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
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";
	private static final String INDEXOUTOFBOUNDSEXCEP = "[mscorlib]System.IndexOutOfRangeException";
	private static final String EXCEPTION = "[mscorlib]System.Exception";

	
	
	private boolean isClassType = false;

	private Converter() {
	}

	public static Converter getInstance() {
		return instance;
	}
	

	/**
	 * it considers an array checking 
	 * @param askedType is a Soot Type
	 * @return soot type in CIl
	 */
	public String getTypeInString(soot.Type askedType) {
		String finalType = null;
		String BRACKETS = "";
		
		
		while (askedType instanceof soot.ArrayType) {
			ArrayType arrayType = (soot.ArrayType) askedType;
			askedType = arrayType.getElementType();
			BRACKETS += "[]";
		} 
		
		
	    finalType = Converter.getInstance().ConvertWrapperOrPrimitiveTypeInCIL(askedType) + BRACKETS;

		return finalType;

	}

	/**
	 * Converts soot type into CIL 
	 * @param askedType is a Soot Type
	 * @return CIL Type in String
	 */
	private String ConvertWrapperOrPrimitiveTypeInCIL(soot.Type askedType) {
		isClassType = false; 

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
			} else if (refType.getClassName().equals(Object.class.getName())) {
				return OBJECT_CLASS;
			} else if (refType.getClassName().equals(IndexOutOfBoundsException.class.getName())) {
				return "class " + INDEXOUTOFBOUNDSEXCEP;
			} else if (refType.getClassName().equals(Throwable.class.getName())) {
				return "class " + EXCEPTION;
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
		} else if (askedType instanceof soot.ShortType) {
			return INT16;
		}

		isClassType = true;
		return "class " + askedType.toString();
	}
	
	/**
	 * checks if its a class type
	 * @return true: yes
	 *         false: no
	 */
	public boolean isClassType() {
		return isClassType;
	}
	
	/**
	 * fetches the Class Type from the object askedType 
	 * @param askedType is a Soot Type
	 */
	public String getClassType(soot.Type askedType) {
		
		return askedType.toString();
	}
	
	/**
	 * converts an askedType object into an Enum Type
	 * @param askedType
	 * @return enum
	 */
	public Type getTypeInEnum(soot.Type askedType) {
		
		
		while (askedType instanceof soot.ArrayType) {
			askedType = ((soot.ArrayType) askedType).getElementType();
		}
		
		if (askedType instanceof soot.RefType) {

			RefType refType = (soot.RefType) askedType;
			// TODO: refType.getClassName().equals("-");
			if (refType.getClassName().equals(String.class.getName())) {
				return Type.STRING;

			} else if (refType.getClassName().equals(Integer.class.getName())) {
				return Type.INT;

			} else if (refType.getClassName().equals(Boolean.class.getName())) {
				return Type.BOOLEAN;
			} else if (refType.getClassName().equals(Character.class.getName())) {
				return Type.CHAR;
			} else if (refType.getClassName().equals(Double.class.getName())) {
				return Type.DOUBLE;
			} else if (refType.getClassName().equals(Float.class.getName())) {
				return Type.FLOAT;
			} else if (refType.getClassName().equals(Long.class.getName())) {
				return Type.LONG;
			} else if (refType.getClassName().equals(Byte.class.getName())) {
				return Type.BYTE;
			}

		} else if (askedType instanceof soot.IntType) {
			return Type.INT;
		} else if (askedType instanceof soot.BooleanType) {
			return Type.BOOLEAN;
		} else if (askedType instanceof soot.CharType) {
			return Type.CHAR;
		} else if (askedType instanceof soot.DoubleType) {
			return Type.DOUBLE;
		} else if (askedType instanceof soot.FloatType) {
			return Type.FLOAT;
		} else if (askedType instanceof soot.LongType) {
			return Type.LONG;
		} else if (askedType instanceof soot.ByteType) {
			return Type.BYTE;
		} else if (askedType instanceof soot.ShortType) {
			return Type.SHORT;
		}

		isClassType = true;
		return Type.OTHER;
	}
	


}
