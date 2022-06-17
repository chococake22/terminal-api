package project.terminalv2.respository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.terminalv2.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 제목
    Page<Board> findAllByTitleContaining(String search, Pageable pageable);

    // 작성자
    Page<Board> findAllByWriterContaining(String search, Pageable pageable);

    // 내용
    Page<Board> findAllByContentContaining(String search, Pageable pageable);

    // 전체 카테고리 포함
    @Query(value = "SELECT b FROM Board b WHERE b.title like %:keyword% or b.writer like  %:keyword% or  b.content like  %:keyword%")
    Page<Board> findAllBySearch(String keyword, Pageable pageable);
}
