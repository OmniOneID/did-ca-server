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

import org.omnione.did.wallet.enums.WalletEncryptType;
import org.omnione.did.wallet.exception.WalletException;
import org.omnione.did.wallet.key.WalletManagerInterface;
import org.omnione.did.wallet.key.data.CryptoKeyPairInfo;
import org.omnione.did.wallet.key.data.CryptoKeyPairInfo.KeyAlgorithmType;
import org.omnione.did.wallet.key.data.KeyElement;

import java.util.List;


/**
 * DummyWalletManager is a placeholder implementation of the WalletManagerInterface.
 *
 * <p>This class is intended to provide a default, non-functional implementation of the
 * WalletManagerInterface for cases where the actual wallet manager is not required,
 * such as when a specific Spring profile is active (e.g., "sample").</p>
 *
 * <p>The methods in this class do not perform any real operations and return either
 * default values or empty results. This class is primarily used to satisfy the
 * dependency injection requirements without executing any meaningful logic.</p>
 *
 * <p>Use this class only when an instance of WalletManagerInterface is needed but
 * no real wallet operations should be performed.</p>
 *
 */
public class DummyWalletManager implements WalletManagerInterface {
    @Override
    public void create(String s, char[] chars, WalletEncryptType walletEncryptType) throws WalletException {
        // No operation
    }

    @Override
    public void connect(String s, char[] chars) throws WalletException {
        // No operation
    }

    @Override
    public void changePassword(char[] chars, char[] chars1) throws WalletException {
        // No operation
    }

    @Override
    public boolean disConnect() {
        return false; // Always return false
    }

    @Override
    public boolean isConnect() {
        return false; // Always return false
    }

    @Override
    public void addKey(CryptoKeyPairInfo cryptoKeyPairInfo) throws WalletException {
        // No operation
    }

    @Override
    public void generateRandomKey(String s, KeyAlgorithmType keyAlgorithmType) throws WalletException {
        // No operation
    }

    @Override
    public boolean isExistKey(String s) throws WalletException {
        return false; // Always return false
    }

    @Override
    public String getPublicKey(String s) throws WalletException {
        return null; // Always return null
    }

    @Override
    public String getKeyAlgorithm(String s) throws WalletException {
        return null; // Always return null
    }

    @Override
    public KeyElement getKeyElement(String s) throws WalletException {
        return null; // Always return null
    }

    @Override
    public List<String> getKeyIdList() throws WalletException {
        return null; // Always return null
    }

    @Override
    public void removeKey(String s) throws WalletException {
        // No operation
    }

    @Override
    public void removeAllKeys() throws WalletException {
        // No operation
    }

    @Override
    public byte[] getSharedSecret(String s, String s1) throws WalletException {
        return new byte[0]; // Always return an empty byte array
    }

    @Override
    public byte[] generateCompactSignatureFromHash(String s, byte[] bytes) throws WalletException {
        return new byte[0]; // Always return an empty byte array
    }
}
