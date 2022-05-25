package member.model.dao;

import static common.JdbcTemplate.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import member.model.dto.Member;
import member.model.dto.MemberRole;
import member.model.exception.MemberException;

public class MemberDao {
	Properties prop = new Properties();
	
	public MemberDao() {
		try {
			String fileName = MemberDao.class.getResource("/sql/memberQuery.properties").getPath();
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Member findByMemberId(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		String sql = prop.getProperty("findByMemberId");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset = pstmt.executeQuery();
			if(rset.next()) member = handleMemberResultSet(rset);
		} catch(SQLException e) {
			throw new MemberException("로그인 오류", e);
		} finally {
			close(pstmt);
		}
		return member;
	}
	
	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		Member member = new Member();
		member.setMemberId(rset.getString("member_id"));
		member.setPassword(rset.getString("password"));
		member.setMemberName(rset.getString("member_name"));
		// "U" -> MemberRole.U, "A" -> MemberRole.A
		member.setMemberRole(MemberRole.valueOf(rset.getString("member_role")));
		member.setGender(rset.getString("gender"));
		member.setBirthday(rset.getDate("birthday"));
		member.setEmail(rset.getString("email"));
		member.setPhone(rset.getString("phone"));
		member.setAddress(rset.getString("address"));
		member.setHobby(rset.getString("hobby"));
		member.setEnrollDate(rset.getDate("enroll_date"));
		return member;
	}

	public int insertMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("insertMember");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getMemberRole().toString());
			pstmt.setString(5, member.getGender());
			pstmt.setDate(6, member.getBirthday());
			pstmt.setString(7, member.getEmail());
			pstmt.setString(8, member.getPhone());
			pstmt.setString(9, member.getAddress());
			pstmt.setString(10, member.getHobby());
			
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new MemberException("회원가입 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	
}
