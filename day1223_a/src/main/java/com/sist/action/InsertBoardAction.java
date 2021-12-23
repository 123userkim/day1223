package com.sist.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InsertBoardAction implements SistAction {

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		int no= 0 ;
		String title ="새 글 작성";
		if(request.getParameter("no") !=null) {
			no = Integer.parseInt(request.getParameter("no"));
			title = "답글 작성";
		}
		
		//새글이면 0이 상태유지 되어서 가는 거고, 답글이면 부모의 no를 상태유지
		request.setAttribute("no", no);
		request.setAttribute("title", title);
		return "insertBoard.jsp";
	}

}
