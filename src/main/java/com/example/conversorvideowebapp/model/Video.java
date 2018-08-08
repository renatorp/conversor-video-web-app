package com.example.conversorvideowebapp.model;

public class Video {

	private String id;
	private String nome;
	private String formato;

	public Video(String id, String nome, String formato) {
		super();
		this.id = id;
		this.nome = nome;
		this.formato = formato;
	}

	public Video() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", nome=" + nome + ", formato=" + formato + "]";
	}

}
