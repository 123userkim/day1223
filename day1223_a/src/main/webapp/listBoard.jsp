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
	
	<!-- x단추누르면 없어짐 -->
	<form action="listBoard.do" method="post">
		<select name="searchCloum">
			<option value="writer">작성자</option>
			<option value="title">제목</option>
			<option value="content">내용</option>
		</select>
		<input type="search" name = "keyword">
		<input type="submit" value="검색">
	</form>
	<table border="1" width="80%">
		<tr>
			<th><a href="listBoard.do?orderColum=no">글번호</a></th>
			<th><a href="listBoard.do?orderColum=title">글제목</a></th>
			<th><a href="listBoard.do?orderColum=writer">작성자</a></th>
			<th><a href="listBoard.do?orderColum=regdate">등록일</a></th>
		</tr>
		
		<c:forEach var="b" items="${list }">
			<tr>
				<td>${b.no }</td>
				<td>
				<!-- 만약 답글이라면 -->
					<c:if test="${b.b_level >0 }">
					<c:forEach var="i" begin="1" end="${b.b_level }">
						&nbsp;&nbsp;					
					</c:forEach>
					<img  src="re.png">
					</c:if>					
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



