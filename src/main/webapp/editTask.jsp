<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Task</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/tailwind.output.css"/>
</head>
<body class="bg-gray-300 dark:bg-gray-900">

<%@include file="WEB-INF/views/includes/header.jsp" %>
<%@include file="WEB-INF/views/includes/sidebar.jsp" %>

<main id="main" class="mt-32 h-screen px-4">
    <h2 class="my-6 text-4xl font-semibold text-center font-poppins tracking-widest text-gray-700 dark:text-gray-200">
        <span class="text-primary-100 dark:text-orange">Edit</span> Task
    </h2>

    <c:if test="${not empty errors}">
        <div class="bg-red-500 text-white p-4 rounded-lg shadow-md mb-4">
            <h3 class="font-semibold text-lg">Please fix the following errors:</h3>
            <ul class="list-disc list-inside mt-2">
                <c:forEach var="error" items="${errors}">
                    <li class="mt-1">${error}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/tasks" method="post" class="my-6">
        <!-- Hidden fields for action and task ID -->
        <input type="hidden" name="action" value="edit"/>
        <input type="hidden" name="taskId" value="${taskToEdit.id}"/>

        <div class="grid grid-cols-2 gap-4">
            <!-- Title -->
            <div>
                <label>Title</label>
                <input type="text" name="title" value="${taskToEdit.title}" class="border p-2 rounded w-full"/>
            </div>

            <!-- Assigned To -->
            <div>
                <label>Assigned To</label>
                <select name="assignedTo" class="border p-2 rounded w-full">
                    <c:choose>
                        <c:when test="${sessionScope.isManager}">
                            <c:forEach var="user" items="${users}">
                                <option value="${user.id}"
                                        <c:if test="${user.id == taskToEdit.assignedTo.id}">selected</c:if>>${user.username}</option>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <option value="${sessionScope.loggedInUser.id}"
                                    selected>${sessionScope.loggedInUser.username}</option>
                        </c:otherwise>
                    </c:choose>
                </select>
            </div>

            <!-- Start Date -->
            <div>
                <label>Start Date</label>
                <input type="date" name="startDate" value="${taskToEdit.startDate}" min="2024-01-01"
                       class="border p-2 rounded w-full"/>
            </div>

            <!-- End Date -->
            <div>
                <label>End Date</label>
                <input type="date" name="end_date" value="${taskToEdit.endDate}" min="2024-01-01"
                       class="border p-2 rounded w-full"/>
            </div>

            <!-- Tags -->
            <div>
                <label>Tags</label>
                <input type="text" name="tags"
                       value="<c:forEach var='tag' items='${taskToEdit.tags}' varStatus='status'>${tag.name}<c:if test='${!status.last}'>, </c:if></c:forEach>"
                       class="border p-2 rounded w-full"/>

            </div>
        </div>

        <!-- Task Status -->
        <div>
            <label>Task Status</label>
            <select name="taskStatus" class="border p-2 rounded w-full">
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="DONE">DONE</option>
            </select>

        </div>

        <button type="submit"
                class="px-4 py-2 my-6 bg-orange rounded text-white hover:bg-primary-100 focus:outline-none transition-colors">
            Update Task
        </button>
    </form>
</main>

<%@include file="WEB-INF/views/includes/footer.jsp" %>

<script src="./assets/js/navigation.js"></script>
<script src="./assets/js/navbar.js"></script>
<script src="./assets/js/theme.js"></script>
</body>
</html>
