# Application Utility

The Application Utility is a tool mean for filling out Insurance (benefits) applications programmatically.
Using a .csv file filled with employee demographic information, this application allows users to map the information in the .csv file into a fillable .pdf form.

The idea behind this application is to reduce the amount of monotonous data entry required during a company's Open Enrollment, 
as well as reduce the number of errors that may come from said data entry. It was originally developed for use at an Insurance Brokerage handling
their client's Open Enrollments.

## Roadmap

- **Update 4/8/20** - Electron rewrite has started! Take a look at the [electron](https://github.com/wparks08/application-utility/tree/electron) branch to check it out.

JavaFX was used to build this application. While the application is functional and effective, there are a lot of changes that need to happen - it can be much better.

The current intent is to rewrite the entire application under the [Electron](https://www.electronjs.org/) framework.
This would allow the usage of modern web development technology, which would allow for a web-based version to be developed much more easily.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To start, clone this repository to your local machine.

```shell script
git clone https://github.com/wparks08/ApplicationUtility
```

Open the project with the Java IDE of your choice. Build, and run the application from the `main` method of the `Main` class.

### Installing

The best way to get the application up and running is to open it in a Java IDE of your choice.
Build and run the project, where the main class is `AppUtility.Main`.

In a future build, Gradle will be implemented for dependency management (before the application is rewritten in Electron).

## Built With

* [Java](https://www.java.com/en/)
* [JavaFX 8](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-overview.htm) - Graphics and media packages for Java-based cross-platform applications.
* [JFoenix](https://github.com/jfoenixadmin/JFoenix) - JavaFX Material Design library
* [Apache PDFBox](https://pdfbox.apache.org/) - For interacting with fillable .pdf forms
* [Apache POI](https://poi.apache.org/) - Java API for Microsoft Documents

## Authors

* **Will Parks** - *Concept & Implementation* - [wparks08](https://github.com/wparks08)
