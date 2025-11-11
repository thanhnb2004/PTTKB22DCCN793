<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Phiếu nhập kho</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<main class="content">
    <h1>Phiếu nhập kho</h1>
    <div class="success">
        <p>Đã lưu phiếu nhập kho #${invoice.id} thành công.</p>
    </div>
    <section>
        <h2>Thông tin chung</h2>
        <p><strong>Nhà cung cấp:</strong> ${invoice.supplier.name}</p>
        <p><strong>Nhân viên kho:</strong> ${invoice.warehouseStaff.fullName}</p>
        <p><strong>Thời gian:</strong> ${invoice.importDate}</p>
        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${invoice.total}" type="currency" currencySymbol=""/> VND</p>
    </section>
    <section>
        <h2>Danh sách sản phẩm</h2>
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>Tên sản phẩm</th>
                <th>Số lượng</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
                <th>Ghi chú</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${invoice.products}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${item.product.name}</td>
                    <td>${item.quantity}</td>
                    <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol=""/></td>
                    <td><fmt:formatNumber value="${item.price * item.quantity}" type="currency" currencySymbol=""/></td>
                    <td>${item.note}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
    <a class="button" href="${pageContext.request.contextPath}/warehouse/import">Lập phiếu mới</a>
    <a class="button-secondary" href="${pageContext.request.contextPath}/index.jsp">Về trang chủ</a>
</main>
</body>
</html>
