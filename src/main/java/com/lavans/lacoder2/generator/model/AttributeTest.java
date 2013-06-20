package com.lavans.lacoder2.generator.model;


public class AttributeTest {
	public static void main(String[] args){
		AttributeTest test = new AttributeTest();
		test.getConstName();
	}
	
	public void getConstName(){
		Attribute attribute = new Attribute();
		attribute.setName("fq52High");
		System.out.println(attribute.getConstName());

		attribute.setName("fq52high");
		System.out.println(attribute.getConstName());

		attribute.setName("fq_52high");
		System.out.println(attribute.getConstName());
	}
}
