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

package org.omnione.did.cas.v1.agent.service;

import lombok.RequiredArgsConstructor;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.db.repository.CertificateVcRepository;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.springframework.stereotype.Service;

/**
 * The CertificateVcQueryService class provides methods for saving and retrieving certificate VCs.
 * It is designed to facilitate the storage and retrieval of certificate VCs, ensuring that the data is accurate and up-to-date.
 */
@RequiredArgsConstructor
@Service
public class CertificateVcQueryService {
    private final CertificateVcRepository certificateVcRepository;

    /**
     * Save the given certificate VC to the database.
     *
     * @param vc the certificate VC to save
     * @return the saved certificate VC
     */
    public CertificateVc save(CertificateVc vc) {
        return certificateVcRepository.save(vc);
    }

    /**
     * Retrieve the most recent certificate VC from the database.
     *
     * @return the most recent certificate VC
     * @throws OpenDidException if the certificate VC cannot be retrieved
     */
    public CertificateVc findCertificateVc() {
          return certificateVcRepository.findFirstByOrderByCreatedAtDesc()
                  .orElseThrow(() -> new OpenDidException(ErrorCode.CERTIFICATE_DATA_NOT_FOUND));
    }

}
