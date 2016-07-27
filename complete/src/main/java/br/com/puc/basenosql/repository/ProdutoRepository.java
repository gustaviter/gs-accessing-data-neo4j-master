package br.com.puc.basenosql.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import br.com.puc.basenosql.entity.Produto;

public interface ProdutoRepository extends GraphRepository<Produto> {
	@Query("START produtos=node(*) MATCH produtos<--pessoa WHERE has(pessoa.UF) AND pessoa.UF={uf} return produtos")
	Iterable<Produto> listarProdutosUF(String uf); 
}
