<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Nhập sản phẩm kho</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/styles.css">
</head>
<body>
<main class="content">
    <h1>Nhập sản phẩm kho</h1>
    <jsp:include page="/WEB-INF/views/common/messages.jspf" />

    <form method="get" class="inline-group" action="${pageContext.request.contextPath}/warehouse/import">
        <div>
            <label for="supplierKeyword">Tìm nhà cung cấp</label>
            <input type="text" id="supplierKeyword" name="supplierKeyword" value="${supplierKeyword}">
        </div>
        <div>
            <label for="productKeyword">Tìm sản phẩm</label>
            <input type="text" id="productKeyword" name="productKeyword" value="${productKeyword}">
        </div>
        <div style="align-self: end;">
            <button type="submit" class="button">Tìm kiếm</button>
        </div>
    </form>

    <form method="post">
        <section>
            <h2>Chọn nhà cung cấp</h2>
            <div class="inline-group">
                <div>
                    <label for="supplierId">Nhà cung cấp hiện có</label>
                    <select id="supplierId" name="supplierId">
                        <option value="">-- Chọn nhà cung cấp --</option>
                        <c:forEach var="supplier" items="${suppliers}">
                            <option value="${supplier.id}" <c:if test="${formSupplier.id == supplier.id}">selected</c:if>>
                                ${supplier.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <p>Hoặc thêm mới nhà cung cấp:</p>
                    <label for="newSupplierName">Tên</label>
                    <input type="text" id="newSupplierName" name="newSupplierName" value="${formSupplier.name}">
                    <label for="newSupplierEmail">Email</label>
                    <input type="email" id="newSupplierEmail" name="newSupplierEmail" value="${formSupplier.email}">
                    <label for="newSupplierPhone">Số điện thoại</label>
                    <input type="text" id="newSupplierPhone" name="newSupplierPhone" value="${formSupplier.phoneNumber}">
                    <label for="newSupplierAddress">Địa chỉ</label>
                    <textarea id="newSupplierAddress" name="newSupplierAddress">${formSupplier.address}</textarea>
                </div>
            </div>
        </section>

        <section>
            <h2>Danh sách sản phẩm nhập</h2>
            <table class="table product-table" id="productTable">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Sản phẩm hiện có</th>
                    <th>Thông tin sản phẩm mới (nếu có)</th>
                    <th>Số lượng</th>
                    <th>Đơn giá (VND)</th>
                    <th>Ghi chú</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${productRequests}" varStatus="status">
                    <tr class="product-row">
                        <td class="row-number">${status.index + 1}</td>
                        <td>
                            <select name="productId">
                                <option value="">-- Chọn sản phẩm --</option>
                                <c:forEach var="product" items="${products}">
                                    <option value="${product.id}" <c:if test="${item.productId == product.id}">selected</c:if>>
                                        ${product.name}
                                    </option>
                                </c:forEach>
                            </select>
                            <small>Chọn sản phẩm đã tồn tại (nếu có)</small>
                        </td>
                        <td>
                            <label>Tên sản phẩm mới</label>
                            <input type="text" name="productName" value="${item.productName}">
                            <label>Loại</label>
                            <input type="text" name="productType" value="${item.productType}">
                            <label>Đơn vị</label>
                            <input type="text" name="unit" value="${item.unit}">
                        </td>
                        <td>
                            <input type="number" min="1" name="quantity" value="${item.quantity > 0 ? item.quantity : ''}" required>
                        </td>
                        <td>
                            <input type="number" min="0" step="0.01" name="price" value="${item.price}" required>
                        </td>
                        <td>
                            <textarea name="note">${item.note}</textarea>
                            <button type="button" class="remove-row-button" onclick="removeRow(this)">Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <button type="button" class="add-row-button" onclick="addRow()">Thêm sản phẩm</button>
        </section>

        <div class="action-row">
            <button type="submit" class="button">Lưu phiếu nhập</button>
            <a class="button-secondary" href="${pageContext.request.contextPath}/index.jsp">Hủy</a>
        </div>
    </form>
</main>

<template id="productRowTemplate">
    <tr class="product-row">
        <td class="row-number"></td>
        <td>
            <select name="productId">
                <option value="">-- Chọn sản phẩm --</option>
                <c:forEach var="product" items="${products}">
                    <option value="${product.id}">${product.name}</option>
                </c:forEach>
            </select>
            <small>Chọn sản phẩm đã tồn tại (nếu có)</small>
        </td>
        <td>
            <label>Tên sản phẩm mới</label>
            <input type="text" name="productName">
            <label>Loại</label>
            <input type="text" name="productType">
            <label>Đơn vị</label>
            <input type="text" name="unit">
        </td>
        <td>
            <input type="number" min="1" name="quantity" required>
        </td>
        <td>
            <input type="number" min="0" step="0.01" name="price" required>
        </td>
        <td>
            <textarea name="note"></textarea>
            <button type="button" class="remove-row-button" onclick="removeRow(this)">Xóa</button>
        </td>
    </tr>
</template>

<script>
    function addRow() {
        const template = document.getElementById('productRowTemplate');
        const tbody = document.querySelector('#productTable tbody');
        const clone = template.content.cloneNode(true);
        tbody.appendChild(clone);
        updateRowNumbers();
    }

    function removeRow(button) {
        const row = button.closest('tr');
        const tbody = document.querySelector('#productTable tbody');
        if (tbody.children.length > 1) {
            row.remove();
        } else {
            row.querySelectorAll('input, textarea, select').forEach(el => el.value = '');
            row.querySelector('select').selectedIndex = 0;
        }
        updateRowNumbers();
    }

    function updateRowNumbers() {
        document.querySelectorAll('#productTable tbody tr').forEach((row, index) => {
            const cell = row.querySelector('.row-number');
            if (cell) {
                cell.textContent = index + 1;
            }
        });
    }

    updateRowNumbers();
</script>
</body>
</html>
