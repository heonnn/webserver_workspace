package board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.service.BoardService;

/**
 * Servlet implementation class BoardCommentDeleteServlet
 */
@WebServlet("/board/boardCommentDelete")
public class BoardCommentDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 사용자 입력값 처리
			int commentNo = Integer.parseInt(request.getParameter("commentNo"));
			int boardNo = Integer.parseInt(request.getParameter("boardNo"));
			
			// 업무로직
			int result = boardService.deleteBoardComment(commentNo);
			
			response.sendRedirect(request.getContextPath() + "/board/boardView?no=" + boardNo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
