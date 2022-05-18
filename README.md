# qgames

This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .
This is project about test task (creation of Job System.)

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.
> > **_NOTE:_**  You can request the end points

```shell script
GET http://localhost:8080/managing/status
GET http://localhost:8080/managing/status/com.tangelogames.jobsystem.jobs.CustomJob1
GET http://localhost:8080/managing/status/com.tangelogames.jobsystem.jobs.CustomJob2
```

To run immediately

```shell script
GET http://localhost:8080/managing/run/com.tangelogames.jobsystem.jobs.CustomJob1
GET http://localhost:8080/managing/run/com.tangelogames.jobsystem.jobs.CustomJob2
```

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.
The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.
If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

## Rregarding How to create Custom Job

Please take a look on the package

```java
package com.tangelogames.jobsystem.jobs;
```

and follow the implementation of

```java
package com.tangelogames.jobsystem.jobs.CustomJob1;
```

To implement CustomJob a developer should extend the

```java
public abstract class AbstractScheduledJob;
```

Like it did the CustomJob1



