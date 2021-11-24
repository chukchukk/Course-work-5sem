package com.ponomarev.coursework.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;


@Data
@Builder
@RedisHash("ConfirmationToken")
public class ConfirmationToken implements Serializable {

	@Id
	private String id;

	private String confirmationToken;

}
