package soot.toCIL;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;

import soot.ArrayType;
import soot.RefType;
import soot.toCIL.structures.Type;

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
	private static final String INDEXOUTOFRANGEDEXCEPTION = "IndexOutOfRangeException";
	private static final String NULLREFERENCEEXCEPTION = "NullReferenceException";
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";
	
	
	private boolean isClassType = false;

	private Converter() {
	}

	public static Converter getInstance() {
		return instance;
	}
	

	public String getTypeInString(soot.Type askedType) {
		String finalType = null;
		final String BRACKETS = "[]";

		if (askedType instanceof soot.ArrayType) {
			ArrayType arrayType = (soot.ArrayType) askedType;
			finalType = Converter.getInstance().ConvertWrapperOrPrimitiveTypeInCIL(arrayType.getElementType());
			finalType += BRACKETS;
		} else {
			finalType = Converter.getInstance().ConvertWrapperOrPrimitiveTypeInCIL(askedType);
		}

		return finalType;

	}

	private String ConvertWrapperOrPrimitiveTypeInCIL(soot.Type askedType) {
		isClassType = false; 
		String cilException = "";
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
			}
			
			else if ((cilException = isExceptionType(refType)) != null) {
				return "class " + cilException;
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
	
	private String isExceptionType(RefType refType) {
		final String System = "[mscorlib]System.";
		
	    if (refType.getClassName().equals(IndexOutOfBoundsException.class.getName())) {
			return System + INDEXOUTOFRANGEDEXCEPTION;
		}  else if (refType.getClassName().equals(NullPointerException.class.getName())) {
			return System + NULLREFERENCEEXCEPTION;
		} 
		
		//...
		
		
		return null;
	}

	public boolean isClassType() {
		return isClassType;
	}
	
	public String getClassType(soot.Type askedType) {
		
		return askedType.toString();
	}
	
	public Type getTypeInEnum(soot.Type askedType) {
		
		
		if (askedType instanceof soot.ArrayType) {
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
