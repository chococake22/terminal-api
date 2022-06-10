package project.terminalv2.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.terminalv2.domain.MyTime;

import java.util.Optional;

@Repository
public interface MyTimeRepository extends JpaRepository<MyTime, Long> {

    Page<MyTime> findAllByUserUserNo(Long userNo, Pageable pageable);

    Optional<MyTime> findByUserUserNoAndBusTimeBusTimeNo(Long userNo, Long busTimeNo);


}
