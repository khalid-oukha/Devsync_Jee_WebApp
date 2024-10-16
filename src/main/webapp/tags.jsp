<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>tag Management</title>
    <link rel="stylesheet" href="./assets/css/tailwind.output.css"/>
</head>
<body class="bg-gray-300 dark:bg-gray-900">

<%@include file="WEB-INF/views/includes/header.jsp" %>

<%@include file="WEB-INF/views/includes/sidebar.jsp" %>
<main id="main" class="mt-32 h-screen px-28">


    <h2 class="my-6 text-4xl font-semibold text-center font-poppins tracking-widest text-gray-700 dark:text-gray-200">
        <span class="text-primary-100 dark:text-orange">Registered</span> tags
    </h2>


    <form action="tags" method="post" class="my-6">
        <input type="hidden" name="action" value="create"/>

        <button type="submit"
                class="px-4 py-2 my-2 bg-orange rounded text-white hover:bg-primary-100 focus:outline-none transition-colors">
            welcome
        </button>

        <div class="overflow-x-auto dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
            <table class="w-full whitespace-no-wrap">
                <thead>
                <tr class="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
                    <th class="px-4 py-3">name</th>
                </tr>
                </thead>
                <tbody class="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                <tr>
                    <td class="border border-gray-300 px-3 py-2 dark:border-gray-700 bg-gray-50">
                        <input type="text" placeholder="name" name="name" class="border p-2 rounded w-full">
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </form>


    <div class="w-full overflow-hidden rounded-lg shadow-xs">
        <div class="w-full overflow-x-auto">
            <table class="w-full whitespace-no-wrap">
                <thead>
                <tr class="text-xs font-semibold tracking-wide text-left text-gray-500 uppercase border-b dark:border-gray-700 bg-gray-50 dark:text-gray-400 dark:bg-gray-800">
                    <th class="px-4 py-3">tag id</th>
                    <th class="px-4 py-3">name</th>
                    <th class="px-4 py-3">action</th>
                </tr>
                </thead>
                <tbody class="bg-white divide-y dark:divide-gray-700 dark:bg-gray-800">
                <c:forEach var="tag" items="${tags}">
                    <tr class="text-gray-700 dark:text-gray-400">
                        <td class="px-4 py-3 text-sm">
                                ${tag.id}
                        </td>
                        <td class="px-4 py-3 text-sm">
                                ${tag.name}
                        </td>

                        <td class="px-4 py-3">
                            <div class="flex items-center space-x-4 text-sm">
                                <a href="tags?edittagId=${tag.id}">
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-purple-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Edit">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"></path>
                                        </svg>
                                    </button>
                                </a>

                                <form action="tags" method="post">
                                    <input type="hidden" name="action" value="delete"/>
                                    <input type="hidden" name="tagId" value="${tag.id}"/>
                                    <button class="flex items-center justify-between px-2 py-2 text-sm font-medium leading-5 text-red-600 rounded-lg dark:text-gray-400 focus:outline-none focus:shadow-outline-gray"
                                            aria-label="Delete">
                                        <svg class="w-5 h-5" aria-hidden="true" fill="currentColor" viewBox="0 0 20 20">
                                            <path fill-rule="evenodd"
                                                  d="M8.707 9.707a1 1 0 01-1.414 0L5 7.414 3.707 8.707a1 1 0 01-1.414-1.414l1.5-1.5a1 1 0 011.414 0L8 7.414l5-5A1 1 0 1113.707 4.293l-5 5z"
                                                  clip-rule="evenodd">

                                            </path>
                                        </svg>
                                    </button>
                                </form>
                            </div>
                        </td>

                    </tr>
                </c:forEach>
                </tbody>

            </table>
        </div>

    </div>
</main>


<script src="./assets/js/navigation.js"></script>
<script src="./assets/js/navbar.js"></script>
<script src="./assets/js/theme.js"></script>
</body>
</html>