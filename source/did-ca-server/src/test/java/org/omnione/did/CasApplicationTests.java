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

package org.omnione.did;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.omnione.did.base.constants.UrlConstant;
import org.omnione.did.base.datamodel.data.*;
import org.omnione.did.base.datamodel.enums.WalletTokenPurpose;
import org.omnione.did.base.db.domain.UserPii;
import org.omnione.did.base.db.repository.UserPiiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CasApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("sample")
class CasApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserPiiRepository userPiiRepository;

    @Test
    @DisplayName("Wallet Token 요청 테스트")
    void requestWalletToken() throws Exception {

        // 1. 요청 DTO 설정
        WalletTokenSeed walletTokenSeed = new WalletTokenSeed();
        walletTokenSeed.setUserId("testUser");
        walletTokenSeed.setValidUntil("2024-01-01T09:00:00Z");
        walletTokenSeed.setNonce("zNgLzfu8edopNmKhmuGMv32");
        walletTokenSeed.setPkgName("com.raonsecure.did");
        walletTokenSeed.setPurpose(WalletTokenPurpose.ISSUE_VC);

        // 2. 요청
        MvcResult result = mockMvc.perform(post(UrlConstant.Cas.V1 + UrlConstant.Cas.REQUEST_WALLET_TOKEN_DATA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletTokenSeed)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        // 3. 실제 응답 확인
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Actual Controller Response: " + responseBody);

        // 4. 응답 검증 (옵션)
        WalletTokenData actualResDto = objectMapper.readValue(responseBody, WalletTokenData.class);
        //assertEquals("Success", actualResDto.getResult());
        System.out.println("final  Response: " + actualResDto);
    }

    @Test
    @DisplayName("Attested App Info 요청 테스트")
    void requestAttestedAppInfo() throws Exception {
        // 1. 요청 DTO 설정
        ReqAttestedAppInfo reqAttestedAppInfo = new ReqAttestedAppInfo();
        reqAttestedAppInfo.setAppId("com.raonsecure.did");

        // 2. 요청
        MvcResult result = mockMvc.perform(post(UrlConstant.Cas.V1 + UrlConstant.Cas.REQUEST_ATTESTED_APPINFO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqAttestedAppInfo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        // 3. 실제 응답 확인
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Actual Controller Response: " + responseBody);

        // 4. 응답 검증 (옵션)
        WalletTokenData actualResDto = objectMapper.readValue(responseBody, WalletTokenData.class);
        //assertEquals("Success", actualResDto.getResult());
        System.out.println("final  Response: " + actualResDto);
    }

//    @Test
//    @DisplayName("Save User Info 통합 테스트")
//    void testSaveUserInfo() throws Exception {
//        // 1. 요청 DTO 설정
//        SaveUserInfoDto saveUserInfoDto = new SaveUserInfoDto();
//        saveUserInfoDto.setUserId("testUser123");
//        saveUserInfoDto.setPii("Some PII data");
//
//        // 2. 요청 실행
//        MvcResult result = mockMvc.perform(post(UrlConstant.Cas.V1 + UrlConstant.Cas.SAVE_USER_INFO)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(saveUserInfoDto)))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//
//        // 3. 데이터베이스 확인
//        UserPii savedUserPii = userPiiRepository.findByUserId("testUser123")
//                .orElseThrow(() -> new AssertionError("User PII not found"));
//
//        assertEquals("testUser123", savedUserPii.getUserId());
//        assertEquals("Some PII data", savedUserPii.getPii());
//
//        // 4. 응답 확인 (필요한 경우)
//        String responseBody = result.getResponse().getContentAsString();
//        System.out.println("Actual Controller Response: " + responseBody);
//    }

    @Test
    @DisplayName("Retrieve PII 테스트")
    void testRetrievePii() throws Exception {
        // 1. 요청 DTO 설정
        RetrievePiiReqDto reqDto = new RetrievePiiReqDto();
        reqDto.setUserId("testUser123");

        // 2. 컨트롤러 호출 및 응답 검증
        MvcResult result = mockMvc.perform(post(UrlConstant.Cas.V1 + UrlConstant.Cas.RETRIEVE_PII)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        // 3. 실제 응답 확인
        String responseBody = result.getResponse().getContentAsString();
        System.out.println("Actual Controller Response: " + responseBody);

        // 4. 응답 검증 (옵션)
        RetrievePiiResDto actualResDto = objectMapper.readValue(responseBody, RetrievePiiResDto.class);
        System.out.println("final  Response: " + actualResDto);
    }
}
