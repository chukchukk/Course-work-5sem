package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.model.User;
import com.ponomarev.coursework.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
	 UserInfo findByUser(User user);
	 Optional<UserInfo> findByEmail(String email);
}
