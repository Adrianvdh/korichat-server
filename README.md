# KoriChat
KoriChat is a client-server chat application. Basic features are supported such as user and
group management, sending direct messages between users and broadcasting messages in user groups.

Stuff I need to do:
- [ ] User registration with persistent storage
- [ ] User login with session tracking
- [ ] List registered users
- [ ] Send direct messages to users
- [ ] Creating groups of users
- [ ] Broadcasting messages to groups
- [ ] Asynchronous messaging with custom message queue


## User management
User management allows new users to register and account, login as a user and the ability to list registered users.

### Registering a user
Register a new user, in this example we register the user adrian as a new user.

```
REG adrian CHAT/1.0
Name: Adrian van den Houten
```

The server will reply with one of the following responses:

If the registration is successful:
```
CHAT/1.O 201 Created
```
(This is user created on the server and can be used when ever you like. Users are recorded in a relational database.)

Or, if the user has already been registered.

```
CHAT/1.O 409 Conflict
```

### Logging in
Login with a user. This enables a person to login-to the user and start sending messages or doing other
stuff like creating groups.


here are some commands that require you to be logged in:
TODO

```
USE adrian CHAT/1.0
```

The server will reply with one of the following responses:

if logging in was successful, a session id will be returned.
Subsist requests to the server will require passing the session id to allow the server to identify the user.
```
CHAT/1.0 200 OK
Set-Cookie: SESSIONID=042cf5ba-7f52-4805-a2a0-3c6006fe3eee
```
Every connection to the server as a sessionId. SessionId's are bound to the TCP/IP connection of the client.
For anonymous users, the session is not recoverable when the connection drops and a new session will be created.

For users that login. The sessionId is used in every request to the server so that the server can identify a
user's connection. If the user's connection is dropped, the next time the client sends a request, the server
will attempt to bind the new connection to the user's same sessionId again.

### Listing users
Listing registered users.

This is a helpful command that can be used to allow a user to easily be able to choose someone to direct message.

Listing all registered users:

```
LIST_USER CHAT/1.0
```
List users in a group. In this case out group's name is 'chat'
```
LIST_USER chat CHAT/1.0
```

The server will reply with one of the following responses:

If there are existing users already registered on the server, we shall get a response that looks like this:
```
CHAT/1.0 200 OK

{
    "users": [
        "adrian",
        "josie"
    ]
}
```
In this case, adrian and josie have been registered on the server.

## Group management

Create a group named 'chat' that is owned by user 'adrian'

```
MK_GROUP chat:adrian CHAT/1.0
```

Add josie to our 'chat' group and give her a user role.

```
ADD_USER chat CHAT/1.0
Invite: josie:user,john:user
```

## Messaging

Send a direct message to Josie

```
SEND josie CHAT/1.0

Hey josie! :)
```

Send message on a group
```
SEND chat

Hello everyone
```
## Authors

* **Adrian van den Houten** - *Initial work* - [Adrianvdh](https://github.com/Adrianvdh)
