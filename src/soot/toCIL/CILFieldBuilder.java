package soot.toCIL;

import soot.SootField;
import soot.SootMethod;
import soot.toCIL.structures.CILModifiers;

public class CILFieldBuilder {
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	private static final String PROTECTED = "family";
	private static final String FINAL = "initonly";
	private static final String STATIC = "static";

	
	public static String buildCILField(SootField sootField) {
		StringBuilder sb =  new StringBuilder();
		String[] modifiers = CILModifierBuilder.ModifierBuilder(sootField.getModifiers());
		String fieldName = sootField.getName();
		String type = Converter.getInstance().getTypeInString(sootField.getType());
		
		sb.append(".field ");
		sb.append(modifiers[CILModifiers.C_ACCESS]);
		sb.append(" ");
		sb.append(modifiers[CILModifiers.C_STATICORINSTANCE]);
		sb.append(" ");
		sb.append(modifiers[CILModifiers.C_FINAL]);
		sb.append(" ");
		sb.append(type);
		sb.append(" ");
		sb.append(modifiers[CILModifiers.C_VOLATILE]);
		sb.append(" ");
		sb.append(fieldName);
		
		return sb.toString();
	}

}
