package com.ponomarev.coursework.repository;

import com.ponomarev.coursework.entity.PassportInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportInfoRepository extends JpaRepository<PassportInfo, Long> {
    Optional<PassportInfo> findAllByPassportSeriesAndPassportNumber(String passportSeries,
                                                                    String passportNumber);
}
