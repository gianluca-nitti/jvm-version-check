# jvm-version-check [![Build Status](https://travis-ci.org/gianluca-nitti/jvm-version-check.svg?branch=master)](https://travis-ci.org/gianluca-nitti/jvm-version-check)
This is a tiny (single class) tool which takes one command line argument representing a version string and compares it to the version of the JVM the class is running on.
If the JVM version is equal or higher than the supplied one, the exit code will be 0 (OK), otherwise 1 (JVM is outdated) or other code if exception is thrown during the computation (e.g. supplied string is not a valid version).

Version format specification: http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html

#Build
```
gradle compileJava
```
Run tests with
```
gradle test
```
The class placed at `build/classes/main/JvmVersion.class` can be moved anywhere and run with
```
java JvmVersion <x.y.z_u>
```
then check the error code.

#Example
```
sh-4.4$ java -version
java version "1.8.0_121"
Java(TM) SE Runtime Environment (build 1.8.0_121-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.121-b13, mixed mode)
sh-4.4$ java JvmVersion 1.8.0_40
sh-4.4$ echo $?
0
sh-4.4$ java JvmVersion 1.8.0_121
sh-4.4$ echo $?
0
sh-4.4$ java JvmVersion 1.9.0
sh-4.4$ echo $?
1
sh-4.4$ java JvmVersion
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: 0
        at JvmVersion.main(JvmVersion.java:53)
sh-4.4$ java JvmVersion hello
Exception in thread "main" java.lang.IllegalArgumentException: Invalid version string "hello"
        at JvmVersion.<init>(JvmVersion.java:15)
        at JvmVersion.runningJvmSupports(JvmVersion.java:49)
        at JvmVersion.main(JvmVersion.java:53)
sh-4.4$
```
