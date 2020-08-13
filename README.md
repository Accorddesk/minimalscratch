# Micronaut test-suite fails

any test itself works, running it from IDEA Intellij as well as from terminal

```
./gradlew test --tests *MainApplicationTest
./gradlew test --tests *BackendDefaultWSTest
./gradlew test --tests *FrontendDefaultWSTest
```

but running them alltogether only the first one is green (doesn't matter which one I uncomment)

```
gradle test
```


