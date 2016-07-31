package soot.toCIL.structures;

import java.util.ArrayList;

public class CILClass implements Body{
	private ArrayList<Member> allMembers;
	private ArrayList<Method> allMethods;
	private String startBody;

	public CILClass(ArrayList<Member> allMembers) {
		this.allMembers = allMembers;
		allMethods = new ArrayList<>();
		
		startBody = "";
		
		
	}
	
	public void addMethod(Method m) {
		allMethods.add(m);
	}
	
	public ArrayList<Method> getMethods() {
		return allMethods;
	}
	
	
	public ArrayList<Member> getMembers() {
		return allMembers;
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
