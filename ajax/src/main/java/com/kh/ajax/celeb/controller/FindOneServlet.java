package com.kh.ajax.celeb.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kh.ajax.celeb.dto.Celeb;
import com.kh.ajax.celeb.manager.CelebManager;

/**
 * Servlet implementation class FindOneServlet
 */
@WebServlet("/celeb/findOne")
public class FindOneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무로직
		List<Celeb> celebList = CelebManager.getInstance().getCelebList();
		Celeb celeb = null;
		for(Celeb c : celebList) {
			if(c.getNo() == no)
				celeb = c;
		}
		
		// 3. 응답처리 json
		response.setContentType("application/json; charset=utf-8");
//		response.getWriter().append(new Gson().toJson(celeb));
		new Gson().toJson(celeb, response.getWriter());
	}

}
