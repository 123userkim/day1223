package com.sist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.sist.db.ConnectionProvider;
import com.sist.vo.BoardVO;

public class BoardDAO {
	
	public static int pageSize = 10; //한 화면에 보여줄 레코드의 수
	public static int totalRecord; //전체 레코드의 수
	public static int totalPage; //전체 페이지의 수
	
	//전체 레코드의 수를 반환하는 메소드 정의
	public int getTotalRecord(String searchCloum, String keyword ) {
		int n = 0;
		//무조건 전체 레코드가 아니라 검색을 했다면 검색한 레코드의 숫자로 바꿔줘야함 
		String sql = "select count(*) from board ";
		if(keyword != null) {
			//검색어가 있다면 아래의 sql문을 붙이기
			sql += " where "+ searchCloum +" like '%"+keyword+"%'";
		}
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				n = rs.getInt(1);
			}
			ConnectionProvider.close(conn, pstmt,rs);
		}catch(Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return n;
	}
		
	public void updateHit(int no) {
		String sql = "update board set hit = hit + 1 where no=?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
	}
	
	public int getNextNo() {
		int no = 0;
		String sql = "select nvl(max(no),0)+1 from board";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				no = rs.getInt(1);
			}
			ConnectionProvider.close(conn, pstmt, rs);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return no;
	}
	
	public int insertBoard(BoardVO b) {
		int re = -1;
		String sql = "insert into board values(?,?,?,?,?,sysdate,0,?,?,?,?,?)";
		//int no = getNextNo();
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getNo());
			pstmt.setString(2, b.getTitle());
			pstmt.setString(3, b.getWriter());
			pstmt.setString(4, b.getPwd());
			pstmt.setString(5, b.getContent());
			pstmt.setString(6, b.getFname());
			pstmt.setLong(7, b.getFsize());
			pstmt.setInt(8, b.getB_ref());
			pstmt.setInt(9, b.getB_level());
			pstmt.setInt(10, b.getB_step());
			
			re = pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return re;
	}
	public int updateBoard(BoardVO b) {
		int re = -1;
		try {
			Connection conn = ConnectionProvider.getConnection();
			String sql = "update board set title=?,content=?,fname=?,fsize=? where no=? and pwd=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getContent());
			pstmt.setString(3, b.getFname());
			pstmt.setLong(4, b.getFsize());
			pstmt.setInt(5, b.getNo());
			pstmt.setString(6, b.getPwd());
			re = pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return re;
	}
	public int deleteBoard(int no, String pwd) {
		int re = -1;
		try {
			Connection conn = ConnectionProvider.getConnection();
			String sql = "delete board where no=? and pwd=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.setString(2, pwd);
			re = pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return re;
	}
	//페이지번호를 갖도록 함
	public ArrayList<BoardVO> listBoard(int pageNUM, String orderColum, String searchCloum, String keyword){
		//페이지를 보여주는 것은 리스트업을 할 때 필요하기 때문에
		totalRecord = getTotalRecord(searchCloum,keyword);
		//페이지수가 생각보다 적게 나옴 : 둘다 int라서 Math.ceil안 먹음 => 둘 둥 하나는 double로 바꾸기
		totalPage = (int)Math.ceil(totalRecord/(double)pageSize);
		System.out.println("전체 레코드 수 :"+totalRecord);
		System.out.println("전체 페이지 수 :"+totalPage);
		
		int start =(pageNUM-1)*BoardDAO.pageSize+1;
		int end = start +BoardDAO.pageSize-1;
		System.out.println("start"+start);
		System.out.println("end"+end);
		
		ArrayList<BoardVO> list = new ArrayList<BoardVO>();
		try {
			Connection conn = ConnectionProvider.getConnection();
			
			//keyword가 0이 아닌 게 오면 연결해주기
			String sql2="select * from board ";
			if(keyword != null) {
				sql2 += " where "+ searchCloum +" like '%"+keyword+"%'";
			}
			
			String sql = "select no, title, writer, pwd,content, regdate, hit, fname, fsize, b_ref, b_level, b_step from( "
					+ "			select rownum n, no, title, writer, pwd,content, regdate, hit, fname, fsize, b_ref, b_level, b_step"
					+ "			from("+sql2+" order by b_ref desc, b_step)) "
					+ "			where n between ? and ?";
			//컬럼이름엔 ?불간읗
			if(orderColum != null) {
				sql = "select no, title, writer, pwd,content, regdate, hit, fname, fsize, b_ref, b_level, b_step from( "
						+ "			select rownum n, no, title, writer, pwd,content, regdate, hit, fname, fsize, b_ref, b_level, b_step"
						+ "			from("+sql2+" order by "+orderColum+")) "
						+ "			where n between ? and ?";
			}
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(new BoardVO(
						rs.getInt(1), 		//글번호
						rs.getString(2), 	//글제목
						rs.getString(3), 	//작성자
						rs.getString(4), 	//글암호
						rs.getString(5), 	//글내용
						rs.getDate(6),		//등록일
						rs.getInt(7),		//조회수
						rs.getString(8), 	//파일명
						rs.getLong(9),		//파일크기
						rs.getInt(10),		//b_ref
						rs.getInt(11),		//b_level
						rs.getInt(12)		////b_step		
						));
			}
			ConnectionProvider.close(conn, pstmt, rs);
			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return list;
	}
	public BoardVO getBoard(int no) {
		BoardVO b = null;
		try {
			Connection conn = ConnectionProvider.getConnection();
			String sql = "select * from board where no=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				b = new BoardVO(
						rs.getInt(1), 		//글번호
						rs.getString(2), 	//글제목
						rs.getString(3), 	//작성자
						rs.getString(4), 	//글암호
						rs.getString(5), 	//글내용
						rs.getDate(6),		//등록일
						rs.getInt(7),		//조회수
						rs.getString(8), 	//파일명
						rs.getLong(9),		//파일크기
						rs.getInt(10),		//b_ref
						rs.getInt(11),		//b_level
						rs.getInt(12)		////b_step		
						);		
			}
			ConnectionProvider.close(conn, pstmt, rs);
			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return b;
	}

	public void updateStep(int b_ref, int b_step) {
		// TODO Auto-generated method stub
		String sql = "update board set b_step = b_step+1 where b_ref = ? and b_step > ?";
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b_ref);
			pstmt.setInt(2, b_step);
			pstmt.executeUpdate();
			ConnectionProvider.close(conn, pstmt);
			
		}catch(Exception e) {
			System.out.println("예외발생:"+e.getMessage());
			
		}
	}
}