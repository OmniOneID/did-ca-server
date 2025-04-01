package org.omnione.did.base.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties for the blockchain connection.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperty {
    private String filePath;
}
