package soot.toCIL;

import soot.SootClass;
import soot.SootField;

public class CILClassBuilder {
	private static final String PRIVATE = "private";
	private static final String PUBLIC = "public";
	private static final String PROTECTED = "family";
	private static final String FINAL = "initonly";
	private static final String STATIC = "static";
	private static final String OBJECT_CLASS = "[mscorlib]System.Object";

	public static String buildCILClass(SootClass sootClass) {

		String cilClass = ".class ";

		if (sootClass.isInnerClass()) {
			if (sootClass.isPrivate()) {
				cilClass += PRIVATE;

				if (sootClass.isStatic()) {
					cilClass += " " + STATIC;

					if (sootClass.isFinal()) {
						cilClass += " " + FINAL;
					}
				}

			} else if (sootClass.isProtected()) {
				cilClass += PROTECTED;
				if (sootClass.isStatic()) {
					cilClass += " " + STATIC;

					if (sootClass.isFinal()) {
						cilClass += " " + FINAL;
					}
				}

			} else if (sootClass.isPublic()) {
				cilClass += PUBLIC;
				if (sootClass.isStatic()) {
					cilClass += " " + STATIC;

					if (sootClass.isFinal()) {
						cilClass += " " + FINAL;
					}
				}

			}

		} else {
			if (sootClass.isPublic()) {
				cilClass += " " + PUBLIC;
			}
		}

		String className = sootClass.getName();
		String superClass;
		if (sootClass.hasSuperclass())
			superClass = (sootClass.getSuperclass().getName().equals(Object.class.getName()) ? OBJECT_CLASS
					: sootClass.getSuperclass().getName());
		else
			superClass = OBJECT_CLASS;

		StringBuilder sb = new StringBuilder();
		sb.append(cilClass);
		sb.append(" auto ansi beforefieldinit");
		sb.append(" ");
		sb.append(className);
		sb.append(" extends");
		sb.append(" ");
		sb.append(superClass);
		sb.append(" ");
		sb.append("{");

		return sb.toString();
	}

}
