<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<%@ page import="java.util.*,java.sql.*,javax.sql.*,javax.naming.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<body>
<%

Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
boolean result = false;
javax.naming.Context envCtx = new InitialContext();
DataSource dataSource = (DataSource) envCtx.lookup("java:jboss/db2/jdbc/urbtix");

try {
    conn = dataSource.getConnection();
	stmt = conn.createStatement();
	rs = stmt.executeQuery("select count(*) from TBL_PRINTLG");
	if(rs.next()) {
		System.out.println(rs.getInt(1));
		result = true;
	}
} catch (Exception e) {
    e.printStackTrace();
}finally{
	stmt.close();
	rs.close();
	conn.close();
}



%>
</body>
</html>