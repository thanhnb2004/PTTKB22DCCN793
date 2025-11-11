<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký thành công</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<main class="content">
    <h1>Đăng ký thành viên thành công</h1>
    <div class="success">
        <p>Xin chào ${customer.fullName}! Tài khoản thành viên của bạn đã được tạo.</p>
    </div>
    <a class="button" href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a>
</main>
</body>
</html>
