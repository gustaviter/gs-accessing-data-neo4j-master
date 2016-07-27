package br.com.puc.basenosql.entity;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Categoria extends AbstractEntity {
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Categoria() { }
	public Categoria(String descricao) {
		this.descricao = descricao;
	}
}
