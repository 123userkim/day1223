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
		
		//���������� �ȹ��� ���� ����  : ó������ �� ��
		//�������� 1�� ������ �صΰ�
		int pageNUM=1;
		//���� ������ ���� �޴´ٸ�, ���� ���������� ��ȯ
		if(request.getParameter("pageNUM")!=null) {			
			pageNUM =Integer.parseInt(request.getParameter("pageNUM"));
		}
		 
		System.out.println("pageNUM:"+pageNUM);
		
		//�Ű������� ���� �޼ҵ�� �ٲٱ�
		ArrayList<BoardVO> list  = dao.listBoard(pageNUM);		
		//total�������� �������� �������
		request.setAttribute("totalPage", BoardDAO.totalPage);
		request.setAttribute("list", list);
		return "listBoard.jsp";
	}
}



