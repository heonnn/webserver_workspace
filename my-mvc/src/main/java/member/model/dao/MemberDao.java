package member.model.dao;

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
			while(rset.next()) {
				member = handleMemberResultSet(rset);
			}
		} catch(SQLException e) {
			throw new MemberException("로그인 오류", e);
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

	
}
