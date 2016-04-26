package soot.toCIL;

import net.sf.cglib.core.Local;
import soot.Modifier;
import soot.SootClass;
import soot.SootField;
import soot.Value;
import soot.tagkit.AbstractHost;
import soot.toCIL.structures.CILModifiers;

public class CILModifierBuilder {

	
	public static String[] ModifierBuilder(int flags) {
		final int max = 9;
		
		String[] modifiers = new String[max];
		
		for(int i = 0; i < max; i++) {
			modifiers[i] = "";
		}
		
		if((flags & Modifier.ABSTRACT) == Modifier.ABSTRACT) {
			modifiers[CILModifiers.C_ABSTRACT] = CILModifiers.ABSTRACT;
		} 
		
		if ((flags & Modifier.PRIVATE) == Modifier.PRIVATE) {
			modifiers[CILModifiers.C_ACCESS] = CILModifiers.PRIVATE;
		} 
		
		if ((flags & Modifier.PUBLIC) == Modifier.PUBLIC) {
			modifiers[CILModifiers.C_ACCESS] = CILModifiers.PUBLIC;
		} 
		
		if ((flags & Modifier.PROTECTED) == Modifier.PROTECTED) {
			modifiers[CILModifiers.C_ACCESS] = CILModifiers.PROTECTED;
		} 
		
		if ((flags & Modifier.STATIC) == Modifier.STATIC) {
			modifiers[CILModifiers.C_STATICORINSTANCE] = CILModifiers.STATIC;
		}  else {
			modifiers[CILModifiers.C_STATICORINSTANCE] = CILModifiers.INSTANCE;
		}
		
		if ((flags & Modifier.VOLATILE) == Modifier.VOLATILE) {
			modifiers[CILModifiers.C_VOLATILE] = CILModifiers.VOLATILE;
		} 
		
		if ((flags & Modifier.SYNCHRONIZED) == Modifier.SYNCHRONIZED) {
			modifiers[CILModifiers.C_SYNCHRONIZED] = CILModifiers.SYNCHRONIZED;
		} 
		
		if ((flags & Modifier.TRANSIENT) == Modifier.TRANSIENT) {
			modifiers[CILModifiers.C_TRANSIENT] = CILModifiers.TRANSIENT;
		} 
		
		if ((flags & Modifier.NATIVE) == Modifier.NATIVE) {
			modifiers[CILModifiers.C_NATIVE] = CILModifiers.NATIVE;
		} 
		
		if ((flags & Modifier.STRICTFP) == Modifier.STRICTFP) {
			modifiers[CILModifiers.C_STRICTFP] = CILModifiers.STRICTFP;
		}
		
		
		return modifiers;
	}
	
	public static boolean isVolatile(int flags) {
		
		return ((flags & Modifier.VOLATILE) == Modifier.VOLATILE)? true: false;
	}
}
