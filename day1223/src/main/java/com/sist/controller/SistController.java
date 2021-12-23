package com.sist.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.action.SistAction;

/**
 * Servlet implementation class SistController
 */

//do라고 하는 모든 것에 대헤서 콘트롤러가 작동함
@WebServlet("*.do")
public class SistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//사용자가 요청할 서비스명, 일처리담당
	HashMap<String, SistAction> map;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SistController() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    //딱 한번 작동하는 init이라는 메소드를 오버라이딩해서 파일을 읽어옮
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		//super.init(config);
		map = new HashMap<String, SistAction>();
		try {
			//실제경로를 알기
			String path = config.getServletContext().getRealPath("WEB-INF");
	    	//파일을 읽어들임
			FileReader fr = new FileReader(path + "/sist.properties");
			//key value관리
			Properties prop = new Properties();
			//설정 파일로부터 프로퍼티의 정보를 가져올 수 있음
			prop.load(fr);
			Iterator keyList = prop.keySet().iterator();
			while(keyList.hasNext()) {
				String key = (String)keyList.next();
				String clsName = prop.getProperty(key);
				SistAction obj = (SistAction)Class.forName(clsName).newInstance();
				map.put(key, obj);
			}
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		proRequest(request, response);
		//원래 있던 서비스는 삭제하기
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		proRequest(request, response);
		//원래 있던 서비스는 삭제하기
	}
	//get, post 방식 어떤 걸 사용해도 proRequest호출하기
	//사용자가 요청한 정보, 응답한 정보를 매개변수로 받아옴, 예외처리도 항상 같이
	public void proRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String uri = request.getRequestURI();//현재프로젝트랑 나머지이름도 같이 나옴
		/*
		 	/day1222/listBoard.do
		*/
		
		String cmd = uri.substring( uri.lastIndexOf("/") + 1 ); 
		//서비스명만 추출 -> 끝에서부터 찾기 , /는 비포함 : +1
		//서비스명에 대한 일처리의 action map을 꺼내오기
		//insertBoard.do
		SistAction action = map.get(cmd);  //new InsertBoardAction()
		//viewPage에 담아셔 이동
		String viewPage = action.proRequest(request, response);//insertBoard.jsp
		//new ListBoardAction()에 일을 맡김
		//객체를 생성해줌
		
		//클라이언트로부터 최초에 들어온 요청을 JSP/Servlet 내에서 원하는 자원으로 요청을 넘기는(보내는) 역할
		RequestDispatcher dispatcher 
		= request.getRequestDispatcher(viewPage);
		
		dispatcher.forward(request, response);
		//게시물안의 정보가 상태 유지가 되어서 jsp로 감
	}
}