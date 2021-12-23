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
		//������ �Լ� �ʿ��ؼ� ������ �ƿ� ������
		//request���� ������ ���� ���� ����
		HttpSession session = request.getSession();
		
		
		request.setCharacterEncoding("utf-8");
		String orderColum = request.getParameter("orderColum");
		System.out.println("�����÷� : "+ orderColum);
		
		String searchCloum = request.getParameter("searchCloum");
		System.out.println("�˻��÷�"+searchCloum);
		
		String keyword = request.getParameter("keyword");
		System.out.println("�˻���:"+keyword);
		
		//���ο� �˻�� ����, ���ǿ� ���������� �˻�� �ִٸ�
		//�˻��� ���¿��� ����¡�� �̷��������
		//��5�������� �� �˻��� ���븸 �����°��� ���� �� 		
		//---------------------------------------------------------------
		//�Ʊ� �˻��� �� �ִٸ� �װ� �����;��� 
		if(keyword == null && session.getAttribute("keyword")!= null) {
			searchCloum =(String)session.getAttribute("searchCloum");
			keyword =(String)session.getAttribute("keyword");	 			
		}//���� ���ڵ� ���� 10
		
		
		
		//���ο� ���� �÷��� ����, �Ʊ� ������ �� �ִٸ�
		if(orderColum == null && session.getAttribute(orderColum)!= null) {
			orderColum =(String)session.getAttribute("orderColum");	
		}
		
		
		//���������� �ȹ��� ���� ����  : ó������ �� ��
		//�������� 1�� ������ �صΰ�
		int pageNUM=1;
		//���� ������ ���� �޴´ٸ�, ���� ���������� ��ȯ
		if(request.getParameter("pageNUM")!=null) {			
			pageNUM =Integer.parseInt(request.getParameter("pageNUM"));
		}
		 
		System.out.println("pageNUM:"+pageNUM);
		
		//�Ű������� ���� �޼ҵ�� �ٲٱ�
		ArrayList<BoardVO> list  = dao.listBoard(pageNUM,orderColum,searchCloum,keyword);		
		//total�������� �������� �������
		request.setAttribute("totalPage", BoardDAO.totalPage);
		request.setAttribute("list", list);
		
		//searchColumn, keyword �������� = >���������� �ٸ�
		if(keyword != null){
			//���ǿ��ٰ� ���������� ���Ѿ���
			session.setAttribute("keyword", keyword);
			session.setAttribute("searchCloum", searchCloum);			
		}		
		
		if(orderColum != null) {
			session.setAttribute("orderColum", orderColum);
		}
		return "listBoard.jsp";
	}
}



