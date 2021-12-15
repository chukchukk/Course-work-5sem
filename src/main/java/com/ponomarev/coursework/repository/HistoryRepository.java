package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.entity.History;
import com.ponomarev.coursework.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
	Optional<List<History>> findAllByUserInfo(UserInfo userInfo);
	List<History> findAllByDateAndUserInfo(Date date, UserInfo userInfo);
}
