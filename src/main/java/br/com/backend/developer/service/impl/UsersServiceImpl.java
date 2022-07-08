package br.com.backend.developer.service.impl;

import br.com.backend.developer.dto.request.JobRequest;
import br.com.backend.developer.dto.request.UsersRequest;
import br.com.backend.developer.dto.response.JobResponse;
import br.com.backend.developer.dto.response.UsersResponse;
import br.com.backend.developer.entity.JobEntity;
import br.com.backend.developer.repository.JobRepository;
import br.com.backend.developer.repository.UsersRepository;
import br.com.backend.developer.service.UsersService;
import br.com.backend.developer.dto.SearchCriteria;
import br.com.backend.developer.entity.UsersEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService, Specification<UsersEntity> {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ModelMapper modelMapper;


    private SearchCriteria criteria;

    @Override
    public UsersResponse register(UsersRequest usersRequest) {

       return toUsersDTO(save(toUsersEntityModel(usersRequest)));
    }

    @Override
    public UsersResponse update(UsersRequest usersRequest) throws Exception {

        if (Objects.isNull(usersRequest)) {
            throw new Exception("Erro ao localizar o usuário!");
        }
        return toUsersDTO(save(toUsersEntityModel(usersRequest)));
    }

    @Override
    public void delete(Long id) throws Exception {
        UsersEntity usersEntity = usersRepository.findById(id).get();
        List<JobEntity> jobEntities = jobRepository.findByReports(usersEntity.getJobEntity().getReports());
        if (CollectionUtils.isEmpty(jobEntities)) {
            usersRepository.delete(usersEntity);
        } else {
            throw new Exception("Não pode ser excluído até que outro gerente seja definido para o usuário.");
        }
    }

    @Override
    public UsersResponse view(Long id, UsersRequest usersDTO) throws Exception {
        UsersEntity usersEntity = usersRepository.findById(id).get();
        UsersEntity usersEntity1 = toUsersEntityModel(usersDTO);
        usersEntity1.setId(id);
        usersEntity1.getJobEntity().setId(usersEntity.getJobEntity().getId());
        if (usersEntity.getId() == usersEntity1.getId()) {
            return toUsersDTO(save(usersEntity1));
        }
        return toUsersDTO(usersEntity);
    }

    @Override
    public List<UsersResponse> viewList() {
        return usersRepository.findAll()
                .stream()
                .map(this::toUsersDTO)
                .collect(Collectors.toList());
    }

    public UsersEntity save(UsersEntity usersEntity) {
        return usersRepository.saveAndFlush(usersEntity);
    }

    public UsersEntity toUsersEntityModel(UsersRequest usersRequest) {
        UsersEntity usersEntity = modelMapper.map(usersRequest, UsersEntity.class);
        JobEntity jobEntity = modelMapper.map(usersRequest.getJobRequest(), JobEntity.class);
        usersEntity.setJobEntity(jobEntity);
        return usersEntity;
    }

    public UsersResponse toUsersDTO(UsersEntity usersEntity) {
        UsersResponse usersResponse = modelMapper.map(usersEntity, UsersResponse.class);
        JobResponse jobResponse = modelMapper.map(usersEntity.getJobEntity(), JobResponse.class);
        usersResponse.setJobResponse(jobResponse);
        return usersResponse;
    }

    public JobEntity toJobEntityModel(JobRequest jobRequest) {
        return modelMapper.map(jobRequest, JobEntity.class);
    }

    @Override
    public Predicate toPredicate(Root<UsersEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    @Override
    public Collection<UsersEntity> findByFilter(String nome,
                                                String email,
                                                Long id) {

       List<UsersEntity> usersEntities = usersRepository.findAll(new Specification<UsersEntity>() {
           @Override
           public Predicate toPredicate(Root<UsersEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
               List<Predicate> predicates = new ArrayList<>();
               if (nome != null) {
                   predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("nome"), nome)));
               }
               if (email != null) {
                   predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("email"), email)));
               }
               if (id != null) {
                   predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
               }
               return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[predicates.size()])));
           }
       });

        return usersEntities;
    }

    @Override
    public UsersResponse updatePartial(Long id, String nome, String email, Integer reports) throws Exception {
        UsersEntity usersEntity = usersRepository.findById(id).get();
        UsersEntity usersEntity1 = UsersEntity
                .builder()
                .id(id)
                .nome(nome == null ? usersEntity.getNome() : nome)
                .email(email == null ? usersEntity.getEmail() : email)
                .jobEntity(JobEntity
                        .builder()
                        .id(usersEntity.getJobEntity().getId())
                        .reports(reports == null ? usersEntity.getJobEntity().getReports() : reports)
                        .build())
                .build();
        return toUsersDTO(save(usersEntity1));
    }
}
