package com.example.conversorvideowebapp.vo;

import java.util.List;

public class EncoderResponseBody {

	private String id;
	private List<EncoderResponseOutput> outputs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<EncoderResponseOutput> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<EncoderResponseOutput> outputs) {
		this.outputs = outputs;
	}

}
