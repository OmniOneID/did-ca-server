package org.omnione.did.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Property class for setup for lls.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "lls")
public class LLSProperty {
    private String url;
}
