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

#### Commands sent to the server must be in the following format:

* The header contains 4 fields, each seperated by a space.  
* If the data field is not empty then the header is followed by a "\r\n\r\n" sequence.
* If the data field is empty then the header is followed by a "\r\n\r\n\r\n" sequence.

The 4 header fields are: Command, Transaction ID, Message Sequence Number and data length.

Command Field - contains the operation that the server will perform (listed above)

Transaction ID Field - specifies which transaction the message relates to. 

Message Sequence Number Field - specifies the current position of the message in a transaction.  NEW_TXN messages have a sequence number of 0. 

Data Length Field - specifies the length of the data field.  Should be 0 if the data field is empty.

Data Field - if the command is a write then the data field contains the data to be written to the file.  If the command is a read then the data field contains the name of the file to be read.

Examples: 
* READ -1 0 8\r\n\r\ntest.txt
* NEW_TXN -1 0 0\r\n\r\n\r\n
* WRITE 1 1 11\r\n\r\nhello world

#### Responses sent from the server will be in the following format:

* The header contains 5 fields, each seperated by a space.  
* If the data field is not empty then the header is followed by a "\r\n\r\n" sequence.
* If the data field is empty then the header is followed by a "\r\n\r\n\r\n" sequence.

The 5 header fields are: Command, Transaction ID, Message Sequence Number, Error Code, and Data Length

Command Field - contains the type of response. All valid response commands are listed above.

Transaction ID Field - Specifies which transaction the response relates to.

Message Sequence Number Field - Only relevant for an ASK_RESEND message. For an ASK_RESEND message it specifies which write message needs to be resent to the server.

Error Code - Specifies the error code assosiated with the response. All valid error codes are listed above.

Data Length Field - specifies the length of the data field.  Should be 0 if the data field is empty.

Data Field - Contains an easy to understand explanation of what caused the error.

