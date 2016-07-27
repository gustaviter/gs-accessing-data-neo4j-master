package br.com.puc.basenosql.relationship;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import br.com.puc.basenosql.entity.AbstractEntity;
import br.com.puc.basenosql.entity.Pessoa;
import br.com.puc.basenosql.entity.Produto;

@RelationshipEntity(type="COMPROU_UM")
public class Compra extends AbstractEntity {
	@StartNode
	private Pessoa pessoa;
	@EndNode
	private Produto produto;

	private String descricao;
	
	public Compra(Pessoa pessoa, Produto produto, String descricao) {
		this.pessoa = pessoa;
		this.produto = produto;
		this.descricao = descricao;
	}
	
	public Compra() {}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
