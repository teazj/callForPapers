<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%
    String thisURL = request.getRequestURI();
    UserService userService = UserServiceFactory.getUserService();
    if (request.getUserPrincipal() != null) {
%>
    <a href="<%= userService.createLogoutURL("/") %>">sign out</a>
<%
    } else {
%>
    <a href="<%= userService.createLoginURL(thisURL) %>">sign in</a>
<%
    }
%>

<%=
    request.getUserPrincipal().getName()
%>