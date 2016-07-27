package br.com.puc.basenosql.entity;

import org.springframework.data.neo4j.annotation.GraphId;

public class AbstractEntity {
	@GraphId
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
