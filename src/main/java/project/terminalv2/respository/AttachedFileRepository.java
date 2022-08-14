package project.terminalv2.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.terminalv2.domain.AttachedFile;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {

    Optional<AttachedFile> findByBoard_BoardNoAndFilename(Long BoardNo, String filename);

    void deleteAllByBoardBoardNo(Long BoardNo);
}
