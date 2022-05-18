package member.model.service;

import static common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import member.model.dao.MemberDao;
import member.model.dto.Member;

/**
 * 
 * 1. connection 생성
 * 2. dao요청 (connection)
 * 3. dml 경우 transaction 처리
 * 4. connection 반환
 * 5. controller로 반환 처리
 *
 */
public class MemberService {
	
	private MemberDao memberDao = new MemberDao();
	
	public Member findByMemberId(String memberId) {
		Connection conn = getConnection();
		Member member = memberDao.findByMemberId(conn, memberId);
		close(conn);
		return member;
	}
	
	public int insertMember(Member member) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.insertMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e; // controller에게 통보용.
		} finally {
			close(conn);
		}
		return result;
	}

	public int updateMember(Member member) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.updateMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public int deleteMember(String memberId) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.deleteMember(conn, memberId);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public int updatePassword(Member updateMember) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.updatePassword(conn, updateMember);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public List<Member> findAll() {
		Connection conn = getConnection();
		List<Member> list = memberDao.findAll(conn);
		close(conn);
		return list;
	}

	public int updateMemberRole(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.updateMemberRole(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
		} finally {
			close(conn);
		}
		return result;
	}

	public List<Member> findBy(Map<String, String> param) {
		Connection conn = getConnection();
		List<Member> list = memberDao.findBy(conn, param);
		close(conn);
		return list;
	}

}
