package br.com.backend.developer.repository;

import br.com.backend.developer.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity,Long>, JpaSpecificationExecutor<UsersEntity> {

    List<UsersEntity> findByNome(String nome);

}
