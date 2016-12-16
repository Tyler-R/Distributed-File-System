# Requirements

The server requires java 8 for compilation and execution.

If Java 8 is installed you can run 
```bash
sudo update-alternatives --config java
sudo update-alternatives --config javac
```

and then select the Java 8 options.
Once this is done the server should compile and execute without error.

If you are unable to run sudo then the easiest way to compile and run the application using Java 8 is to run the above commands without sudo, and then instead of selecting the Java 8 version, copy the path the exeuctables path for Java 8. Then you can paste the path into the build and run scripts respectively overtop of the java/javac commands.

Alternatively if Java 8 is not installed you can follow an online tutorial such as [this one](http://tipsonubuntu.com/2016/07/31/install-oracle-java-8-9-ubuntu-16-04-linux-mint-18/) to install Java

# Building the server

running
```bash
sh build.sh
```
should build the server without issue if you have Java 8 installed.

# Running the server
once the server has been built running 
```bash
sh run.sh <ipAddress> <port> <directory> 
```
For Example
```bash
sh run.sh 127.0.0.1 8080 ./tmp/
```

will execute the program. Note that the directory must end in a '/' character.

