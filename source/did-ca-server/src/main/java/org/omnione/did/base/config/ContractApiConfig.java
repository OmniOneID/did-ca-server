package org.omnione.did.base.config;

import org.omnione.did.ContractApi;
import org.omnione.did.ContractFactory;
import org.omnione.did.base.property.BlockchainProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!sample & !lss")
public class ContractApiConfig {
    @Bean
    public ContractApi contractApi(BlockchainProperty blockchainProperty) {
        return ContractFactory.EVM.create(blockchainProperty.getFilePath());
    }
}
