package com.sist.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.sist.dao.BoardDAO;
import com.sist.vo.BoardVO;

public class InsertBoardOKAction implements SistAction {
	
	public BoardDAO dao;
	public InsertBoardOKAction() {
		dao = new BoardDAO();
	}
	

	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String path = request.getRealPath("upload");
		System.out.println("pat:"+path);
		MultipartRequest multi =
		new MultipartRequest(request, path, 1024*1024*5, "utf-8",
				new DefaultFileRenamePolicy());
		
		BoardVO b = new BoardVO();
		b.setTitle(multi.getParameter("title"));
		b.setWriter(multi.getParameter("writer"));
		b.setPwd(multi.getParameter("pwd"));
		b.setContent(multi.getParameter("content"));
		b.setFname("");
		b.setFsize(0);
		
		String fname = multi.getFilesystemName("uploadFile");
		if(fname != null && !fname.equals("")) {
			b.setFname(fname);
			File file = multi.getFile("uploadFile");
			b.setFsize(file.length());
		}
		int re = dao.insertBoard(b);
		String viewPage = "";
		if(re == 1) {
			viewPage = "insertBoardOK.jsp";
		}else {
			viewPage = "error.jsp";
			request.setAttribute("msg", "게시물 등록에 실패하였습니다.");
		}
		return viewPage;
	}

}










