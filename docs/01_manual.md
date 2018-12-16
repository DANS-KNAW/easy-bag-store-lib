---
title: Manual
layout: home
---

Manual
======

TABLE OF CONTENTS
-----------------

SYNOPSIS
--------

```scala
   import nl.knaw.dans.lib.bagstore._
   import better.files._

   val bagStore = BagStore(
       baseDir = File("/data/my-bag-store"),
       stagingDir = File("/data/bag-staging"))
   val bag = new File("/home/myhome/mybag")
   bagStore.add(bag).map(item =>
      println("Bag added")
      println(s"Bag ID = ${item.getId}")
      println(s"Storage at ${item.getLocation.getOrElse("don't know")}"))

   bagStore.get(BagId("2c6b4e34-004b-11e9-a516-bf707c4199d6"))
      .map(item =>
        println("Found bag")
        println("")


```



DESCRIPTION
-----------
A bag store is a way to store and identify data packages following a few very simple rules. See the [definitions] page
for a quasi-formal description and see the [tutorial] page for a more informal, hands-on introduction. The tutorial uses
the [`easy-bag-store`] program, which implements both a command line and an HTTP interface to a bag store. However, the
bag store should be viewed first and foremost as a specification and it should be fairly simple to implement your own tools.
This library can be considered a reference implementation.

[definitions]: 03_definitions.html
[tutorial]: 04_tutorial.html
[`easy-bag-store`]: https://github.com/DANS-KNAW/easy-bag-store


EXAMPLES
--------
TODO: add "recipes"


INSTALLATION AND CONFIGURATION
------------------------------
To use this library from a Maven project, add the DANS Maven repository to your POM, as well one dependency:

```xml
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
             http://maven.apache.org/maven-v4_0_0.xsd">
    <!---
    ...
    --->

        <dependencies>
          <!---
            ...
          --->
          <dependency>
              <groupId>nl.knaw.dans.shared</groupId>
              <artifactId>easy-bag-store-lib_2.12</artifactId>
              <version>${easy-bag-store.version}</version>
          </dependency>
        </dependencies>
        <repositories>
            <!--
              Possibly other repositories here
            -->
            <repository>
                <id>DANS</id>
                <releases>
                    <enabled>true</enabled>
                </releases>
                <url>http://maven.dans.knaw.nl/</url>
            </repository>
        </repositories>
    </project>
```



BUILDING FROM SOURCE
--------------------

Prerequisites:

* Java 8 or higher
* Maven 3.3.3 or higher

Steps:

    git clone https://github.com/DANS-KNAW/easy-bag-store-lib.git
    cd easy-bag-store-lib
    mvn install
