<%@tag import="com.lavans.lacoder2.lang.StringUtils"%>
<%@tag import="java.lang.reflect.Method"%>
<%@tag pageEncoding="UTF-8"%>
<%!
@SuppressWarnings("unused")
private String getValue(Object item, String attr){
	// attrの指定が無ければtoString()を返す。
	if(StringUtils.isEmpty(attr)){
		return item.toString();
	}
	// getterメソッドを探してinvoke
	try{
		String getterName = "get"+StringUtils.capitalize(attr);
		Method getter = item.getClass().getMethod(getterName);
		getter.setAccessible(true);
		String value = getter.invoke(item).toString();

		return value;
	}catch(Exception e){
		e.printStackTrace();
	}

	return "";
}
%>