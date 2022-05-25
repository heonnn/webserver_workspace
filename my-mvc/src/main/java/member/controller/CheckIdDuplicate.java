package member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class CheckIdDuplicate
 */
@WebServlet("/member/checkIdDuplicate")
public class CheckIdDuplicate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String memberId = request.getParameter("memberId");
			
			Member member = memberService.findByMemberId(memberId);
			boolean available = (member == null);
			
			request.setAttribute("memberId", memberId);
			request.setAttribute("available", available);
			request.getRequestDispatcher("/WEB-INF/views/member/checkIdDuplicate.jsp")
			.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
