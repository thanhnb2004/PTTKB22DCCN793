<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hệ thống quản lý siêu thị điện máy</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<header class="header">
    <h1>Hệ thống quản lý siêu thị điện máy</h1>
    <nav>
        <ul>
            <li><a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a></li>
            <li><a href="${pageContext.request.contextPath}/customer/register">Đăng ký thành viên</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.loggedStaff}">
                    <li><a href="${pageContext.request.contextPath}/warehouse/import">Nhập sản phẩm kho</a></li>
                    <li class="welcome">Xin chào, ${sessionScope.loggedStaff.fullName} (${sessionScope.loggedStaff.role})</li>
                    <li>
                        <form action="${pageContext.request.contextPath}/staff/logout" method="post">
                            <button type="submit">Đăng xuất</button>
                        </form>
                    </li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/staff/login">Đăng nhập nhân viên</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</header>
<main class="content">
    <section>
        <h2>Chức năng nổi bật</h2>
        <div class="card-container">
            <article class="card">
                <h3>Đăng ký thành viên</h3>
                <p>Khách hàng đăng ký thông tin để trở thành thành viên và nhận ưu đãi.</p>
                <a class="button" href="${pageContext.request.contextPath}/customer/register">Thực hiện ngay</a>
            </article>
            <article class="card">
                <h3>Nhập sản phẩm kho</h3>
                <p>Nhân viên kho tìm kiếm nhà cung cấp, sản phẩm và lập phiếu nhập.</p>
                <a class="button" href="${pageContext.request.contextPath}/warehouse/import">Đi tới chức năng</a>
            </article>
        </div>
    </section>
</main>
<footer class="footer">
    <p>&copy; 2024 Siêu thị điện máy.</p>
</footer>
</body>
</html>
