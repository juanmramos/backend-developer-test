package br.com.backend.developer.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobResponse {
    private Long id;
    private Integer  reports;
}
