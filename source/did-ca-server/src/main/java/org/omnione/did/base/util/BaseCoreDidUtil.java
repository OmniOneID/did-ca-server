/*
 * Copyright 2024-2025 OmniOne.
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

package org.omnione.did.base.util;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.omnione.did.core.data.rest.DidKeyInfo;
import org.omnione.did.core.data.rest.SignatureParams;
import org.omnione.did.core.exception.CoreException;
import org.omnione.did.core.manager.DidManager;
import org.omnione.did.crypto.enums.DigestType;
import org.omnione.did.crypto.enums.MultiBaseType;
import org.omnione.did.crypto.exception.CryptoException;
import org.omnione.did.crypto.util.DigestUtils;
import org.omnione.did.crypto.util.MultiBaseUtils;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.VerificationMethod;
import org.omnione.did.data.model.enums.did.AuthType;
import org.omnione.did.data.model.enums.did.DidKeyType;
import org.omnione.did.data.model.enums.did.ProofPurpose;
import org.omnione.did.wallet.exception.WalletException;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.omnione.did.wallet.key.data.KeyElement;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for core DID operations.
 * This class provides methods for parsing DID documents, getting verification methods, and verifying DID document key proofs.
 */
@Slf4j
public class BaseCoreDidUtil {

    /**
     * Parses a DID document from its JSON representation.
     *
     * @param didDocJson The JSON string representing the DID document.
     * @return The parsed DidManager object.
     */
    public static DidManager parseDidDoc(String didDocJson) {
        DidManager didManager = new DidManager();
        didManager.parse(didDocJson);

        return didManager;
    }

    /**
     * Parses a DID document.
     *
     * @param didDocument The DID document object.
     * @return The parsed DidManager object.
     */
    public static DidManager parseDidDoc(DidDocument didDocument) {
        DidManager didManager = new DidManager();
        didManager.parse(didDocument.toJson());

        return didManager;
    }

    /**
     * Retrieves a verification method from a DID document.
     *
     * @param didDocument The DID document object.
     * @param keyId The key ID of the verification method.
     * @return The verification method object.
     */
    public static VerificationMethod getVerificationMethod(DidDocument didDocument, String keyId) {
        DidManager didManager = parseDidDoc(didDocument);
       return didManager.getVerificationMethodByKeyId(keyId);
    }

    public static Map<String, List<ProofPurpose>> createDefaultProofPurposes() {
        Map<String, List<ProofPurpose>> purposes = new LinkedHashMap<>();
        purposes.put("auth", List.of(ProofPurpose.AUTHENTICATION));
        purposes.put("assert", List.of(ProofPurpose.ASSERTION_METHOD));
        purposes.put("keyagree", List.of(ProofPurpose.KEY_AGREEMENT));
        purposes.put("invoke", List.of(ProofPurpose.CAPABILITY_INVOCATION));
        return purposes;
    }

    public static List<DidKeyInfo> getDidKeyInfosFromWallet(WalletManagerInterface walletManager, String controller, Map<String, List<ProofPurpose>> purposes) {
        List<DidKeyInfo> didKeyInfos = new ArrayList<>();
        try {
            for (String keyId : walletManager.getKeyIdList()) {
                KeyElement keyElement = walletManager.getKeyElement(keyId);
                DidKeyInfo keyInfo = new DidKeyInfo();
                keyInfo.setKeyId(keyId);
                keyInfo.setAlgoType(setDidKeyType(keyElement.getAlgorithm()));
                keyInfo.setPublicKey(keyElement.getPublicKey());
                keyInfo.setController(controller);
                keyInfo.setAuthType(AuthType.Free);
                keyInfo.setKeyPurpose(purposes.getOrDefault(keyId, new ArrayList<>()));
                didKeyInfos.add(keyInfo);
            }
        } catch (WalletException e) {
            log.error("Failed to get key element: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.FAILED_TO_LOAD_KEY_ELEMENT);
        }
        return didKeyInfos;
    }

    public static String setDidKeyType(String algo) {
        switch (algo) {
            case "Rsa2048":
                return DidKeyType.RSA_VERIFICATION_KEY_2018.getRawValue();
            case "Secp256r1":
                return DidKeyType.SECP256R1_VERIFICATION_KEY_2018.getRawValue();
            case "Secp256k1":
                return DidKeyType.SECP256K1_VERIFICATION_KEY_2018.getRawValue();
            default:
                return null;
        }
    }

    public static DidDocument createDidDocument(DidManager didManager, String did, String controller, List<DidKeyInfo> didKeyInfos) {
        try {
            return didManager.createDocument(did, controller, didKeyInfos);
        } catch (CoreException e) {
            log.error("Failed to create DID document: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.DIDDOC_GENERATION_FAILED);
        }
    }

    public static List<String> getSigningKeyIds(Map<String, List<ProofPurpose>> purposes) {
        List<String> keyIds = new ArrayList<>();
        for (Map.Entry<String, List<ProofPurpose>> entry : purposes.entrySet()) {
            for (ProofPurpose purpose : entry.getValue()) {
                if (purpose != ProofPurpose.KEY_AGREEMENT) {
                    keyIds.add(entry.getKey());
                    break;
                }
            }
        }
        return keyIds;
    }

    public static DidDocument signAndAddProof(DidManager didManager, WalletManagerInterface walletManager, List<String> signingKeyIds) {
        try {
            List<SignatureParams> signatureParams = didManager.getOriginDataForSign(signingKeyIds);
            for (SignatureParams params : signatureParams) {
                byte[] digest = DigestUtils.getDigest(params.getOriginData().getBytes(StandardCharsets.UTF_8), DigestType.SHA256);
                byte[] signature = walletManager.generateCompactSignatureFromHash(params.getKeyId(), digest);
                params.setSignatureValue(MultiBaseUtils.encode(signature, MultiBaseType.base58btc));
            }
            didManager.addProof(signatureParams);
            return didManager.getDocument();
        } catch (CoreException | CryptoException | WalletException e) {
            log.error("Failed to sign DID Document: {}", e.getMessage());
            throw new OpenDidException(ErrorCode.SIGNATURE_GENERATION_FAILED);
        }
    }

    /**
     * Parses a DID document from its JSON representation and returns the DID.
     *
     * @param didDocJson The JSON string representing the DID document.
     * @return The parsed DID.
     */
    public static String parseDid(String didDocJson) {
        DidDocument didDocument = new DidDocument();
        didDocument.fromJson(didDocJson);

        return didDocument.getId();
    }
}

