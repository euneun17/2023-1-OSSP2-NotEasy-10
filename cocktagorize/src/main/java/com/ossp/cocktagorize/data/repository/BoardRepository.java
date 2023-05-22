package com.ossp.cocktagorize.data.repository;

import com.ossp.cocktagorize.data.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {
    public Board findById(int id);
    Board save(Board board);
    void deleteById(int id);
    Page<Board> findAllByOrderByIdDesc(Pageable pageable);
    Page<Board> findAllByOrderByTitle(Pageable pageable);
    Page<Board> findAllByOrderByLikedDesc(Pageable pageable);
    Page<Board> findAllByTitleContaining(String title,Pageable pageable);
    Page<Board> findAllByContentContaining(String content,Pageable pageable);
    List<Board> findAllByUserId(int userId);
    @Query(value="select b.*, Max(br.created_date) as creation_time from board b left outer join board_reply br on b.board_id=br.board_id group by b.board_id order by creation_time",nativeQuery = true)
    Page<Board> findAllByBoardReplyCreationTime(Pageable pageable);
}
