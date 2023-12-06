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

## Images

## Login and Register
![Login](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/6b4f6539-9f2d-4aeb-b1cd-34fff90179f6)
![Register](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/130a7280-dbc5-4812-98a3-d029f4a09ab6)

## Posts
![Posts](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/e432449e-f4b9-427c-a39a-d9a96ef38dec)
![Post 2](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/d42daa2b-0500-4053-94ea-cfa469c5994f)

## Like, dislike, heart comment
![Post with like and image](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/3c23d80e-0389-42fb-8575-7bc3636cacd7)

## Create and edit post
![Create Post](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/7997738c-380b-454e-80fc-2ecb2dc7e688)
![Update Post](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/775558a4-36bf-42d8-a3b2-2daffd151520)

## Comments
![Comments](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/c6a5ff73-b5d1-48b0-84b3-29999e0aa02c)

## Create and edit group
![Groups](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/1821af67-89bc-49c4-b504-3d800de9d1fa)
![Create group](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/3399a05e-90d1-494c-ac7b-916bf3e88067)

## Profile
![User profile](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/6a4b80d2-54e0-4d9d-88d5-fe3b1b97d179)
![Edit profile](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/947ff00c-7151-4e4c-8f4b-f6f76674419e)

## Search for friends
![Search for friends](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/cf0bfc08-fae0-4617-a41d-42ae70beaf0f)

## Friend requests
![Friend requests](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/00459a14-46c7-4dd9-b9bd-9857eeb6a43a)

## Report
![Report](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/733060f7-730f-41a6-9d7d-1d903fe0e6e2)
![Reports](https://github.com/anna02272/SR46-2021-SVT_KVT2023-projekat/assets/96575598/9ae82a5b-3d12-49fe-af95-6566a3eec610)

















