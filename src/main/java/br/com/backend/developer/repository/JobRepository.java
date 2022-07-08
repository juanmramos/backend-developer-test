package br.com.backend.developer.repository;

import br.com.backend.developer.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<JobEntity,Long> {

    List<JobEntity> findByReports(Integer reports);
}
