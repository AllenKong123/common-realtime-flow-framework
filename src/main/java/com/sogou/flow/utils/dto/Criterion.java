package com.sogou.flow.utils.dto;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	A dto that makes the name and value as a bean for querying as conditions.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-9-1 下午5:32:03
 * @Criterion
 */
public class Criterion {

	private String variableName;
	
	private String variableValue;

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	
}
