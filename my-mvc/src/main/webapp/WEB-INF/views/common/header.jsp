<%@ page import="member.model.dto.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Member loginMember = (Member) session.getAttribute("loginMember");
	String msg = (String) session.getAttribute("msg");
	if(msg != null) session.removeAttribute("msg");
	
	String saveId= null;
	Cookie[] cookies = request.getCookies();
	if(cookies != null) {
		for(Cookie cookie : cookies) {
			if("saveId".equals(cookie.getName())) {
				saveId = cookie.getValue();
			}
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello MVC</title>
<script>
</script>
</head>
<body>
	<div id="container">
		<header>
			<h1>Hello MVC</h1>
			<% if(loginMember == null)  { %>
				<div class="login-container">
					<!-- 로그인 전 -->
			        <!-- 로그인폼 시작 -->
			        <form id="loginFrm" name="loginFrm" method="POST" action="<%= request.getContextPath() %>/member/memberLogin">
		            	<table>
			                <tr>
			                    <td><input type="text" name="memberId" id="memberId" placeholder="아이디" tabindex="1" value="<%= saveId != null ? saveId : "" %>"></td>
			                    <td><input type="submit" value="로그인" tabindex="3"></td>
			                </tr>
			                <tr>
			                    <td><input type="password" name="password" id="password" placeholder="비밀번호" tabindex="2"></td>
			                    <td></td>
			                </tr>
			                <tr>
			                    <td colspan="2">
			                        <input type="checkbox" name="saveId" id="saveId" <%= saveId != null ? "checked" : "" %>/>
			                        <label for="saveId">아이디저장</label>&nbsp;&nbsp;
			                        <input type="button" value="회원가입" onclick="location.href='#">
			                    </td>
			                </tr>
			            </table>
			        </form>
		        	<!-- 로그인폼 끝-->
		        <% } else { %>
	        		<!-- 로그인 성공시 -->
	        		<table id="login">
	        			<tbody>
	        				<tr>
	        					<td><%= loginMember.getMemberName() %>님, 안녕하세요.</td>
	        				</tr>
	        				<tr>
	        					<td>
	        						<input type="button" value="내정보보기" />
	        						<input type="button" value="로그아웃" onclick="location.href='<%= request.getContextPath() %>/member/memberLogout'"/>
	        					</td>
	        				</tr>
	        			</tbody>
	        		</table>
	        	<% } %>
	    	</div>
	    	
	    	<!-- 메인메뉴 시작 -->
	    	<nav>
		        <ul class="main-nav">
		            <li class="home"><a href="<%= request.getContextPath() %>">Home</a></li>
		            <li class="notice"><a href="#">공지사항</a></li>
		            <li class="board"><a href="#">게시판</a></li>
		        </ul>
		    </nav>
	    	<!-- 메인메뉴 끝-->
		</header>
		<section id="content">