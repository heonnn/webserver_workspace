package board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.dto.Attachment;
import board.model.service.BoardService;

/**
 * Servlet implementation class BoardDeleteServlet
 */
@WebServlet("/board/boardDelete")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 입력값 처리
			int no = Integer.parseInt(request.getParameter("no"));
			
			// 2. 업무로직
			// a. 파일 삭제
			List<Attachment> attachments = boardService.findByNo(no).getAttachments();
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			for(Attachment attach : attachments) {
				File file = new File(saveDirectory, attach.getRenamedFilename());
				if(file.exists()) file.delete();
			}
			// b. db
			int result = boardService.deleteBoard(no);
			
			// 3. 리다이렉트
			response.sendRedirect(request.getContextPath() + "/board/boardList");
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
