package com.solution.calc.domain.log.entity;

import com.solution.calc.audit.Auditable;
import com.solution.calc.constant.LogType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long logId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private LogType logType;

    @Column(nullable = false)
    @Setter
    private String message;

    @Column(nullable = false)
    @Setter
    private String activeUsername;

    @Column(nullable = true)
    @Setter
    private String targetUsername;

}