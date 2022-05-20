package member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.PasswordHashing;
import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class MemberLoginServlet
 */
@WebServlet("/member/memberLogin")
public class MemberLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 인코딩
		request.setCharacterEncoding("utf-8");
		
		// 2. 입력값 처리
		String memberId = request.getParameter("memberId");
		String password = PasswordHashing.encrypt(request.getParameter("password"), memberId);
		String saveId = request.getParameter("saveId"); // on | null
		
		// 3. 업무로직
		Member loginMember = memberService.findByMemberId(memberId);

		HttpSession session = request.getSession();
		if(loginMember != null && loginMember.getPassword().equals(password)) {
			session.setAttribute("loginMember", loginMember);
			
			// cookie 설정 - 아이디 저장
			Cookie cookie = new Cookie("saveId", memberId);
			cookie.setPath(request.getContextPath());
			if(saveId != null) {
				cookie.setMaxAge(7 * 24 * 60 * 60);
			} else {
				cookie.setMaxAge(0);
			}
			response.addCookie(cookie);
		} else {
			session.setAttribute("msg", "아이디 또는 비밀번호가 일치하지 않습니다.");
		}
		
		// 4. 리다이렉트
		response.sendRedirect(request.getContextPath() + "/");
	}
	

}
