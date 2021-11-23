package com.ponomarev.coursework.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@RedisHash("ConfirmationToken")
public class ConfirmationToken implements Serializable {

	@Id
	private String id;

	private String confirmationToken;

	private String userInfo;
}
