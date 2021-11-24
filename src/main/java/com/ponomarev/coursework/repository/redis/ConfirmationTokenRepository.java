package com.ponomarev.coursework.repository.redis;

import com.ponomarev.coursework.model.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {

}
