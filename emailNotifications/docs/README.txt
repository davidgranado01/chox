README
======

The Manual Email Notifications system provides a simple mechanism for providing update emails for organisations which are not on CHOX. 
 
 The main control is the settings.txt file.
 An example of this file is in src/test/resources 
 It should be in the same directory as the Jar file. 
 
 The program can be run using the following command:
 
 java -jar emailNotifications-2.0-jar-with-dependencies.jar ./settings.txt 20151125 false
 
 Note that these settings assume that the settings file is in the same location as the Jar file, that the start date for reporting is 20151125 and that no emails are to be sent (false).
 
 