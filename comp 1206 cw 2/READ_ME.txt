there are two seperate main methods in Client and Server; neither require any arguments to work

Extensions:
- all message passing (over the filesystem) is encrypted; fresh keys are generated for each new client
- the server is highly multithreaded for maximum speed for simultaneous clients
- server save is also encrypted
- Item search fields are updated from the server to allow user to choose from options rather than guessing the text to search.
- items/notifications are colour coded to increase usability. 
