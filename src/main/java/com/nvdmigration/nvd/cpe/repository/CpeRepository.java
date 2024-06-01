package com.omreon.accountservice.nvd.cpe.repository;

import com.omreon.accountservice.nvd.cpe.document.Cpe;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface CpeRepository extends CouchbaseRepository<Cpe, String> {
}
