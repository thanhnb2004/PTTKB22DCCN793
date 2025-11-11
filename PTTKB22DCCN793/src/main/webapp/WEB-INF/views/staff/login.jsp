<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập nhân viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<main class="content">
    <h1>Đăng nhập nhân viên</h1>
    <jsp:include page="/WEB-INF/views/common/messages.jspf" />
    <form method="post" class="form-grid">
        <label for="username">Tên đăng nhập</label>
        <input type="text" id="username" name="username" value="${param.username}" required>

        <label for="password">Mật khẩu</label>
        <input type="password" id="password" name="password" required>

        <div class="action-row">
            <button type="submit" class="button">Đăng nhập</button>
            <a class="button-secondary" href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a>
        </div>
    </form>
</main>
</body>
</html>
