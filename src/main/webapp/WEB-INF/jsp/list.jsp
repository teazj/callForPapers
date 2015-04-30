<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>InsertTitleMessage</title>
</head>
<body>
	<h3>유저 리스트</h3>
	<c:forEach var="user" items="${userlist}">
	username : ${user.name } ,  user_message : ${user.message } <br>
	</c:forEach>

	<h3>새로운 유저와 메시지</h3>
	<form action="/add">
		name : <input type="text" name="name"><br> message : <input
			type="text" name="message"><br> <input type="submit">
	</form>

</body>
</html>