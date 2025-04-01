package org.omnione.did.cas.v1.agent.service;

import lombok.extern.slf4j.Slf4j;
import org.omnione.did.data.model.did.DidDocument;
import org.omnione.did.data.model.did.InvokedDidDoc;
import org.omnione.did.data.model.enums.did.DidDocStatus;
import org.omnione.did.data.model.enums.vc.RoleType;
import org.omnione.did.data.model.vc.VcMeta;
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
