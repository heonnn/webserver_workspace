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
 * Servlet implementation class FindAllServlet
 */
@WebServlet("/celeb/findAll")
public class FindAllServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 업무로직
		CelebManager manager = CelebManager.getInstance();
		List<Celeb> celebList = manager.getCelebList();
		
		// 2. 응답처리 - json 변환해서 출력
		response.setContentType("application/json; charset=utf-8");
		Gson gson = new Gson();
		String jsonStr = gson.toJson(celebList);
		System.out.println("jsonStr = " + jsonStr);
		response.getWriter().append(jsonStr);
		
	}

}
