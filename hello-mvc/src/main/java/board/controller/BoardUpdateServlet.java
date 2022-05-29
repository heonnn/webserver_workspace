package board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import board.model.dto.Attachment;
import board.model.dto.BoardExt;
import board.model.service.BoardService;
import common.HelloMvcFileRenamePolicy;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/board/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	/**
	 * 수정폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무로직
		BoardExt board = boardService.findByNo(no);
		
		// 3. view단 처리
		request.setAttribute("board", board);
		request.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp")
			.forward(request, response);
	}

	/**
	 * DB 수정 요청
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 2. MultipartRequest 객체 생성
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			int maxPostSize = 1024 * 1024 * 10;
			String encoding = "utf-8";
			FileRenamePolicy policy = new HelloMvcFileRenamePolicy();
			MultipartRequest multiReq = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
			
			
			// 3. 사용자 입력값 처리
			int no = Integer.parseInt(multiReq.getParameter("no"));
			String title = multiReq.getParameter("title");
			String memberId = multiReq.getParameter("memberId");
			String content = multiReq.getParameter("content");
			String[] delFiles = multiReq.getParameterValues("delFile"); // 삭제하려는 첨부파일 pk
			
			BoardExt board = new BoardExt();
			board.setNo(no);
			board.setTitle(title);
			board.setMemberId(memberId);
			board.setContent(content);
			
			File upFile1 = multiReq.getFile("upFile1");
			File upFile2 = multiReq.getFile("upFile2");
			
			// 첨부한 파일이 하나라도 있는 경우
			if(upFile1 != null || upFile2 != null) {
				List<Attachment> attachments = new ArrayList<>();
				if(upFile1 != null) attachments.add(getAttachment(multiReq, no, "upFile1"));
				if(upFile2 != null) attachments.add(getAttachment(multiReq, no, "upFile2"));
				
				board.setAttachments(attachments);
			}
			
			// 4. 업무로직
			int result = boardService.updateBoard(board);
			// 첨부파일 삭제 처리
			if(delFiles != null) {
				for(String temp : delFiles) {
					int attachNo = Integer.parseInt(temp); // attachment pk
					Attachment attach = boardService.findAttachmentByNo(attachNo);
					
					// a. 파일 삭제
					File delFile = new File(saveDirectory, attach.getRenamedFilename());
					if(delFile.exists()) delFile.delete();
					
					// b. db record 삭제
					result = boardService.deleteAttachment(attachNo);
					System.out.println("> " + attachNo + "번 첨부파일 삭제!");
				}
			}
			
			// 5. 리다이렉트
			response.sendRedirect(request.getContextPath() + "/board/boardView?no=" + no);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private Attachment getAttachment(MultipartRequest multiReq, int boardNo, String name) {
		Attachment attach = new Attachment();
		attach.setBoardNo(boardNo);
		attach.setOriginalFilename(multiReq.getOriginalFileName(name));
		attach.setRenamedFilename(multiReq.getFilesystemName(name));
		return attach;
		
	}

}
