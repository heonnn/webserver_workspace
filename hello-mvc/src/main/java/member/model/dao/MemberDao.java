package member.model.dao;

import static common.JdbcTemplate.close;
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

import member.model.dto.Member;
import member.model.dto.MemberRole;
import member.model.exception.MemberException;

public class MemberDao {
	private Properties prop = new Properties();
	
	public MemberDao() {
		try {
			// buildpath의 sql/member-query.properties파일의 내용을 불러오기
			String fileName = MemberDao.class.getResource("/sql/member-query.properties").getPath();
			System.out.println("fileName@MemberDao = " + fileName);
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Member findByMemberId(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("findByMemberId");
		Member member = null;
		
		try {
			// 1. pstmt 객체 & 미완성쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			// 2. 실행 및 rset 처리
			rset = pstmt.executeQuery();
			while(rset.next()) {
				member = handleMemberResultSet(rset);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 3. 자원반납(rset, pstmt)
			close(rset);
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
			throw new MemberException("회원가입오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int updateMember(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMember");
		try {
			// 1. pstmt 객체 생성 & 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getGender());
			pstmt.setDate(3, member.getBirthday());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getHobby());
			pstmt.setString(8, member.getMemberId());
			
			// 2. 실행
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new MemberException("회원정보수정 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int deleteMember(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("deleteMember");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			throw new MemberException("회원탈퇴 오류", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public int updatePassword(Connection conn, Member updateMember) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updatePassword");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, updateMember.getPassword());
			pstmt.setString(2, updateMember.getMemberId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			throw new MemberException("비밀번호 수정 오류!", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	/**
	 * 1건 조회시 member 객체 하나 또는 null 리턴
	 * n건 조회시 여러건의 member 객체를 가진 list 또는 빈 list 리턴
	 * @param conn
	 * @return
	 */
	public List<Member> findAll(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		String sql = prop.getProperty("findAll");
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
		} catch(Exception e) {
			throw new MemberException("관리자 회원목록 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

	public int updateMemberRole(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = prop.getProperty("updateMemberRole");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberRole().toString());
			pstmt.setString(2, member.getMemberId());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			throw new MemberException("권한정보 수정 오류!", e);
		} finally {
			close(pstmt);
		}
		return result;
	}

	public List<Member> findBy(Connection conn, Map<String, String> param) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		String sql = prop.getProperty("findBy");
		sql = sql.replace("#", param.get("searchType"));
		System.out.println("sql = " + sql);
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + param.get("searchKeyword") + "%");
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
		} catch(Exception e) {
			throw new MemberException("관리자 회원검색 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		return list;
	}

}
