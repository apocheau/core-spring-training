<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<c:url value="/styles/springsource.css"/>" type="text/css"/>
	<title>Security Lab: Account Details</title>
</head>

<body>
<div id="main_wrapper">

<h1>Account Details</h1>

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
	
	
	<%-- TODO-09: 	Only viewers should be allowed to view beneficiaries information. 
					Hide the table row below from all users who do not have the "VIEWER" role --%>
	
	
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
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</td></tr>
</table>

	
	<%-- TODO-08: 	- Using 'vince', try go to the 'account details' page and then click on 'Edit accounts'.
					As you can see, access to this page is denied because 'vince' does not have the EDITOR role.
					It would be more elegant to hide this link from 'vince' and only show it to editors.
					- Using the 'security' tag library, hide the 'Edit Account' link unless a
	              	user has permission to access that page. 
	              	- Try logging in as a user with and without the editor role and verify that you see 
	              	the correct behavior.
	              --%>
	              
	              
	<p><a href="editAccount?entityId=${account.entityId}">Edit Account</a></p>

<!--  Don't show logout unless someone is logged in -->
<security:authentication property="principal" var="principal" scope="page" />
<c:if test="${principal != null}">
	<p><a href="<c:url value="/j_spring_security_logout"/>">Logout</a> (${principal.username})</p>
</c:if>

</div>
</body>

</html>
