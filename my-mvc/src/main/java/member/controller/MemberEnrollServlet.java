package member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.PasswordHashing;
import member.model.dto.Member;
import member.model.dto.MemberRole;
import member.model.service.MemberService;

/**
 * Servlet implementation class MemberEnrollServlet
 */
@WebServlet("/member/memberEnroll")
public class MemberEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/memberEnroll.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String memberId = request.getParameter("memberId");
			String password = PasswordHashing.encrypt(request.getParameter("password"), memberId);
			String memberName = request.getParameter("memberName");
			String gender = request.getParameter("gender");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			
			// string -> date
			String _birthday = request.getParameter("birthday");
			Date birthday = null;
			if(_birthday != null && !"".equals(_birthday)) birthday = Date.valueOf(_birthday);
			
			// string[] -> string
			String[] _hobby = request.getParameterValues("hobby");
			String hobby = null;
			if(_hobby != null) hobby = String.join(",", _hobby);
			
			Member member = new Member(memberId, password, memberName, MemberRole.U, gender, birthday, email, phone, address, hobby, null);
			int result = memberService.insertMember(member);
			
			String msg = "회원가입을 축하합니다.";
			request.getSession().setAttribute("msg", msg);
			
			response.sendRedirect(request.getContextPath() + "/");
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
