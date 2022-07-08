package br.com.backend.developer.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
        private Long id;

        private String nome;

        private String email;

        private JobResponse jobResponse;
}
