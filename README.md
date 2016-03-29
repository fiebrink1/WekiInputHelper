# WekiInputHelper
The WekiInputHelper works with any Wekinator-style feature extractor that OSC messages. It sits in between your feature extractor and Wekinator. (Or, you could stick it between your feature extractor and some other environment.)

It allows you to apply processing operations such as:

* min, max, average, standard deviation of a feature over an analysis window
* collecting input values into a fixed-length history buffer
* first- and second-order differences
* arbitrary mathematical expressions, including those with digital filter notation
* altering sending behaviour, including rate throttling, or sending only when a trigger condition is satisfied

## Instructions

0. Install using the installer for your operating system (see Releases), or by running "java -jar WekiInputHelper.jar" in the command line.

1. On the first screen, you will first need to specify the OSC message used by your feature extractor (this is /wek/inputs by default), the port your feature extractor is sending to (this is 6448 by default), and the number of inputs. Each OSC message sent to WekiInputHelper must have the same number of inputs bundled in this OSC message, and they must be sent as floats. *Note that these settings match all of the example feature extractors for Wekinator at www.wekinator.org/examples/, so you can use WekiInputHelper to intercept these messages without changing those programs.*

2. On the second screen, you will need to specify the OSC message you want to output. Typically this will be sent to Wekinator, so by default we'll use the typical Wekinator OSC input message (/wek/inputs, same as sent by the standard example feature extractors). By default it will send OSC messages to *port 6449* -- note that this is different from Wekinator's default listening port of 6448! This is because we can only have one application listening on an OSC port at once, and WekiInputHelper will take 6448 by default. So make sure you've set up Wekinator (or whatever other program you're sending to) to listen on some other port.

3.  Use the "Add new" button in this screen to add new features, which are computed using the original feature values.

4.  By default, WekiInputHelper will send a new message out every time it receives a feature message in. You can optionally change this behavior in the second pane, "When to send."

5.  Once you're ready to send data, go to the "Send and monitor" pane. WekiInputHelper *will not send data unless you are in this pane.*

6.  Enjoy!

## Building from source
This project has the following dependencies:
* For math expression parsing: https://github.com/fiebrink1/expr
* For OSC communication: http://www.illposed.com/software/javaosc.html
* For XML file support: http://x-stream.github.io/, http://repo.maven.apache.org/maven2/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar, http://www.extreme.indiana.edu/dist/java-repository/xpp3/jars/xpp3_min-1.1.4c.jar


