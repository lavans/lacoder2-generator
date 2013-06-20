<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--■■■ロイター　START■■■-->
<!--▽ヘッドラインニュース-->
<table style="border-style: none; border-collapse: separate; border-spacing: 1px; width: 100%;" >
	<c:forEach var="item" items="${out.list}">
		<tr>
			<th><fmt:formatDate value="${item.lastUpdate}" pattern="MM/dd HH:mm" />&nbsp;</th>
			<td>
				<a href="${burl}${daid}&qidd=F:${param.detailview}%23seqNo:${item.newsId}&qidh=F:${param.headlineview}%23seqNo:${item.newsId}%23GO_BEFORE:%23BEFORE:0">
					<c:out value="${item.title}" />
				</a>
			</td>
		</tr>
	</c:forEach>
</table>
<!--△ヘッドラインニュース-->
<!--■■■ロイター　END■■■-->
