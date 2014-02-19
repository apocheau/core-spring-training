<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href='<spring:url value="/styles/springsource.css"/>' type="text/css"/>
	<title>mvc-rest-solution: Account Details</title>
</head>

<body>
<div id="main_wrapper">

<h1>Account Details</h1>
<spring:url value='/accounts/{id}.json' var="jsonUrl">
	<spring:param name="id" value="${account.entityId}"/>
</spring:url>
<p><a href="${jsonUrl}">View as JSON</a></p>
<table>
	<tr><td>
		<table>
			<tr>
				<td>Account:</td>
				<td>${account.number}</td>
			</tr>
			<tr>
				<td>Name:</td>
				<td>${account.name}</td>
			</tr>
		</table>
	</td></tr>
	<tr><td>
		<table>
			<thead>
				<tr>
					<td>Beneficiaries:</td>
				</tr>
				<tr>
					<td>Name</td>
					<td>Allocation Percentage</td>
					<td>Savings</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${account.beneficiaries}" var="beneficiary">
					<tr>
						<td>${beneficiary.name}</td>
						<td>${beneficiary.allocationPercentage}</td>
						<td>${beneficiary.savings}</td>
						<td>
							<spring:url value="/accounts/{accId}/beneficiaries/{benName}" var="deleteUrl">
								<spring:param name="accId"   value="${account.entityId}"/>
								<spring:param name="benName" value="${beneficiary.name}"/>
							</spring:url>
							<form action="${deleteUrl}" method="post">
								<input type="hidden" name="_method" value="delete" />
								<input type="submit" value="Remove" />
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</td></tr>
</table>
<h2>Add a new beneficiary</h2>
<spring:url value='/accounts/{id}/beneficiaries' var="addUrl">
	<spring:param name="id" value="${account.entityId}"/>
</spring:url>
<form action="${addUrl}" method="post">
	Name: <input type="text" name="beneficiaryName" />
	<input type="submit" value="Add" />
</form>
</div>
</body>

</html>