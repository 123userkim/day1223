package com.sist.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.ast.IfStatement;

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
		//세션이 게속 필요해서 변수로 아예 설정함
		//request에서 세션을 얻어올 수가 있음
		HttpSession session = request.getSession();
		
		
		request.setCharacterEncoding("utf-8");
		String orderColum = request.getParameter("orderColum");
		System.out.println("정렬컬럼 : "+ orderColum);
		
		String searchCloum = request.getParameter("searchCloum");
		System.out.println("검색컬럼"+searchCloum);
		
		String keyword = request.getParameter("keyword");
		System.out.println("검색어:"+keyword);
		
		//새로운 검색어가 없고, 세션에 상태유지된 검색어가 있다면
		//검색한 상태에서 페이징이 이루어지는지
		//총5페이지일 때 검색한 내용만 나오는가에 대한 것 		
		//---------------------------------------------------------------
		//아까 검색한 게 있다면 그걸 가져와야함 
		if(keyword == null && session.getAttribute("keyword")!= null) {
			searchCloum =(String)session.getAttribute("searchCloum");
			keyword =(String)session.getAttribute("keyword");	 			
		}//만약 레코드 수가 10
		
		
		
		//새로운 정렬 컬럼이 없고, 아까 정렬한 게 있다면
		if(orderColum == null && session.getAttribute(orderColum)!= null) {
			orderColum =(String)session.getAttribute("orderColum");	
		}
		
		
		//페이지수를 안받을 수도 있음  : 처음으로 들어갈 때
		//페이지를 1로 설정을 해두고
		int pageNUM=1;
		//만약 페이지 수를 받는다면, 받은 페이지수를 반환
		if(request.getParameter("pageNUM")!=null) {			
			pageNUM =Integer.parseInt(request.getParameter("pageNUM"));
		}
		 
		System.out.println("pageNUM:"+pageNUM);
		
		//매개변수를 갖는 메소드로 바꾸기
		ArrayList<BoardVO> list  = dao.listBoard(pageNUM,orderColum,searchCloum,keyword);		
		//total페이지를 상태유지 해줘야함
		request.setAttribute("totalPage", BoardDAO.totalPage);
		request.setAttribute("list", list);
		
		//searchColumn, keyword 상태유지 = >페이지수가 다름
		if(keyword != null){
			//세션에다가 상태유지를 시켜야함
			session.setAttribute("keyword", keyword);
			session.setAttribute("searchCloum", searchCloum);			
		}		
		
		if(orderColum != null) {
			session.setAttribute("orderColum", orderColum);
		}
		return "listBoard.jsp";
	}
}



