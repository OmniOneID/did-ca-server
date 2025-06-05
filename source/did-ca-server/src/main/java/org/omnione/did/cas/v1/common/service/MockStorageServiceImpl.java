package org.omnione.did.cas.v1.common.service;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.cas.v1.common.service.StorageService;
import org.omnione.did.data.model.did.DidDocument;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("sample")
public class MockStorageServiceImpl implements StorageService {

    @Override
    public DidDocument findDidDoc(String didKeyUrl) {
        return null;
    }
}
