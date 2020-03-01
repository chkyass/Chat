# Chat

### Implementation
- Implementation of a webchat application using Spring Boot, available at https://ychouaki.com/chat. Do not hesitate to register and contact me on it!
- The Controller layer exposes the JSON endpoints queried by JQuery to dynamically rewrite the frontend.
- WebSockets and publish and subscribe mechanisms of the STOMP protocol are used to route messages between rooms.
- The Frontend is built with javascript, jQuery, Html, CSS and multimedia rules for responsive web design.
- Spring Security and MySQL manage role-based authorizations and authentications.
- Spring JPA is used for the persistence layer and maintains the logic between users, rooms and messages.
- Nginx acts as an HTTPS reverse proxy for the application and is configured to allow WebSockets.
- The deployment of the application on the server is automated with Ansible.

### Run
```
mvn package
java -jar target/chatroom-0.0.1-SNAPSHOT.jar
``
