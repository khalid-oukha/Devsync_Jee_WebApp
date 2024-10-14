<%@ page import="java.util.List" %>
<%@ page import="org.example.entities.Task" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<%
    List<Task> todoTasks = (List<Task>) request.getAttribute("todoTasks");
%>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management</title>
    <link rel="stylesheet" href="./assets/css/tailwind.output.css"/>
</head>
<body class="bg-gray-300 dark:bg-gray-900">

<%@include file="WEB-INF/views/includes/header.jsp" %>

<%@include file="WEB-INF/views/includes/sidebar.jsp" %>

<main id="main" class="mt-32 h-screen px-4">
    <h2 class="my-6 text-4xl font-semibold text-center font-poppins tracking-widest text-gray-700 dark:text-gray-200">
        <span class="text-primary-100 dark:text-orange">Tasks</span> Board
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
    <div class="h-screen p-2">
        <div class="grid lg:grid-cols-4 md:grid-cols-4 sm:grid-cols-2 gap-5">
            <!-- To-do -->
            <div class="bg-white rounded px-2 py-2">
                <!-- board category header -->
                <div class="flex flex-row justify-between items-center mb-2 mx-1">
                    <div class="flex items-center">
                        <h2 class="bg-red-100 text-sm w-max px-1 rounded mr-2 text-gray-700">To-do</h2>
                        <p class="text-gray-400 text-sm">${todoTasks.size()}</p>
                    </div>
                    <div class="flex items-center text-gray-300">
                        <p class="mr-2 text-2xl">---</p>
                        <p class="text-2xl">+</p>
                    </div>
                </div>

                <div class="grid grid-rows-2 gap-2">
                    <c:forEach var="task" items="${todoTasks}">
                        <div class="p-2 rounded shadow-sm border-gray-100 border-2">
                            <h3 class="text-sm mb-3 text-gray-700">${task.title}</h3>
                            <p class="bg-red-100 text-xs w-max p-1 rounded mr-2 text-gray-700">To-do</p>


                            <div class="flex flex-row items-center mt-2">
                                <div class="bg-gray-300 rounded-full w-4 h-4 mr-3"></div>
                                <a href="#" class="text-xs text-gray-500">${task.assignedTo.username}</a>
                            </div>
                            <p class="text-xs text-gray-500 mt-2">${task.startDate}</p>
                            <p class="text-xs text-gray-500 mt-2">${task.endDate}</p>
                            <!-- Display tags -->
                            <div class="mt-1">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-gray-200 text-xs px-2 py-1 rounded-full mr-1">${tag}</span>
                                </c:forEach>
                            </div>

                            <div class="flex items-center space-x-4 text-sm">

                                <a href="tasks?editTaskId=${task.id}">
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-purple-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Edit">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path>
                                        </svg>
                                    </button>
                                </a>

                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Delete">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                  clip-rule="evenodd"></path>
                                        </svg>
                                    </button>
                                </form>
                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="requestModification"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="requestModification">
                                        <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" id="Outline"
                                             viewBox="0 0 24 24"
                                             width="5"
                                             height="5">
                                            <path d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z"/>
                                            <path d="M12,5a1,1,0,0,0-1,1v8a1,1,0,0,0,2,0V6A1,1,0,0,0,12,5Z"/>
                                            <rect x="11" y="17" width="2" height="2" rx="1"/>
                                        </svg>
                                    </button>

                                </form>

                            </div>
                        </div>

                    </c:forEach>
                </div>
                <div class="flex flex-row items-center text-gray-300 mt-2 px-1">
                    <a href="tasks/new" class="rounded mr-2 text-2xl">+</a>
                    <a href="tasks/new" class="pt-1 rounded text-sm">New</a>
                </div>
            </div>

            <!-- WIP Kanban -->
            <div class="bg-white rounded px-2 py-2">
                <!-- board category header -->
                <div class="flex flex-row justify-between items-center mb-2 mx-1">
                    <div class="flex items-center">
                        <h2 class="bg-yellow-100 text-sm w-max px-1 rounded mr-2 text-gray-700">IN_PROGRESS</h2>
                        <p class="text-gray-400 text-sm">2</p>
                    </div>
                    <div class="flex items-center text-gray-300">
                        <p class="mr-2 text-2xl">---</p>
                        <p class="text-2xl">+</p>
                    </div>
                </div>
                <!-- board card -->
                <div class="grid grid-rows-2 gap-2">
                    <c:forEach var="task" items="${inProgressTasks}">

                        <div class="p-2 rounded shadow-sm border-gray-100 border-2">
                            <h3 class="text-sm mb-3 text-gray-700">${task.title}</h3>
                            <p class="bg-yellow-100 text-xs w-max p-1 rounded mr-2 text-gray-700">IN_PROGRESS</p>
                            <div class="flex flex-row items-center mt-2">
                                <div class="bg-gray-300 rounded-full w-4 h-4 mr-3"></div>
                                <a href="#" class="text-xs text-gray-500">${task.assignedTo.username}</a>
                            </div>
                            <p class="text-xs text-gray-500 mt-2">${task.startDate}</p>
                            <p class="text-xs text-gray-500 mt-2">${task.endDate}</p>
                            <!-- Display tags -->
                            <div class="mt-1">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-gray-200 text-xs px-2 py-1 rounded-full mr-1">${tag}</span>
                                </c:forEach>
                            </div>
                            <div class="flex items-center space-x-4 text-sm">
                                <a href="tasks?editTaskId=${task.id}">
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-purple-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Edit">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path>
                                        </svg>
                                    </button>
                                </a>

                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Delete">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                  clip-rule="evenodd">
                                            </path>
                                        </svg>
                                    </button>
                                </form>
                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="requestModification"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="requestModification">
                                        <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" id="Outline"
                                             viewBox="0 0 24 24"
                                             width="5"
                                             height="5">
                                            <path d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z"/>
                                            <path d="M12,5a1,1,0,0,0-1,1v8a1,1,0,0,0,2,0V6A1,1,0,0,0,12,5Z"/>
                                            <rect x="11" y="17" width="2" height="2" rx="1"/>
                                        </svg>
                                    </button>

                                </form>
                            </div>
                        </div>

                    </c:forEach>

                </div>
                <div class="flex flex-row items-center text-gray-300 mt-2 px-1">
                    <p class="rounded mr-2 text-2xl">+</p>
                    <p class="pt-1 rounded text-sm">New</p>
                </div>
            </div>

            <!-- Complete Kanban -->
            <div class="bg-white rounded px-2 py-2">
                <!-- board category header -->
                <div class="flex flex-row justify-between items-center mb-2 mx-1">
                    <div class="flex items-center">
                        <h2 class="bg-green-100 text-sm w-max px-1 rounded mr-2 text-gray-700">DONE</h2>
                        <p class="text-gray-400 text-sm">4</p>
                    </div>
                    <div class="flex items-center">
                        <p class="text-gray-300 mr-2 text-2xl">---</p>
                        <p class="text-gray-300 text-2xl">+</p>
                    </div>
                </div>
                <!-- board card -->
                <div class="grid grid-rows-2 gap-2">
                    <c:forEach var="task" items="${doneTasks}">

                        <div class="p-2 rounded shadow-sm border-gray-100 border-2">
                            <h3 class="text-sm mb-3 text-gray-700">${task.title}</h3>
                            <p class="bg-green-100 text-xs w-max p-1 rounded mr-2 text-gray-700">Complete</p>
                            <div class="flex flex-row items-center mt-2">
                                <div class="bg-gray-300 rounded-full w-4 h-4 mr-3"></div>
                                <a href="#" class="text-xs text-gray-500">${task.assignedTo.username}</a>
                            </div>
                            <p class="text-xs text-gray-500 mt-2">${task.startDate}</p>
                            <p class="text-xs text-gray-500 mt-2">${task.endDate}</p>
                            <!-- Display tags -->
                            <div class="mt-1">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-gray-200 text-xs px-2 py-1 rounded-full mr-1">${tag}</span>
                                </c:forEach>
                            </div>
                            <div class="flex items-center space-x-4 text-sm">
                                <a href="tasks?editTaskId=${task.id}">
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-purple-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Edit">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path>
                                        </svg>
                                    </button>
                                </a>

                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Delete">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                  clip-rule="evenodd"></path>
                                        </svg>
                                    </button>
                                </form>
                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="requestModification"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="requestModification">
                                        <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" id="Outline"
                                             viewBox="0 0 24 24"
                                             width="5"
                                             height="5">
                                            <path d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z"/>
                                            <path d="M12,5a1,1,0,0,0-1,1v8a1,1,0,0,0,2,0V6A1,1,0,0,0,12,5Z"/>
                                            <rect x="11" y="17" width="2" height="2" rx="1"/>
                                        </svg>
                                    </button>

                                </form>

                            </div>
                        </div>

                    </c:forEach>
                </div>
                <div class="flex flex-row items-center text-gray-300 mt-2 px-1">
                    <p class="rounded mr-2 text-2xl">+</p>
                    <p class="pt-1 rounded text-sm">New</p>
                </div>
            </div>

            <div class="bg-white rounded px-2 py-2">
                <!-- board category header -->
                <div class="flex flex-row justify-between items-center mb-2 mx-1">
                    <div class="flex items-center">
                        <h2 class="bg-yellow-100 text-sm w-max px-1 rounded mr-2 text-gray-700">request To Modify
                            Tasks</h2>
                        <p class="text-gray-400 text-sm">2</p>
                    </div>
                    <div class="flex items-center text-gray-300">
                        <p class="mr-2 text-2xl">---</p>
                        <p class="text-2xl">+</p>
                    </div>
                </div>
                <!-- board card -->
                <div class="grid grid-rows-2 gap-2">
                    <c:forEach var="task" items="${requestToModifyTasks}">

                        <div class="p-2 rounded shadow-sm border-gray-100 border-2">
                            <h3 class="text-sm mb-3 text-gray-700">${task.title}</h3>
                            <p class="bg-yellow-100 text-xs w-max p-1 rounded mr-2 text-gray-700">IN_PROGRESS</p>
                            <div class="flex flex-row items-center mt-2">
                                <div class="bg-gray-300 rounded-full w-4 h-4 mr-3"></div>
                                <a href="#" class="text-xs text-gray-500">${task.assignedTo.username}</a>
                            </div>
                            <p class="text-xs text-gray-500 mt-2">${task.startDate}</p>
                            <p class="text-xs text-gray-500 mt-2">${task.endDate}</p>
                            <!-- Display tags -->
                            <div class="mt-1">
                                <c:forEach var="tag" items="${task.tags}">
                                    <span class="bg-gray-200 text-xs px-2 py-1 rounded-full mr-1">${tag}</span>
                                </c:forEach>
                            </div>
                            <div class="flex items-center space-x-4 text-sm">
                                <a href="tasks?editTaskId=${task.id}">
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-purple-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Edit">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path>
                                        </svg>
                                    </button>
                                </a>

                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Delete">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z"
                                                  clip-rule="evenodd">
                                            </path>
                                        </svg>
                                    </button>
                                </form>
                                <form action="tasks" method="post">
                                    <input type="hidden" name="action" value="requestModification"/>
                                    <input type="hidden" name="taskId" value="${task.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="requestModification">
                                        <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" id="Outline"
                                             viewBox="0 0 24 24"
                                             width="5"
                                             height="5">
                                            <path d="M12,0A12,12,0,1,0,24,12,12.013,12.013,0,0,0,12,0Zm0,22A10,10,0,1,1,22,12,10.011,10.011,0,0,1,12,22Z"/>
                                            <path d="M12,5a1,1,0,0,0-1,1v8a1,1,0,0,0,2,0V6A1,1,0,0,0,12,5Z"/>
                                            <rect x="11" y="17" width="2" height="2" rx="1"/>
                                        </svg>
                                    </button>

                                </form>
                            </div>
                        </div>

                    </c:forEach>

                </div>
                <div class="flex flex-row items-center text-gray-300 mt-2 px-1">
                    <p class="rounded mr-2 text-2xl">+</p>
                    <p class="pt-1 rounded text-sm">New</p>
                </div>
            </div>
        </div>
    </div>
</main>


<script src="./assets/js/navigation.js"></script>
<script src="./assets/js/navbar.js"></script>
<script src="./assets/js/theme.js"></script>

</body>
</html>