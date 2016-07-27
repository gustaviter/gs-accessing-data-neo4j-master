package br.com.puc.basenosql.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import br.com.puc.basenosql.relationship.Compra;
import br.com.puc.basenosql.relationship.Venda;

@NodeEntity
public class Pessoa extends AbstractEntity {
	
	private String nome;
	private String email;
	@Indexed(fieldName="uf")
	private String UF;
	
//	@RelatedTo(type="COMPROU_UM")
//	public @Fetch Set<Produto> produtosComprados;
	
//	@RelatedTo(type="VENDEU_UM")
//	public @Fetch Set<Produto> produtosVendidos;
	
	@RelatedToVia
	private Set<Compra> compras = new HashSet<Compra>();
	
	@RelatedToVia
	private Set<Venda> vendas = new HashSet<Venda>();
	
	public Compra compraUm(Produto produto, String descricao) {
		final Compra compra = new Compra(this, produto, descricao);
		getCompras().add(compra);
		return compra;
	}
	
	public Venda vendeUm(Produto produto, String descricao) {
		final Venda venda = new Venda(this, produto, descricao);
		getVendas().add(venda);
		return venda;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUF() {
		return UF;
	}
	public void setUF(String uF) {
		UF = uF;
	}
	/*public Set<Produto> getProdutosComprados() {
		return produtosComprados;
	}
	public void setProdutosComprados(Set<Produto> produtosComprados) {
		this.produtosComprados = produtosComprados;
	}
	public Set<Produto> getProdutosVendidos() {
		return produtosVendidos;
	}
	public void setProdutosVendidos(Set<Produto> produtosVendidos) {
		this.produtosVendidos = produtosVendidos;
	}*/

	public Set<Compra> getCompras() {
		return compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

	public Set<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(Set<Venda> vendas) {
		this.vendas = vendas;
	}
}
