<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="${pageContext.request.contextPath}/as/main/Top.html">lacoder2-generator</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li id="Projects"><a href="${pageContext.request.contextPath}/as/main/Top.html">Projects</a></li>
              <li id="DBUtils"><a href="${pageContext.request.contextPath}/as/dbutils/DBMain.html">DBUtils</a></li>
              <li id="StatsSql"><a href="${pageContext.request.contextPath}/as/dbutils/StatsSql.html">StatsSql</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
<!--  -->
<div style="height: 60px;"></div>

<script type="text/javascript">
$("#${param.title}").addClass("active");
</script>



