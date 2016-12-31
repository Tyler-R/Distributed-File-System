# runs the program on my computer using default values. 
# -Dorg.gradle.java.home='/usr/lib/jvm/java-8-oracle' can be excluded if the command "gradle --version" returns 1.8.* for the JVM version. 

gradle run -Dorg.gradle.java.home='/usr/lib/jvm/java-8-oracle' -PappArgs="['127.0.0.1', '8080', './']"
