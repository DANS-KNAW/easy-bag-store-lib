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

```xml
    <dependency>
        <groupId>nl.knaw.dans.shared</groupId>
        <artifactId>easy-bag-store-lib_2.12</artifactId>
        <version>1.0.0</version>
    </dependency>
```


DESCRIPTION
-----------
A bag store is a way to store and identify data packages following a few very simple rules. See the [bag-store] page
for a quasi-formal description and see the [tutorial] page for a more informal, hands-on introduction. The `easy-bag-store` 
command line tool and HTTP-service facilitate the management of one or more bag stores, but use of these tools is optional; 
the whole point of the bag store concept is that it should be fairly easy to implement your own tools for working with it.

[bag-store]: 03_definitions.html
[tutorial]: 04_tutorial.html

### Command line tool
By using the `easy-bag-store` command you can manage a bag store from the command line. The sub-commands in above 
[SYNOPSIS](#synopsis) are subdivided into two groups:

* Sub-commands that target items in the bag store. These implement operations that change, check or retrieve items in a bag store.
* Sub-commands that target bags outside a bag store. These are typically bags that are intended to be 
  added to a bag store later, or that have just been retrieved from one. These sub-commands still work in the context of one or
  more bag stores, because the bag directories they operate on may contain [local references] to bags in those stores.
  
Some of the sub-commands require you to specify the store context you want to use. The store to operate on can be specified
in one of two ways:

* With the `--store` option. This expects the shortname of a store, which is mapped to a base directory in the `stores.properties`
  configuration file.
* By specifying the base directory directly, using the `--base-dir` option.

If you call a sub-command that requires a store context, without providing one, you are prompted for a store shortname.

### HTTP service
`easy-bag-store` can also be executed as a service that accepts HTTP requests, using the sub-command `run-service`. `initd` and
`systemd` scripts are provided, to facilitate deployment on a Unix-like system (see [INSTALLATION AND CONFIGURATION](#installation-and-configuration)).

For details about the service API see the [OpenAPI specification].

[OpenAPI specification]: ./api.html
[local references]: 03_definitions.html#local-item-uri

INSTALLATION AND CONFIGURATION
------------------------------
The preferred way of install this module is using the RPM package. This will install the binaries to
`/opt/dans.knaw.nl/easy-bag-store`, the configuration files to `/etc/opt/dans.knaw.nl/easy-bag-store`,
and will install the service script for `initd` or `systemd`. It will also set up a default bag store
at `/srv/dans.kanw.nl/bag-store`.

If you are on a system that does not support RPM, you can use the tarball. You will need to copy the
service scripts to the appropiate locations yourself.

BUILDING FROM SOURCE
--------------------

Prerequisites:

* Java 8 or higher
* Maven 3.3.3 or higher

Steps:

    git clone https://github.com/DANS-KNAW/easy-bag-store-lib.git
    cd easy-bag-store-lib
    mvn install
