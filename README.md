## User management

Register a new user, in this example we register the user adrian as a new user.

```
REG adrian CHAT/1.0
Name: Adrian van den Houten
```

Login with a user.

```
USE adrian CHAT/1.0
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
SEND adrian:josie CHAT/1.0

Hey josie! :)
```

Send message on a group
```
SEND adrian:chat

Hello everyone
```