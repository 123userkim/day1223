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

public class UpdateBoardOKAction implements SistAction {
	public BoardDAO dao;
	public UpdateBoardOKAction() {
		dao = new BoardDAO();
	}
	
	@Override
	public String proRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String path = request.getRealPath("upload");
		System.out.println("pat:"+path);
		MultipartRequest multi =
		new MultipartRequest(request, path, 1024*1024*5, "utf-8",
				new DefaultFileRenamePolicy());
		
		String oldFname = multi.getParameter("fname");
		
		BoardVO b = new BoardVO();
		b.setNo(Integer.parseInt(multi.getParameter("no")));
		b.setTitle(multi.getParameter("title"));
		b.setPwd(multi.getParameter("pwd"));
		b.setContent(multi.getParameter("content"));
		b.setFname(oldFname);
		b.setFsize(Integer.parseInt(multi.getParameter("fsize")));
		
		String fname = multi.getFilesystemName("uploadFile");
		if(fname != null && !fname.equals("")) {
			b.setFname(fname);
			File file = multi.getFile("uploadFile");
			b.setFsize(file.length());
		}
		int re = dao.updateBoard(b);
		String viewPage = "";
		if(re == 1) {
			viewPage = "updateBoardOK.jsp";
			if(fname != null && !fname.equals("") && oldFname != null && !oldFname.equals("")) {
				File file = new File(path + "/" + oldFname);
				file.delete();
			}
		}else {
			viewPage = "error.jsp";
			request.setAttribute("msg", "게시물 수정에 실패하였습니다.");
		}
		return viewPage;
		
		
		
	}

}






