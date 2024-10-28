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

package org.omnione.did.base.datamodel.enums;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class WalletTokenPurposeAdapter extends TypeAdapter<WalletTokenPurpose> {
    @Override
    public void write(JsonWriter out, WalletTokenPurpose value) throws IOException {
        if (value != null) {
            out.value(value.toValue());
        } else {
            out.nullValue();
        }
    }

    @Override
    public WalletTokenPurpose read(JsonReader in) throws IOException {
        int purposeValue = in.nextInt();
        for (WalletTokenPurpose purpose : WalletTokenPurpose.values()) {
            if (purpose.toValue().equals(purposeValue)) {
                return purpose;
            }
        }
        throw new IOException("Unknown purpose value: " + purposeValue);
    }
}
