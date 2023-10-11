package com.vacinas.ap3.repository;

import com.vacinas.ap3.entity.RegistroDeVacinacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroDeVacinacaoRepository extends MongoRepository<RegistroDeVacinacao,String> {

}
