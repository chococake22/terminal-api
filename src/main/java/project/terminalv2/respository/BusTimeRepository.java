package project.terminalv2.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.terminalv2.domain.BusTime;

@Repository
public interface BusTimeRepository extends JpaRepository<BusTime, Long> {
}
