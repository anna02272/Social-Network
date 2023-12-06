# Social Network Application

## Project Overview

This repository contains the implementation for a social network application, inspired by popular platforms like Facebook, Instagram, and Twitter. The application provides various functionalities for users, including registration, posting, commenting, group creation, and more.

## Functionalities

- **User Registration:** Users can register, with a system administrator already predefined in the system.
- **Login and Logout:** Users can log in and out of the system. Access to other functionalities requires logging in.
- **Handling Posts:** Users can create posts with or without images.
- **Comment Update:** Users can reply to comments, and multiple replies are possible.
- **User Reactions:** Users can respond to posts and comments with likes, dislikes, and hearts.
- **Sorting Comments:** Comments can be sorted based on likes, dislikes, hearts, and publication date.
- **Sorting Posts:** Posts can be sorted by publication date in ascending or descending order.
- **Handling Groups:** Users can create and administer groups. Group administrators can suspend groups.
- **Reporting:** Users can report inappropriate content or users. Administrators can review and take actions.
- **Overview of Home Page:** Registered users see random public posts and posts from their friends or random groups.
- **Change Password:** Users can change their password by entering the current password twice and providing a new one.
- **Change Profile Data:** Users can set their display name, profile description, and profile picture. The user's group memberships are also displayed.
- **User Search:** Users can search for new friends based on first and last names. Friend requests can be sent and approved or rejected.
- **Blocking and Unblocking Users:** Group administrators can block and unblock users within their group.
- **Group Join Requests:** Group administrators can approve or reject incoming requests to join the group.
- **Removing Group Administrators:** System administrators can remove group administrators, reverting them to regular users.
- **Group Suspension:** System administrators can suspend groups, providing a reason for suspension. The app automatically removes group administrators from suspended groups.

## Non-functional Requirements

- User authentication using username and password.
- Authorization using the token mechanism.
- Log messages about important events during application execution.
  
## Technologies Used

### Server Web Technologies
- Spring framework
- Apache Tomcat (integrated with Spring Boot)
- MySQL

### Client Web Application
- Angular framework

## Application Architecture

The application consists of a web browser, a Spring container (Tomcat or Spring Boot), and a relational database (SUBP). The backend communicates with the frontend via a RESTful service.

## Data Model
The data model includes entities such as User, Post, Comment, Reaction, Report, and Group, representing the key elements of the social network application.



