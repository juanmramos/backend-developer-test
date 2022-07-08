package br.com.backend.developer.service;

import br.com.backend.developer.dto.request.UsersRequest;
import br.com.backend.developer.dto.response.UsersResponse;
import br.com.backend.developer.entity.UsersEntity;

import java.util.Collection;
import java.util.List;

public interface UsersService {

    UsersResponse register(UsersRequest usersRequest);

    UsersResponse update(UsersRequest usersDTO) throws Exception;

    void delete(Long id) throws Exception;

    UsersResponse view(Long id, UsersRequest usersRequest) throws Exception;

    List<UsersResponse> viewList();

    Collection<UsersEntity> findByFilter(String nome,
                                         String email,
                                         Long id);

    UsersResponse updatePartial(Long id, String nome,
                                String email,
                                Integer reports) throws Exception;
}
