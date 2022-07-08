package br.com.backend.developer.controller;

import br.com.backend.developer.constants.ConstantsPath;
import br.com.backend.developer.dto.request.UsersRequest;
import br.com.backend.developer.dto.response.UsersResponse;
import br.com.backend.developer.entity.UsersEntity;
import br.com.backend.developer.repository.UsersRepository;
import br.com.backend.developer.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = ConstantsPath.BACKEND_DEVELOPER)
@Api(tags = {"users"})
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;


    @ApiOperation(value = "Realiza a busca do user pelo id.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok", response = UsersResponse.class),
        @ApiResponse(code = 422, message = "Erro de validação nos campos", response = String.class),
        @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @GetMapping(value = "/buscar/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws Exception {

        try {
            Optional<UsersEntity> optionalPlaneta = Optional.ofNullable(usersRepository.findById(id)
                .orElseThrow(RuntimeException::new));
            return new ResponseEntity<UsersEntity>(optionalPlaneta.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("ID não encontrado!", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Realiza uma listagem de todos users com filtros.")
    @ApiParam(name = "nome", required = false, type = "string")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Ok", responseContainer = "List", response = UsersResponse.class),
        @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @GetMapping(value = "/listar-users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findByListUsers(@RequestParam(value = "nome", required = false) String nome,
                                             @RequestParam(value = "email", required = false) String email,
                                             @RequestParam(value = "id", required = false) Long id) {

        Collection<UsersEntity> usersServiceByFilter = usersService.findByFilter(nome,
                email,
                id);
        if (!CollectionUtils.isEmpty(usersServiceByFilter)) {
            return new ResponseEntity<Collection<UsersEntity>>(usersServiceByFilter, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Nome não encontrado!", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Realiza uma listagem de todos users.")
    @ApiParam(name = "nome", required = false, type = "string")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Ok", responseContainer = "List", response = UsersResponse.class),
            @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @GetMapping(value = "/listar-users-base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> findByListUsersSemFiltro() {

        Collection<UsersEntity> usersEntities = usersRepository.findAll();

        if (!CollectionUtils.isEmpty(usersEntities)) {
            return new ResponseEntity<Collection<UsersEntity>>(usersEntities, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Nome não encontrado!", HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Realiza o cadastro de um user.")
    @ApiResponses({ @ApiResponse(code = 201, message = "Ok", response = UsersResponse.class),
        @ApiResponse(code = 404, message = "Imóvel não encontrado", response = String.class),
        @ApiResponse(code = 422, message = "Erro de validação nos campos", response = String.class),
        @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @PostMapping(value = "/cadastrar-user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createUser(@RequestBody UsersRequest usersRequest) throws Exception {

        UsersResponse usersResponse = usersService.register(usersRequest);

        if (usersResponse != null) {
            return new ResponseEntity<>(usersResponse, HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Problemas ao cadastrar user!", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Remove um user.")
    @ApiResponses({ @ApiResponse(code = 200, message = "Ok", response = String.class),
        @ApiResponse(code = 404, message = "Imóvel não encontrado", response = String.class),
        @ApiResponse(code = 422, message = "Erro de validação nos campos", response = String.class),
        @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @DeleteMapping(value = "/remover-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws Exception {
        usersService.delete(id);
        return new ResponseEntity<>("User removido com sucesso!", HttpStatus.OK);
    }

    @ApiOperation(value = "Realiza o atualização de um User.")
    @ApiResponses({ @ApiResponse(code = 200, message = "Ok", response = UsersResponse.class),
            @ApiResponse(code = 404, message = "Imóvel não encontrado", response = String.class),
            @ApiResponse(code = 422, message = "Erro de validação nos campos", response = String.class),
            @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @PutMapping(value = "/atualizar-user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UsersRequest usersRequest) throws Exception {

        UsersResponse usersResponse = usersService.view(id, usersRequest);
        if (usersResponse != null) {
            return new ResponseEntity<>(usersResponse, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Problemas ao cadastrar user!", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Realiza o atualização de um User.")
    @ApiResponses({ @ApiResponse(code = 200, message = "Ok", response = UsersResponse.class),
            @ApiResponse(code = 404, message = "Imóvel não encontrado", response = String.class),
            @ApiResponse(code = 422, message = "Erro de validação nos campos", response = String.class),
            @ApiResponse(code = 500, message = "Erro interno do servidor", response = String.class), })
    @PatchMapping(value = "/atualizar-user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updateUserPartial(@PathVariable("id") Long id, @RequestBody UsersRequest usersRequest) throws Exception {

        UsersResponse usersResponse = usersService.updatePartial(id,
                usersRequest.getNome(),
                usersRequest.getEmail(),
                Objects.nonNull(usersRequest.getJobRequest()) ? usersRequest.getJobRequest().getReports() : null);
        if (usersResponse != null) {
            return new ResponseEntity<>(usersResponse, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Problemas ao cadastrar user!", HttpStatus.BAD_REQUEST);
    }

}
