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
- Spring Boot
- MySQL

### Client Web Application
- Angular framework

## Application Architecture

The application consists of a web browser, a Spring container (Spring Boot), and a relational database (SUBP). The backend communicates with the frontend via a RESTful service.

## Data Model
The data model includes entities:
- The User entity represents registered user of the application and is intended to store data used for authentication and authorization. 
- An unregistered user can only register to the application. 
- A user can also be a system administrator or a group administrator.
- A group administrator maintains a specific group, while a system administrator manages it application and has the ability to remove groups. 
- Posts are described by the Post entity and they are text, but can also contain images. 
- The Comment entity represents a comment in a given application. 
- The Reaction entity represents a reaction to certain posts or comments.
- If the content violates community or application rules, a related Report entity is created to a post, comment or user. 
- The Group entity represents a group that contains posts and comments are owned by users and maintained by the group administrator.
  ![social-network](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/f0c4375c-62fe-47a0-8a92-572fec27254f)

##Images




