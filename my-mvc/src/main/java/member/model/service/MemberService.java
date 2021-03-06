package member.model.service;

import static common.JdbcTemplate.*;

import java.sql.Connection;

import member.model.dao.MemberDao;
import member.model.dto.Member;

public class MemberService {
	private MemberDao memberDao = new MemberDao();

	public Member findByMemberId(String memberId) {
		Connection conn = getConnection(); 
		Member member = memberDao.findByMemberId(conn, memberId);
		close(conn);
		return member;
	}

	public int insertMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = memberDao.insertMember(conn, member);
			commit(conn);
		} catch(Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	public int updateMember(Member member) {
		Connection conn = getConnection();
		int result = 0;
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
		Connection conn = getConnection();
		int result = 0;
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

}
