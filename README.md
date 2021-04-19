# CSCI 420 (Spring 2021) JINXS UML Editor

This is a command line based UML Editor.

Design patterns used in this project can be found in the patterns.txt file in the code.

### Installation

[Java](https://www.java.com/en/download/) 8 (or above) is required to run the editor.

#### MacOS & Linux

To install the editor:
1. Clone this repository
```
$ git clone https://github.com/mucsci-students/2021sp-420-JINXS
```
2. Navigate into the new directory "2021sp-420-JINXS"
```
$ cd 2021sp-420-JINXS
```
3. Run the source command to make the setup command available
```
$ source .setupEditor.sh
```
4. Run the setup command
```
$ setup
```

#### Windows

[Maven](https://maven.apache.org/download.cgi) is required to setup the editor on Windows.

To install the editor:
1. Clone this repository
```
$ git clone https://github.com/mucsci-students/2021sp-420-JINXS
```
2. Navigate into the new directory "2021sp-420-JINXS"
```
$ cd 2021sp-420-JINXS
```
3. Run the setup command
```
$ .setup.cmd
```

### Usage

#### MacOS & Linux

Run the following command to run the editor

```
$ editor
```
Add the --cli argument to launch in the command-line interface instead of the graphical interface

#### Windows

Run the following command to run the editor

```
$ .editor.cmd
```
Add the --cli argument to launch in the command-line interface instead of the graphical interface

#### User Guide

A list of all available commands to operate the editor in the command-line interface can be viewed with the following command (use the above command to run the editor first based on your OS)

```
$ help
```
The GUI can be interacted with by using the menu bar at the top when launched. Once classes have been added to the window, their location can be changed by dragging and dropping them from their border colored in black. (Full dragability to come in the future). Class attributes like fields, methods, and parameters are displayed in the class boxes themselves. Relationships are represented by arrows between the class boxes with the arrow coming out of the source of the relationship pointing to the source at the end of the arrow head

Work can effectively be saved and loaded between both UIs of the program

Thanks for using our program!

### Authors
* @natshenk
* @JonathanWilkins1
* @Irblake14
* @SimonS1231
* @Xpagan
