package board.model.service;

import static common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import board.model.dao.BoardDao;
import board.model.dto.Board;

public class BoardService {
	
	public static final int NUM_PER_PAGE = 10;
	private BoardDao boardDao = new BoardDao();

	public List<Board> findAll(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Board> list = boardDao.findAll(conn, param);
		close(conn);
		return list;
	}

	public int getToalContents() {
		Connection conn = getConnection();
		int totalContents = boardDao.getTotalContents(conn);
		close(conn);
		return totalContents;
	}

}
