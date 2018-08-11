package com.example.conversorvideowebapp.vo;

import java.util.List;

public class EncoderInputRequestBody {

	private String input;
	private List<EncoderOutputRequest> output;
	private String notifications;

	public EncoderInputRequestBody() {
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public List<EncoderOutputRequest> getOutput() {
		return output;
	}

	public void setOutput(List<EncoderOutputRequest> output) {
		this.output = output;
	}

	public String getNotifications() {
		return notifications;
	}

	public void setNotifications(String notifications) {
		this.notifications = notifications;
	}
}
