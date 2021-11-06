package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.model.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
}
