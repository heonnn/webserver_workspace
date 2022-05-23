package board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.dto.Board;
import board.model.service.BoardService;
import common.HelloMvcUtils;

/**
 * Servlet implementation class BoardListServlet
 */
@WebServlet("/board/boardList")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			int numPerPage = BoardService.NUM_PER_PAGE;
			int cPage = 1;
			
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch(NumberFormatException e) {}
			
			Map<String, Object> param = new HashMap<String, Object>();
			// 1 ~ 10, 11 ~ 20, 21 ~ 30
			int start = (cPage - 1) * numPerPage + 1;
			int end = cPage * numPerPage;
			param.put("start", start);
			param.put("end", end);
			
			
			// 2. 업무로직
			// 2.a content 영역
			List<Board> list = boardService.findAll(param);
			
			// 2.b pagebar 영역
			int totalContents = boardService.getToalContents();
			String url = request.getRequestURI();
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalContents, url);
			// 전체 페이지 수
			// 
			request.setAttribute("list", list);
			request.setAttribute("pagebar", pagebar);
			request.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp")
				.forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
