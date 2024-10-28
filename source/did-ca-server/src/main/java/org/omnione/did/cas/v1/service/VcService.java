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

package org.omnione.did.cas.v1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.db.domain.CertificateVc;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.data.model.vc.VerifiableCredential;
import org.springframework.stereotype.Service;

/**
 * The VcService class provides methods for requesting and processing verifiable credentials (VCs).
 * It is designed to facilitate the retrieval and validation of VCs from the CAS database,
 * ensuring that the data is accurate and up-to-date.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class VcService {
    private final CertificateVcQueryService certificateVcQueryService;
    /**
     * Request a verifiable credential (VC) from the CertificateVc database.
     * This method retrieves the latest VC from the database and returns it as a VerifiableCredential object.
     *
     * @return String containing the requested VC
     * @throws OpenDidException if the VC cannot be retrieved
     */
    public String requestCertificateVc() {
        try {
            CertificateVc certificateVc = certificateVcQueryService.findCertificateVc();
            VerifiableCredential verifiableCredential = new VerifiableCredential();
            verifiableCredential.fromJson(certificateVc.getVc());
            return verifiableCredential.toJson();
        } catch(OpenDidException e) {
            log.error("An OpenDidException occurred while sending requestCertificateVc request", e);
            throw e;
        } catch (Exception e) {
            log.error("An unknown error occurred while sending requestCertificateVc request", e);
            throw new OpenDidException(ErrorCode.FAILED_TO_REQUEST_CERTIFICATE_VC);
        }
    }

}
