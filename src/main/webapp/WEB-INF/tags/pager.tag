<%@tag pageEncoding="UTF-8"
%><%@tag import="java.lang.reflect.Method"
%><%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@attribute name="pager"	required="true" rtexprvalue="true" type="com.lavans.lacoder2.util.Pager"
%><%@attribute name="type"	required="true" rtexprvalue="true"
%><%@attribute name="cond"	required="false" rtexprvalue="true" type="java.util.Map"
%><%!
/**
 * No.1-50/200
 */
private String guide(){
	StringBuffer buf = new StringBuffer(30);
	// ページ制御 -----------------------------------
	if(pager.getTotalCount()==0){
		buf.append("該当するデータがありません。");
	}else{
		buf.append("No."+pager.getStartNumber() +"-"+ pager.getEndNumber() +"("+ pager.getTotalCount() +"件中)");
	}
	return buf.toString();
}
private static final int PAGELINK_MAX=10;
/**
 * Page 1 2 3
 */
private String link(){
	StringBuffer buf = new StringBuffer(30);
	buf.append("Page");
	String url = String.format("&nbsp;<a href=\"%s?%spageInfo.page=:page&pageInfo.rows=%s\">:title</a>", pager.getLinkUrl(), pager.getLinkParam(),pager.getPageInfo().getRows());
	// ページ制御 -----------------------------------
	int start = pager.getPageInfo().getPage()/PAGELINK_MAX*PAGELINK_MAX;
	// 1ページ目以外は前へのリンクをつける
	if(start>0){
		buf.append(url.replace(":page", String.valueOf(start-1)).replace(":title", "←"));
	}
	for(int i=start; i<pager.getTotalPage()+1; i++){
		if(i==pager.getPageInfo().getPage()){
			buf.append("&nbsp;"+ (i+1));
		}else if(i< start+PAGELINK_MAX){
			buf.append(url.replace(":page", String.valueOf(i)).replace(":title", String.valueOf(i+1)));
		}else{
			// MAXを超えた場合
			buf.append(url.replace(":page", String.valueOf(i)).replace(":title", "→"));
			break;	// ループを抜ける
		}
	}
	return buf.toString();
}
%><%
String result="";
if(type.equals("guide")){
	result=guide();
}else if(type.equals("link")){
	result=link();
}
%><%= result %>
