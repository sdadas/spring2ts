## spring2ts

**Generate TypeScript REST client directly from Spring MVC application source**

#### Example

Add spring2ts-annotations dependency to your project.

```xml
<dependency>
    <groupId>com.sdadas.spring2ts</groupId>
    <artifactId>spring2ts-annotations</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Annotate classes or interfaces you want to include in the output with `@SharedService` or `@SharedModel`.
`@SharedModel` generates TypeScript interfaces based on java type properties exposed by getters or setters.
`@SharedService` maps Spring MVC method and class annotations to TypeScript code that sends ajax requests.

Given sample controller:

```java
@SharedService
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public HelloResponse hello(@RequestParam(value = "name", defaultValue = "world") String name) {
        return new HelloResponse("Hello " + name);
    }
}
```

and sample response class:

```java
@SharedModel
public class HelloResponse {

    private String greeting;

    public HelloResponse(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}
```

spring2ts will generate the following directory structure:

```
|---/model
|        |---model.ts
|
|---/service
         |---RequestBuilder.ts
         |---HelloController.ts
```

`model.ts` contains all the interfaces generated from `@SharedModel` annotations. Each `@SharedService` is generated as a separate class in its own file. `RequestBuilder.ts` is a helper class that includes backend code responsible for sending requests. Currently only JQuery based `RequestBuilder` is available but there's more to come in the future (angular2+ backend using http module in progress).

model.ts

```typescript
export interface HelloResponse {
    greeting?: string;
}
```

HelloController.ts:

```typescript
import {HelloResponse} from "../model/model";
import {RequestBuilder, HttpMethods, ContentTypes} from "./RequestBuilder";

export class HelloController {

    public hello(name: string): JQueryPromise<HelloResponse> {
        let _builder: RequestBuilder = new RequestBuilder();
        return _builder.path('/hello')
            .method(HttpMethods.GET)
            .queryParam('name', name)
            .contentType(ContentTypes.Json)
            .build();
    }

}
```

For more advanced examples, see `spring2ts-examples` module sources.

#### Usage

##### Using API directly from Java

Add spring2ts-core dependency:

```xml
<dependency>
    <groupId>com.sdadas.spring2ts</groupId>
    <artifactId>spring2ts-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Use `SourceGen` class, passing java source directory and output directory as parameters.

```java
SourceGen generator = new SourceGen(sourceDir, outputDir);
generator.run();
```

##### Using spring2ts-maven-plugin

Using this tool with maven:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.sdadas.spring2ts</groupId>
            <artifactId>spring2ts-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <executions>
                <execution>
                    <goals>
                        <goal>spring2ts</goal>
                    </goals>
                    <configuration>
                        <sourceDir>${basedir}</sourceDir>
                        <outputDir>${project.build.directory}/generated-sources</outputDir>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```