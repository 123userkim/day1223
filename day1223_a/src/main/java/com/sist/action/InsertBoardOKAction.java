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
		int no = dao.getNextNo();
		int b_ref = no;
		int b_level = 0;
		int b_step = 0;
		
	 int pno = Integer.parseInt(multi.getParameter("no")   );	
		//no가 0이 아닌 값이 온다면 그건 답글 
	 if(pno !=0 ) {
		BoardVO p =  dao.getBoard(pno);
		//부모들의 모든걸 먼저 갖고 옴
		b_ref = p.getB_ref();
		b_level = p.getB_level();
		b_step = p.getB_step();
		
		//step증가시키기 전에 이전에 답글이 있다면 그걸 증가시켜야함 
		//새로운 메소드를 만듦
		dao.updateStep(b_ref,b_step);
		b_level++;
		b_step++;
	 }
		
		
		
		b.setTitle(multi.getParameter("title"));
		b.setWriter(multi.getParameter("writer"));
		b.setPwd(multi.getParameter("pwd"));
		b.setContent(multi.getParameter("content"));
		b.setFname("");
		b.setFsize(0);
		
		b.setNo(no);
		b.setB_ref(b_ref);
		b.setB_level(b_level);
		b.setB_step(b_step);
		
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










