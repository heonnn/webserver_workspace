package member.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class MemberUpdateServlet
 */
@WebServlet("/member/memberUpdate")
public class MemberUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 입력값 처리
			String memberId = request.getParameter("memberId");
			String memberName = request.getParameter("memberName");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			String gender = request.getParameter("gender");
			
			String _birthday = request.getParameter("birthday");
			Date birthday = null;
			if(_birthday != null && !"".equals(_birthday)) birthday = Date.valueOf(_birthday);
			String[] _hobby = request.getParameterValues("hobby");
			String hobby = null;
			if(_hobby != null) hobby = String.join(",", _hobby);
			
			Member member = new Member(memberId, _birthday, memberName, null, gender, birthday, email, phone, address, hobby, birthday);
			
			// 2. 업무 로직
			int result = memberService.updateMember(member);
			String msg = "회원정보를 성공적으로 수정했습니다.";
			
			// 세션 정보 갱신
			HttpSession session = request.getSession();
			Member updateMember = memberService.findByMemberId(memberId);
			session.setAttribute("loginMember", updateMember);
			
			// 3. 리다이렉트
			session.setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath() + "/member/memberView");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		
		
	}

}
