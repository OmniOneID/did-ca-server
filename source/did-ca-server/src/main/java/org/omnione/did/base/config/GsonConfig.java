/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.base.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.omnione.did.base.datamodel.enums.WalletTokenPurpose;
import org.omnione.did.base.datamodel.enums.WalletTokenPurposeAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The GsonConfig class provides a configuration for Gson.
 * This class registers custom type adapters for specific enums used in the application.
 */
@Configuration
public class GsonConfig implements WebMvcConfigurer {
    /**
     * Configures and returns a Gson instance with custom type adapters.
     * This method registers custom adapters for the WalletTokenPurpose enums,
     * allowing Gson to correctly serialize and deserialize these types.
     *
     * @return a configured Gson instance
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(WalletTokenPurpose.class, new WalletTokenPurposeAdapter())
                .create();
    }
}
