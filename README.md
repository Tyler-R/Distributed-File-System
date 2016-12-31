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

