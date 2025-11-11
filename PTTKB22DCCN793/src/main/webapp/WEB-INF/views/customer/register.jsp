<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký thành viên</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<main class="content">
    <h1>Đăng ký thành viên</h1>
    <jsp:include page="/WEB-INF/views/common/messages.jspf" />
    <form method="post" class="form-grid">
        <label for="fullName">Họ tên</label>
        <input type="text" id="fullName" name="fullName" value="${param.fullName}" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="${param.email}" required>

        <label for="password">Mật khẩu</label>
        <input type="password" id="password" name="password" minlength="6" required>

        <label for="phoneNumber">Số điện thoại</label>
        <input type="tel" id="phoneNumber" name="phoneNumber" value="${param.phoneNumber}">

        <label for="dateOfBirth">Ngày sinh</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" value="${param.dateOfBirth}">

        <label for="address">Địa chỉ</label>
        <textarea id="address" name="address">${param.address}</textarea>

        <div class="action-row">
            <button type="submit" class="button">Đăng ký</button>
            <a class="button-secondary" href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a>
        </div>
    </form>
</main>
</body>
</html>
