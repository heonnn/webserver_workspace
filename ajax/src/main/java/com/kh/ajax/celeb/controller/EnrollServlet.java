package com.kh.ajax.celeb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.kh.ajax.celeb.dto.Celeb;
import com.kh.ajax.celeb.dto.CelebType;
import com.kh.ajax.celeb.manager.CelebManager;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class EnrollServlet
 */
@WebServlet("/celeb/enroll")
public class EnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. MultipartRequest 객체 생성
			String saveDirectory = getServletContext().getRealPath("/images");
			int maxPostSize = 1024 * 1024 * 10; // 10mb
			String encoding = "utf-8";
			FileRenamePolicy policy = new DefaultFileRenamePolicy();
			MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
			
			// 2. 사용자 입력값 처리
			int no = Integer.parseInt(multiReq.getParameter("no"));
			String name = multiReq.getParameter("name");
			CelebType type = CelebType.valueOf(multiReq.getParameter("type"));
			String profile = "default.png"; // 기본값
			if(multiReq.getFile("profile") != null) {
				profile = multiReq.getFilesystemName("profile"); // 저장된 파일명
			}
			
			Celeb celeb = new Celeb(no, name, type, profile);
			System.out.println(celeb);
			
			// 3. 업무로직 : CelebManager#celebList에 추가
			List<Celeb> celebList = CelebManager.getInstance().getCelebList();
			celebList.add(celeb);
			
			// 4. 응답 : 생성된 celeb 객체 반환
			response.setContentType("application/json; charset=utf-8");
			// map, dto -> object
			// list -> array
			Map<String, Object> map = new HashMap<>();
			map.put("result", 1);
			map.put("data", celeb);
			
			new Gson().toJson(map, response.getWriter());
		} catch (Exception e) {
			e.printStackTrace();
			
			// throw e; // jquery.ajax.error 함수 호출!
			Map<String, Object> map = new HashMap<>();
			map.put("result", 0);
			map.put("msg", "등록 실패!");
			new Gson().toJson(map, response.getWriter());
		}
	}

}
