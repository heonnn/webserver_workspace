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

import board.model.dto.Attachment;
import board.model.dto.Board;
import board.model.dto.BoardExt;
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

	public List<BoardExt> findAll(Connection conn, Map<String, Object> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<BoardExt> list = new ArrayList<>();
		String sql = prop.getProperty("findAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				BoardExt board = handleBoardResultSet(rset);
				board.setAttachCount(rset.getInt("attach_cnt"));
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

	private BoardExt handleBoardResultSet(ResultSet rset) throws SQLException {
		BoardExt board = new BoardExt();
		
		board.setNo(rset.getInt("no"));
		board.setTitle(rset.getString("title"));
		board.setMemberId(rset.getString("member_id"));
		board.setContent(rset.getString("content"));
		board.setReadCount(rset.getInt("read_count"));
		board.setRegDate(rset.getDate("reg_date"));
		
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

	public int insertBoard(Connection conn, Board board) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getMemberId());
			pstmt.setString(3, board.getContent());
			
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new BoardException("게시글 등록 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int findCurrentBoardNo(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int no = 0;
		String sql = prop.getProperty("findCurrentBoardNo");
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next())
				no = rset.getInt(1);
		} catch(SQLException e) {
			throw new BoardException("게시물 번호 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return no;
	}

	public int insertAttachment(Connection conn, Attachment attach) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertAttachment");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attach.getBoardNo());
			pstmt.setString(2, attach.getOriginalFilename());
			pstmt.setString(3, attach.getRenamedFilename());
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new BoardException("첨부파일 등록 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public BoardExt findByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		BoardExt board = null;
		String sql = prop.getProperty("findByNo");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				board = handleBoardResultSet(rset);
			}
		} catch(SQLException e) {
			throw new BoardException("게시글 한건 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return board;
	}

	public List<Attachment> findAttachmentByBoardNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Attachment> attachments = new ArrayList<>();
		String sql = prop.getProperty("findAttachmentByBoardNo");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Attachment attach = handleAttachmentResultSet(rset);
				attachments.add(attach);
			}
		} catch(SQLException e) {
			throw new BoardException("게시글 번호에 의한 첨부파일 조회 오류", e);
		}
		return attachments;
	}

	private Attachment handleAttachmentResultSet(ResultSet rset) throws SQLException {
		Attachment attach = new Attachment();
		
		attach.setNo(rset.getInt("no"));
		attach.setBoardNo(rset.getInt("board_no"));
		attach.setOriginalFilename(rset.getString("original_filename"));
		attach.setRenamedFilename(rset.getString("renamed_filename"));
		attach.setRegDate(rset.getDate("reg_date"));
		
		return attach;
	}

	public int updateReadCount(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateReadCount");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new BoardException("조회수 증가처리 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public Attachment findAttachmentByNo(Connection conn, int no) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Attachment attach = null;
		String sql = prop.getProperty("findAttachmentByNo");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			if(rset.next()) attach = handleAttachmentResultSet(rset);
		} catch(SQLException e) {
			throw new BoardException("첨부파일 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return attach;
	}

	public int deleteBoard(Connection conn, int no) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteBoard");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new BoardException("게시글 삭제 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}
}
