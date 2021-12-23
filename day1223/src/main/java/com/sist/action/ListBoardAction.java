package com.sist.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;
import com.sist.vo.BoardVO;

public class ListBoardAction implements SistAction {
	public BoardDAO dao;
	public ListBoardAction() {
		dao = new BoardDAO();
	}
	
	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//페이지수를 안받을 수도 있음  : 처음으로 들어갈 때
		//페이지를 1로 설정을 해두고
		int pageNUM=1;
		//만약 페이지 수를 받는다면, 받은 페이지수를 반환
		if(request.getParameter("pageNUM")!=null) {			
			pageNUM =Integer.parseInt(request.getParameter("pageNUM"));
		}
		 
		System.out.println("pageNUM:"+pageNUM);
		
		//매개변수를 갖는 메소드로 바꾸기
		ArrayList<BoardVO> list  = dao.listBoard(pageNUM);		
		//total페이지를 상태유지 해줘야함
		request.setAttribute("totalPage", BoardDAO.totalPage);
		request.setAttribute("list", list);
		return "listBoard.jsp";
	}
}



