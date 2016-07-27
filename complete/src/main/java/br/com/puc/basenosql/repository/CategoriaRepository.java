package br.com.puc.basenosql.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import br.com.puc.basenosql.entity.Categoria;

public interface CategoriaRepository extends GraphRepository<Categoria> { }
