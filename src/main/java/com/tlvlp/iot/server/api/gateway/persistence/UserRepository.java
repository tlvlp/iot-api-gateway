package com.tlvlp.iot.server.api.gateway.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserRepository extends MongoRepository<User, String>, QueryByExampleExecutor<User> {
}
