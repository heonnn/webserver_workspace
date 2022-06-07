package com.kh.ajax.celeb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kh.ajax.celeb.dto.Celeb;
import com.kh.ajax.celeb.dto.CelebType;
import com.kh.ajax.celeb.manager.CelebManager;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/celeb/update")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 0. 파일 업로드
		String saveDirectory = getServletContext().getRealPath("/images");
		int maxPostSize = 1024 * 1024 * 10; // 10mb
		String encoding = "utf-8";
		FileRenamePolicy policy = new DefaultFileRenamePolicy();
		MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
		
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(multiReq.getParameter("no"));
		String name = multiReq.getParameter("name");
		CelebType type = CelebType.valueOf(multiReq.getParameter("type"));
		String profile = null;
		if(multiReq.getFile("profile") != null)
			profile = multiReq.getFilesystemName("profile");
		System.out.println("no = " + no + ", name = " + name + ", type = " + type + ", profile = " + profile);
		
		// 2. 업무로직
		Celeb updatedCeleb = null;
		for(Celeb celeb : CelebManager.getInstance().getCelebList()) {
			if(celeb.getNo() == no) {
				celeb.setName(name);
				celeb.setType(type);
				if(profile != null)
					celeb.setProfile(profile);
				updatedCeleb = celeb;
				break;
			}
		}
		
		// 3. 응답처리
		response.setContentType("application/json; charset=utf-8");
		Map<String, Object> map = new HashMap<>();
		map.put("result", 1);
		map.put("data", updatedCeleb);
		
		response.getWriter().append(new Gson().toJson(map));
	}

}
