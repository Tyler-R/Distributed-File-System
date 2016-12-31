# Running the server
from the command line you can use
```bash
gradle run -PappArgs="['<IPAddress>', '<port>', '<directory>']"
```
For Example
```bash
gradle run -PappArgs="['127.0.0.1', '8080', './tmp/']"
```

will execute the program. Note that the directory must end in a '/' character.

Alternatively you can use an IDE such as eclipse to build and run the server.

# Communication Protocol

The server uses a wire protocol similair to HTTP 1.0 to communicate. 

#### The Server accepts 5 commands: 

READ - Read an entire file.

NEW_TXN - Start a new transaction.

WRITE - Write a chunk of a file as part of a transaction.

COMMIT - Commit all writen files in a transaction to permanaent storage.

ABORT - Abort a transaction.

#### The server will respond with one of these 3 commands: 

ACK - Server Responds to the client when a NEW_TXN or COMMIT message has been received and executed.

ASK_RESEND - Server Requests that the client resend a write message for a specific transaction.

ERROR - Server informs the client that there was an error when processing a message.  The specific error is indicated by the error code.

#### Error codes:

201 - Invalid transaction ID. Occurs when a write or commit message is received with an transaction ID that the server is not currently processing.

202 - Invalid operation. Occurs when a command other than the 5 valid commands are recieved, or if a malformed message is received.

205 - File I/O error.

206 - File not found.

## Wire Protocol

Each message in the wire protocol is broken up into two sections, the header followed by the data.

