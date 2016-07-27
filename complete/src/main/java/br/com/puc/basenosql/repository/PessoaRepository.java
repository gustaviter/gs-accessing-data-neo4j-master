package br.com.puc.basenosql.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import br.com.puc.basenosql.entity.Pessoa;

public interface PessoaRepository extends GraphRepository<Pessoa> { }
