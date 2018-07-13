# KoriChat
KoriChat is a client-server chat application. Basic features are supported such as user and
group management, sending direct messages between users and broadcasting messages in user groups.

## User management
User manegment allows new users to register and account, login as a user and the ability to list registered users.

### Registering a user
Register a new user, in this example we register the user adrian as a new user.

```
REG adrian CHAT/1.0
Name: Adrian van den Houten
```

The server will reply with one of the following responses:

If the registration is successful:
```
201 Created
```
(This is user created on the server and can be used when ever you like. Users are recorded in a relational database.)

Or, if the user has already been registered.

```
409 Conflict
```

### Logging in
Login with a user. This allows a user to send messages
to groups and direct messages to other users.

Without logging in the user won't be able to do create groups or
send messages.

```
USE adrian CHAT/1.0
```

Subsist requests to the server will require passing the session id to allow the server to identify the user.

```
Set-Cookie: SESSIONID: SOME SESSION ID

200 OK
```


List users

```
LISTUSER CHAT/1.0
```
List users who are part of a group
```
LISTUSER chat CHAT/1.0
```
Example response:

```
adrian,josie
```

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
