package org.omnione.did.cas.v1.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.omnione.did.base.exception.ErrorCode;
import org.omnione.did.base.exception.OpenDidException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonParseService {

    public Map<String, Object> parseCertificateVcToMap(String certificateJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(certificateJson, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse Certificate VC JSON (invalid format): {}", certificateJson, e);
            throw new OpenDidException(ErrorCode.INVALID_CERTIFICATE_VC_JSON_FORMAT);
        } catch (Exception e) {
            log.error("Unexpected error while parsing Certificate VC JSON", e);
            throw new OpenDidException(ErrorCode.INVALID_CERTIFICATE_VC_JSON_FORMAT);
        }
    }

    public Map<String, Object> parseDidDocToMap(String didDocJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(didDocJson, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse DID Document JSON (invalid format): {}", didDocJson, e);
            throw new OpenDidException(ErrorCode.INVALID_DID_DOCUMENT);
        } catch (Exception e) {
            log.error("Unexpected error while parsing DID DOcument JSON", e);
            throw new OpenDidException(ErrorCode.INVALID_DID_DOCUMENT);
        }
    }
}
