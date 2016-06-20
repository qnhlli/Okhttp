package com.example.okhttpdemo;

import java.io.Serializable;

public class ResponseMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int resultCode;		//�Ѿ�����õĽӿڷ��ش���
	private String resultMessage;	//�ӿڴ�������Ľ���
	private String dataContent;		//���ؿͻ��˵�ҵ������

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getDataContent() {
		return dataContent;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

}
