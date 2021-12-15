package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CardInfoRepository extends JpaRepository<CardInfo, Long> {
	 Optional<CardInfo> findCardInfoByCardNumber(String cardNumber);
}
