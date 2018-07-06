## User management

Register a new user, in this example we register the user adrian as a new user.

`REG adrian`

Responds with
 
`200 OK` user created

`400 Bad request` user already exists

Login as an existing user. (To be simple, don't use athentication)

`USE adrian`

List registered users (registered users: adrian, josie)

`LISTUSER`

Responds with

`adrian
josie`

## Sending messages

Send a new message to a destination. A destination can be another user or a group.

`SEND adrian:chat Hello everyone`

`SEND adrian:josie Hello josie`


## Groups

Create a group name 'chat' and is owned by 'adrian'

`MK_GROUP chat:adrian`

Add josie to our group and give her a user role.

`ADD_USER chat josie:user`