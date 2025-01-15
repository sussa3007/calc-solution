package com.solution.calc.domain.rtpay.entity;

import com.solution.calc.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "rtpay_key")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RtpayKey extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rtpayKeyId;

    @Column(nullable = false)
    private String officeUsername;

    @Column(nullable = false, unique = true)
    private String rtpayKey;
}
