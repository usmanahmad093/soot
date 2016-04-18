package soot.toCIL;

import soot.SootField;
import soot.SootMethod;

public class CILFieldBuilder {
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	private static final String PROTECTED = "family";
	private static final String FINAL = "initonly";
	private static final String STATIC = "static";

	
	public static String buildCILField(SootField sootField) {
		String cilField = ".field ";

		if (sootField.isPrivate()) {
			cilField += PRIVATE;

			if (sootField.isStatic()) {
				cilField += " " + STATIC;

				if (sootField.isFinal()) {
					cilField += " " + FINAL;
				}
			}

		} else if (sootField.isProtected()) {
			cilField += PROTECTED;
			if (sootField.isStatic()) {
				cilField += " " + STATIC;

				if (sootField.isFinal()) {
					cilField += " " + FINAL;
				}
			}

		} else if (sootField.isPublic()) {
			cilField += PUBLIC;
			if (sootField.isStatic()) {
				cilField += " " + STATIC;

				if (sootField.isFinal()) {
					cilField += " " + FINAL;
				}
			}

		}
		
		String fieldName = sootField.getName();
		String type = Converter.getInstance().getTypeInString(sootField.getType());
		cilField += " " + type + " " + fieldName;

		return cilField;
	}

}
