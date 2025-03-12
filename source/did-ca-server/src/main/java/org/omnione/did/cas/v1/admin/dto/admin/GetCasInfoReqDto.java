/*
 * Copyright 2025 OmniOne.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.omnione.did.cas.v1.admin.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.omnione.did.base.db.constant.CasStatus;
import org.omnione.did.base.db.domain.Cas;
import org.omnione.did.data.model.did.DidDocument;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GetCasInfoReqDto {
    private Long id;
    private String did;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CasStatus status;
    private String serverUrl;
    private String certificateUrl;
    private DidDocument didDocument;
    private String createdAt;
    private String updatedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private static String formatInstant(Instant instant) {
        return Optional.ofNullable(instant)
                .map(FORMATTER::format)
                .orElse(null);
    }

    public static GetCasInfoReqDto fromEntity(Cas cas) {
        return Optional.ofNullable(cas)
                .map(t -> GetCasInfoReqDto.builder()
                        .id(t.getId())
                        .did(t.getDid())
                        .name(t.getName())
                        .status(t.getStatus())
                        .serverUrl(t.getServerUrl())
                        .certificateUrl(t.getCertificateUrl())
                        .createdAt(formatInstant(t.getCreatedAt()))
                        .updatedAt(formatInstant(t.getUpdatedAt()))
                        .build())
                .orElse(null);
    }

    public static GetCasInfoReqDto fromEntity(Cas cas, DidDocument didDocument) {
        return Optional.ofNullable(cas)
                .map(t -> GetCasInfoReqDto.builder()
                        .id(t.getId())
                        .did(t.getDid())
                        .name(t.getName())
                        .status(t.getStatus())
                        .serverUrl(t.getServerUrl())
                        .certificateUrl(t.getCertificateUrl())
                        .didDocument(didDocument)
                        .createdAt(formatInstant(t.getCreatedAt()))
                        .updatedAt(formatInstant(t.getUpdatedAt()))
                        .build())
                .orElse(null);
    }
}
