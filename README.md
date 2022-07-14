# ***Disclaimer***
***This repository has become a sandbox, and should not be used in a production sense. From time to time, the program may not build or be usable. There are a lot of examples of bad practices in this repository. Branches beginning with or containing the word `refactor` will be used to experiment with different ways of restructuring the program, and are being used by me as practice for restructuring bad/old/legacy code into something more maintainable - but is still usually experimental.***

# Application Utility

The Application Utility is a tool mean for filling out Insurance (benefits) applications programmatically.
Using a .csv file filled with employee demographic information, this application allows users to map the information in the .csv file into a fillable .pdf form.

The idea behind this application is to reduce the amount of monotonous data entry required during a company's Open Enrollment, 
as well as reduce the number of errors that may come from said data entry. It was originally developed for use at an Insurance Brokerage handling
their client's Open Enrollments.

## Roadmap

- **Update 6/17/20** - The original JavaFX application has been restructured, with Gradle added. This is, indeed, coming *after* the Electron rewrite started. The `master` branch was an eyesore to look at, and something needed to be done in the interim before the rewrite is complete. (Plus, it was good practice on adding Gradle to an existing project! :) )
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


### Running

To run the application, execute the `:build` then `:run` tasks from the command line or IDE of your choice.

```shell script
./gradlew build
# Then
./gradlew run
``` 

## Built With

* [Java](https://www.java.com/en/)
* [JavaFX](https://openjfx.io/index.html) - Graphics and media packages for Java-based cross-platform applications.
* [JFoenix](https://github.com/jfoenixadmin/JFoenix) - JavaFX Material Design library
* [Apache PDFBox](https://pdfbox.apache.org/) - For interacting with fillable .pdf forms
* [Apache POI](https://poi.apache.org/) - Java API for Microsoft Documents

## Authors

* **Will Parks** - *Concept & Implementation* - [wparks08](https://github.com/wparks08)
