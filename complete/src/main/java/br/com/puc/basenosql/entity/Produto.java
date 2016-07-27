package br.com.puc.basenosql.entity;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Produto extends AbstractEntity {
	
	private String nome;
	
	@RelatedTo(type="PERTENCE_A_UMA")
	private Categoria categoria;
	
	@RelatedTo(type="COMPROU_UM", direction=Direction.INCOMING)
	@Fetch
	private Set<Produto> compradores;
	
	@RelatedTo(type="VENDEU_UM", direction=Direction.INCOMING)
	@Fetch
	private Set<Produto> vendedores;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Set<Produto> getCompradores() {
		return compradores;
	}
	public void setCompradores(Set<Produto> compradores) {
		this.compradores = compradores;
	}
	public Set<Produto> getVendedores() {
		return vendedores;
	}
	public void setVendedores(Set<Produto> vendedores) {
		this.vendedores = vendedores;
	}
}
