package board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.dto.Attachment;
import board.model.service.BoardService;
import common.HelloMvcUtils;

/**
 * Servlet implementation class BoardFileDownloadServlet
 */
@WebServlet("/board/fileDownload")
public class BoardFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무로직
		Attachment attach = boardService.findAttachmentByNo(no);
		System.out.println(attach);
		
		// 3. 응답처리
		String saveDirectory = getServletContext().getRealPath("/upload/board");
		String originalFilename = attach.getOriginalFilename();
		String renamedFilename = attach.getRenamedFilename();
		// 헤더작성
		HelloMvcUtils.fileDownload(response, saveDirectory, originalFilename, renamedFilename); 
		
	}

	

}
