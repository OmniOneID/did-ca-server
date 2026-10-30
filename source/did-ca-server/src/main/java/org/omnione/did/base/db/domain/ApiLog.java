package org.omnione.did.base.db.domain;


import org.omnione.did.base.constants.ActionType;
import org.omnione.did.base.constants.ApiType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "api_log")
public class ApiLog extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method", nullable = false, length = 10)
    private String method;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "api_name")
    private String apiName;

    @Column(name = "api_description")
    private String apiDescription;

    @Column(name = "requester_id")
    private String requesterId;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "message")
    private String message;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "api_type")
    private ApiType apiType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "target_type")
    private String targetType;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @Column(name = "user_agent")
    private String userAgent;
}
