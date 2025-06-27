package org.omnione.did.cas.v1.admin.dto.cas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.omnione.did.cas.v1.admin.constant.EntityStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestEntityStatusResDto {
    private EntityStatus status;
}
