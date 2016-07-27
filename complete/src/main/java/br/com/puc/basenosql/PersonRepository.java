package br.com.puc.basenosql;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {

    Person findByName(String name);

    Iterable<Person> findByTeammatesName(String name);

}
