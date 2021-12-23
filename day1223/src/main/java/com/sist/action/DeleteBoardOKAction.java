package com.sist.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.dao.BoardDAO;

public class DeleteBoardOKAction implements SistAction {
	
	public BoardDAO dao;
	public DeleteBoardOKAction() {
		dao = new BoardDAO();
	}
	
	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int no = Integer.parseInt(request.getParameter("no"));
		String oldFname = dao.getBoard(no).getFname();
		String pwd = request.getParameter("pwd");
		int re = dao.deleteBoard(no, pwd);
		String viewPage = "";
		if(re == 1) {
			viewPage = "deleteBoardOK.jsp";
			if(oldFname != null && !oldFname.equals("")) {
				String path = request.getRealPath("upload");
				File file = new File(path + "/" + oldFname);
				file.delete();
			}
		}else {
			viewPage = "error.jsp";
			request.setAttribute("msg", "게시물 삭제에 실패하였습니다.");
		}
		
		return viewPage;
	}

}