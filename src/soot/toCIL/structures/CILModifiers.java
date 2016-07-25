package soot.toCIL.structures;

/**
 * contains CIL Modifiers
 * @author Usman
 *
 */
public class CILModifiers {
	
	/*
	 * Modifiers: Format: Jimplename = CILName as a Value
	 */
	
	public static final String INTERFACE = "interface";
	public static final String ABSTRACT = "abstract";
	public static final String PRIVATE = "private";
	public static final String PUBLIC = "public";
	public static final String PROTECTED = "family";
	public static final String MEMBER_FINAL = "initonly";
	public static final String METHOD_FINAL = "final";
	public static final String CLASS_FINAL = "sealed";
	public static final String STATIC = "static";
	public static final String INSTANCE = "instance";
	public static final String TRANSIENT = "notserialized";
	public static final String VOLATILE = "modreq([mscorlib]System.Runtime.CompilerServices.IsVolatile)";
	public static final String SYNCHRONIZED = "TODO!";
	public static final String NATIVE = "TODO!";
	public static final String STRICTFP = "TODO!";
	
	public static final int C_ACCESS = 0;
	public static final int C_ABSTRACT = 1;
	public static final int C_STATICORINSTANCE = 2;
	public static final int C_VOLATILE = 3;
	public static final int C_SYNCHRONIZED = 4;
	public static final int C_TRANSIENT = 5;
	public static final int C_NATIVE = 6;
	public static final int C_STRICTFP = 7;
	public static final int C_FINAL = 8;
	public static final int C_INTERFACE = 9;
	

}
