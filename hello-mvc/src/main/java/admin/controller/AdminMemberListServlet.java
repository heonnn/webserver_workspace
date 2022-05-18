package admin.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberListServlet
 */
@WebServlet("/admin/memberList")
public class AdminMemberListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * select * from member order by enroll_date desc
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 업무 로직
			List<Member> list = memberService.findAll();
//			System.out.println("List = " + list);
			request.setAttribute("list", list);
			
			// 2. view단 처리
			request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp")
			.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
