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

//do��� �ϴ� ��� �Ϳ� ���켭 ��Ʈ�ѷ��� �۵���
@WebServlet("*.do")
public class SistController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//����ڰ� ��û�� ���񽺸�, ��ó�����
	HashMap<String, SistAction> map;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SistController() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    //�� �ѹ� �۵��ϴ� init�̶�� �޼ҵ带 �������̵��ؼ� ������ �о��
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		//super.init(config);
		map = new HashMap<String, SistAction>();
		try {
			//������θ� �˱�
			String path = config.getServletContext().getRealPath("WEB-INF");
	    	//������ �о����
			FileReader fr = new FileReader(path + "/sist.properties");
			//key value����
			Properties prop = new Properties();
			//���� ���Ϸκ��� ������Ƽ�� ������ ������ �� ����
			prop.load(fr);
			Iterator keyList = prop.keySet().iterator();
			while(keyList.hasNext()) {
				String key = (String)keyList.next();
				String clsName = prop.getProperty(key);
				SistAction obj = (SistAction)Class.forName(clsName).newInstance();
				map.put(key, obj);
			}
		}catch (Exception e) {
			System.out.println("���ܹ߻�:"+e.getMessage());
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		proRequest(request, response);
		//���� �ִ� ���񽺴� �����ϱ�
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		proRequest(request, response);
		//���� �ִ� ���񽺴� �����ϱ�
	}
	//get, post ��� � �� ����ص� proRequestȣ���ϱ�
	//����ڰ� ��û�� ����, ������ ������ �Ű������� �޾ƿ�, ����ó���� �׻� ����
	public void proRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String uri = request.getRequestURI();//����������Ʈ�� �������̸��� ���� ����
		/*
		 	/day1222/listBoard.do
		*/
		
		String cmd = uri.substring( uri.lastIndexOf("/") + 1 ); 
		//���񽺸� ���� -> ���������� ã�� , /�� ������ : +1
		//���񽺸� ���� ��ó���� action map�� ��������
		//insertBoard.do
		SistAction action = map.get(cmd);  //new InsertBoardAction()
		//viewPage�� ��Ƽ� �̵�
		String viewPage = action.proRequest(request, response);//insertBoard.jsp
		//new ListBoardAction()�� ���� �ñ�
		//��ü�� ��������
		
		//Ŭ���̾�Ʈ�κ��� ���ʿ� ���� ��û�� JSP/Servlet ������ ���ϴ� �ڿ����� ��û�� �ѱ��(������) ����
		RequestDispatcher dispatcher 
		= request.getRequestDispatcher(viewPage);
		
		dispatcher.forward(request, response);
		//�Խù����� ������ ���� ������ �Ǿ jsp�� ��
	}
}