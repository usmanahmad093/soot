package soot.toCIL.structures;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class holds the structure of an CIL Class
 * 
 * @author ahmad
 *
 */
public class Class implements Body {
	private String modifier;
	private String className;
	private String baseClass;
	private ArrayList<Member> allMembers;
	private ArrayList<Method> allMethods;
	private String startBody;

	public Class(String modifier, String className, String baseClass, ArrayList<Member> allMembers) {
		this.modifier = modifier;
		this.className = className;
		this.baseClass = baseClass;
		this.allMembers = allMembers;
		allMethods = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(".cass");
		sb.append(" ");
		sb.append(modifier);
		sb.append(" ");
		sb.append(" auto ansi beforefieldinit");
		sb.append(" ");
		sb.append(className);
		sb.append(" extends");
		sb.append(" ");
		sb.append(baseClass);
		sb.append(" ");
		sb.append("{");
		
		startBody = sb.toString();
		
		
	}
	
	public void addMethod(Method m) {
		allMethods.add(m);
	}
	
	public ArrayList<Method> getMethods() {
		return allMethods;
	}
	
	public void setMembers(ArrayList<Member> allMembers) {
		this.allMembers = allMembers;
	}
	
	public ArrayList<Member> getMembers() {
		return allMembers;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setBaseClassName(String baseClass) {
		this.baseClass = baseClass;
	}
	
	public String getBaseClassName() {
		return baseClass;
	}

	public void setStartBody(String startBody) {
		this.startBody = startBody;
	}

	@Override
	public String getStartBody() {
		
		return startBody;
	}


	@Override
	public String getEndBody() {
		// TODO Auto-generated method stub
		return "}";
	}

}
