package br.com.backend.developer.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersRequest {
    private String nome;
    private String email;
    private JobRequest jobRequest;
}
