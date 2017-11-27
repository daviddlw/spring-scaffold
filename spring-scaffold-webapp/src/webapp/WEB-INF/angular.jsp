<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Hello AngularJS</title>
<script
	src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>
</head>
<body>

	<div ng-app="">
		<span>name: </span><input id="name" ng-model="name" value="" /> <br />
		<span>age: </span><input id="age" ng-model="age" value="" /> <br />
		<span><strong>result: </strong>{{name + "_" + age}}</span>
	</div>
</body>
</html>