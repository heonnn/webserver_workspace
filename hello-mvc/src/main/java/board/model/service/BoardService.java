package board.model.service;

import static common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import board.model.dao.BoardDao;
import board.model.dto.Attachment;
import board.model.dto.Board;
import board.model.dto.BoardExt;

public class BoardService {
	
	public static final int NUM_PER_PAGE = 10;
	private BoardDao boardDao = new BoardDao();

	public List<BoardExt> findAll(Map<String, Object> param) {
		Connection conn = getConnection();
		List<BoardExt> list = boardDao.findAll(conn, param);
		close(conn);
		return list;
	}

	public int getToalContents() {
		Connection conn = getConnection();
		int totalContents = boardDao.getTotalContents(conn);
		close(conn);
		return totalContents;
	}

	/**
	 * Transaction
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board) {
		Connection conn = getConnection();
		int result = 0;
		try {
			// 1. board에 등록
			result = boardDao.insertBoard(conn, board); // pk no 값 결정 - seq_board_no.nextVal
			
			// 2. board pk 가져오기
			int no = boardDao.findCurrentBoardNo(conn);
			System.out.println("방금 등록된 board.no = " + no);
			
			// 3. attachment에 등록
			List<Attachment> attachments = ((BoardExt) board).getAttachments();
			if(attachments != null && !attachments.isEmpty()) {
				for(Attachment attach : attachments) {
					attach.setBoardNo(no);
					result = boardDao.insertAttachment(conn, attach);
				}
			}
			
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public BoardExt findByNo(int no) {
		Connection conn = getConnection();
		BoardExt board = boardDao.findByNo(conn, no); // board 테이블 조회
		List<Attachment> attachments = boardDao.findAttachmentByBoardNo(conn, no); // attachment 테이블 조회
		board.setAttachments(attachments);
		close(conn);
		return board;
	}

	public int updateReadCount(int no) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = boardDao.updateReadCount(conn, no);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public Attachment findAttachmentByNo(int no) {
		Connection conn = getConnection();
		Attachment attach = boardDao.findAttachmentByNo(conn, no);
		close(conn);
		return attach;
	}

}
