<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<a href="insertBoard.do">글등록</a>
	<hr>
	<table border="1" width="80%">
		<tr>
			<th>글번호</th>
			<th>글제목</th>
			<th>작성자</th>
			<th>등록일</th>
		</tr>
		
		<c:forEach var="b" items="${list }">
			<tr>
				<td>${b.no }</td>
				<td>
					<a href="detailBoard.do?no=${b.no }">${b.title }</a>
				</td>
				<td>${b.writer }</td>
				<td>${b.regdate }</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<br>
	<!-- total 페이지를 1부터 시작시켜서 totalPage끝까지 5 -->
	<c:forEach var="i" begin="1" end="${totalPage}">
		<a href="listBoard.do?pageNUM=${i}">${i}</a>&nbsp;&nbsp;
		<!-- -링크를 걸어주고 page번호를 받음 -->
	</c:forEach>
</body>
</html>



