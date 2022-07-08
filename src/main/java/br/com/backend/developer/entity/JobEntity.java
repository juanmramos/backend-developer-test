package br.com.backend.developer.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_db", schema = "backend_developer_test")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_job")
    private Long id;

    @Column(name = "reports")
    private Integer reports;

}
