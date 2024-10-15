# DevSync - Task Management Application

## Project Overview

**DevSync** is a web-based task management application designed to streamline task handling within a team. It offers
various features like user authentication, task management, and scheduling using cron jobs, ensuring that users and
managers can effectively collaborate on projects. The application supports full CRUD (Create, Read, Update, Delete)
operations for users, tasks, and tags, while enforcing specific business rules related to task scheduling, deadlines,
and role-based permissions.

The project was developed using **Jakarta EE** and integrates several modern technologies, ensuring a robust,
maintainable, and scalable system for managing tasks.

## Technologies Used

- **Jakarta EE 10**: Enterprise edition framework for building scalable web applications.
- **Apache Tomcat 10**: Server for running the application.
- **Hibernate ORM with JPA**: Object-Relational Mapping for handling database operations.
- **JSTL (Jakarta Standard Tag Library)**: For rendering dynamic content on the front end.
- **Tailwind CSS**: For styling the front end and ensuring a clean, responsive UI.
- **Maven**: For project management and building the application.
- **JUnit & Mockito**: For unit testing the application's functionality.
- **Quartz Scheduler**: To handle scheduling tasks such as resetting user tokens and updating task statuses
  automatically.

## Functionalities

### Version 1.1.0

#### Task Management

1. **Task Creation Restrictions**:
    - Tasks cannot be created in the past.
    - Users are required to assign multiple tags to tasks for better organization.
    - Task scheduling is restricted to a maximum of 3 days in advance.

2. **Task Completion**:
    - Users must mark tasks as completed before the deadline; late completions are not permitted.

3. **User-Specific Features**:
    - Users can assign additional tasks to themselves but not to other users.
    - A token system is implemented to regulate task replacements and deletions:
        - Users are given two task replacement tokens per day for replacing tasks assigned by their manager.
        - Each user has one token per month for deleting tasks.
        - Deleting a task created by the same user does not affect the user's token balance.

4. **Deployment**:
    - After implementing these features, the application was deployed to a local repository to version 1.1.0.

### Version 1.2.0

#### Enhanced Task Management

1. **Manager Task Replacement**:
    - When a manager replaces a task, it must be reassigned to another user.
    - Once replaced, the task can no longer be modified or deleted by using tokens.

2. **Task Modification Requests**:
    - If a manager does not respond to a task change request within 12 hours, the user who made the request will receive
      double the modification tokens the following day.

3. **Automatic Task Updates**:
    - Every 24 hours, the application automatically marks tasks as incomplete if they are overdue and have not been
      marked as completed.

4. **Manager's Task Overview**:
    - Managers can view an overview of all tasks assigned to their employees, including:
        - Task completion percentages, filtered by week, month, and year.
        - The number of tokens used by employees, providing better insight into team performance and task progress.

5. **Deployment**:
    - After completing the new functionalities, the application was deployed to a local repository, upgrading to version
      1.2.0.

## Testing Strategy

### Unit Testing

- **JUnit & Mockito**: Unit tests were implemented for all critical components of the application. These tests cover key
  features such as task creation, user authentication, and business rule validation.
- Automated tests ensure that changes made to the codebase do not introduce bugs or regressions.

### Test Coverage

- Code coverage was measured to ensure that the majority of critical application logic is covered by unit tests. This
  provides a safety net for future development and refactoring.

## Task Scheduling

The application uses **Quartz Scheduler** to handle automated background processes, ensuring the smooth functioning of
key features such as:

- **Token Reset**: At midnight on the 1st of each month, all users’ task delete tokens are reset.
- **Task Status Update**: Every 24 hours, the application checks for overdue tasks and marks them as incomplete if they
  haven't been completed.

## Project Structure

1. **Entities**: The application includes entities such as `User`, `Task`, and `Tag`, which are mapped to the database
   using JPA annotations.
2. **Services**: Business logic is handled in service classes (e.g., `UserService`, `TaskService`) to ensure proper
   separation of concerns.
3. **Servlets**: Servlets handle HTTP requests and responses, facilitating CRUD operations and interaction between the
   front end and the back end.
4. **Scheduler**: The Quartz Scheduler is used to automate recurring tasks such as resetting tokens and updating task
   statuses.

## Front-End Design

- The application’s user interface is designed using **Tailwind CSS** for a modern and responsive design.
- **JSTL** is used for rendering dynamic content on the front end, providing a seamless user experience across different
  views, such as task dashboards, user management, and manager overviews.

## Screenshots


