package com.kh.ajax.vanilla_js;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VanillaJsSignupServlet
 */
@WebServlet("/vanilla-js/signup")
public class VanillaJsSignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			String username = request.getParameter("username");
			String email = request.getParameter("email");
			System.out.println("usersname = " + username + ", email = " + email);
			
			if(username.isEmpty() || email.isEmpty())
				throw new IllegalArgumentException("사용자이름 또는 이메일을 꼭 작성해주세요.");
			// 2. 업무로직
			
			// 3. view단 처리 - 비동기요청 DML은 redirect 필요없다.
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().append("회원가입 성공!");
		} catch (Exception e) {
			e.printStackTrace();
//			throw e;
			// 사용자 응답 비동기
			response.setContentType("text/plain; charset=utf-8");
			response.getWriter().append(e.getMessage());
		}
	}

}
