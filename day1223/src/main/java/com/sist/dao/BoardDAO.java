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
	public int getTotalRecord() {
		int n = 0;
		String sql = "select count(*) from board";
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
		String sql = "insert into board values(?,?,?,?,?,sysdate,0,?,?)";
		int no = getNextNo();
		try {
			Connection conn = ConnectionProvider.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			pstmt.setString(2, b.getTitle());
			pstmt.setString(3, b.getWriter());
			pstmt.setString(4, b.getPwd());
			pstmt.setString(5, b.getContent());
			pstmt.setString(6, b.getFname());
			pstmt.setLong(7, b.getFsize());
			
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
	public ArrayList<BoardVO> listBoard(int pageNUM){
		//페이지를 보여주는 것은 리스트업을 할 때 필요하기 때문에
		totalRecord = getTotalRecord();
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
			String sql = "select no, title, writer, pwd,content, regdate, hit, fname, fsize from( "
					+ "			select rownum n, no, title, writer, pwd,content, regdate, hit, fname, fsize"
					+ "			from(select * from board order by no desc)) "
					+ "			where n between ? and ?";
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
						rs.getLong(9)));	//파일크기
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
						rs.getLong(9));		//파일크기
			}
			ConnectionProvider.close(conn, pstmt, rs);
			
		}catch (Exception e) {
			System.out.println("예외발생:"+e.getMessage());
		}
		return b;
	}	
}