cls
@echo off

@if 	 "%1"=="" set TARGET=src\editor\*.java
@if NOT  "%1"=="" set TARGET=src\editor\%1.java
@if NOT  "%2"=="" set TARGET=src\editor\%1.java src\editor\%2.java
@if NOT  "%3"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java
@if NOT  "%4"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java src\editor\%4.java
@if NOT  "%5"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java src\editor\%4.java src\editor\%5.java
@if NOT  "%6"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java src\editor\%4.java src\editor\%5.java src\editor\%6.java
@if NOT  "%7"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java src\editor\%4.java src\editor\%5.java src\editor\%6.java src\editor\%7.java
@if NOT  "%8"=="" set TARGET=src\editor\%1.java src\editor\%2.java src\editor\%3.java src\editor\%4.java src\editor\%5.java src\editor\%6.java src\editor\%7.java src\editor\%8.java

@echo on
javac -deprecation -classpath class %TARGET% -d class 
pause
@echo off
