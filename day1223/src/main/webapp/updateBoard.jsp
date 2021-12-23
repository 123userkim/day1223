<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>게시물 등록</h2>
	<hr>
	<form action="updateBoardOK.do" method="post"
	enctype="multipart/form-data">
		<input type="hidden" name="no" value="${b.no }">
		<input type="hidden" name="fname" value="${b.fname }">
		<input type="hidden" name="fsize" value="${b.fsize }">
		글제목 : <input type="text" name="title" value="${b.title }"><br>
		작성자 : ${b.writer }<br>
		글암호 : <input type="password" name="pwd"><br>
		글내용 : <br>
		<textarea rows="10" cols="80" name="content">${b.content }</textarea><br>
		${b.fname }(${b.fsize }bytes)<br>
		파일 : <input type="file" name="uploadFile"><br>
		<input type="submit" value="수정">
		<input type="reset" value="취소">
		
	</form>
</body>
</html>











