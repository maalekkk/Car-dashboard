@ECHO OFF
set CLASSPATH=.
%JAVA_HOME%\bin\java -Xms128m -Xmx384m -Xnoclassgc src\main\java\org\Presentation\Tui.java Tui
pause