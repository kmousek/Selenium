<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>java util</title>
</head>
<body>
 
      <!-- java에서의 util을 마찬가지로 import할 수 있다. -->
    <% 
        Random ran = new Random();
        int num = ran.nextInt(45)+1;
        out.print("<h1램덤>: "+ num + "</h1>");
    %> 
</body>
</html>