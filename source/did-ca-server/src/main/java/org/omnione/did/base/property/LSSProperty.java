package org.omnione.did.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Property class for setup for lss.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "lss")
public class LSSProperty {
    private String url;
}
