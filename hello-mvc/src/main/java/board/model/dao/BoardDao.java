package board.model.dao;

import static common.JdbcTemplate.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import board.model.dto.Board;
import board.model.exception.BoardException;

public class BoardDao {
	Properties prop = new Properties();
	
	public BoardDao() {
		try {
			String fileName = BoardDao.class.getResource("/sql/board-query.properties").getPath();
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Board> findAll(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> list = new ArrayList<Board>();
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Board board = handleBoardResultSet(rset);
				list.add(board);
			}
		} catch(SQLException e) {
			throw new BoardException("게시글 목록 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

	private Board handleBoardResultSet(ResultSet rset) throws SQLException {
		Board board = new Board();
		
		board.setNo(rset.getInt("no"));
		board.setTitle(rset.getString("title"));
		board.setMemberId(rset.getString("member_id"));
		board.setRegDate(rset.getDate("reg_date"));
		board.setReadCount(rset.getInt("read_count"));
		
		return board;
	}

	public int getTotalContents(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContents = 0;
		String sql = prop.getProperty("getTotalContents");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				totalContents = rset.getInt(1);
			}
		} catch(SQLException e) {
			throw new BoardException("전체 게시글수 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return totalContents;
	}
	
}
