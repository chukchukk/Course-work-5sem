package com.ponomarev.coursework.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import javax.persistence.*;
import java.io.Serializable;


@Data
@Builder
@RedisHash(value = "ConfirmationToken", timeToLive = 900)
public class ConfirmationToken implements Serializable {

	@Id
	private String id;

	private String confirmationToken;

}
