# Protocol Buffer Microservices Example

## How to use this

1. Build the application

   ```
   mvn clean package
   ```

2. Start the server

   ```
    java -cp target/book-service-0.0.1-SNAPSHOT.jar uk.co.frameoworktraining.grpc.BookService
Mar 15, 2017 2:47:23 PM uk.co.frameoworktraining.grpc.BookService start
INFO: Server started, listening on 50051
   ```

3. Run the client

   ```
   java -cp target/book-service-0.0.1-SNAPSHOT.jar uk.co.frameoworktraining.grpc.BookServiceClient
Mar 15, 2017 2:48:01 PM io.grpc.internal.ManagedChannelImpl <init>
INFO: [ManagedChannelImpl@42f30e0a] Created with target localhost:50051
Mar 15, 2017 2:48:01 PM uk.co.frameoworktraining.grpc.BookServiceClient getAllBooks
INFO: Book: title desc isbn 123
Mar 15, 2017 2:48:01 PM io.grpc.internal.ManagedChannelImpl maybeTerminateChannel
INFO: [ManagedChannelImpl@42f30e0a] Terminated
   ```
4. Note the additional output from the server

```
$ java -cp target/book-service-0.0.1-SNAPSHOT.jar uk.co.frameoworktraining.grpc.BookService
Mar 15, 2017 2:47:23 PM uk.co.frameoworktraining.grpc.BookService start
INFO: Server started, listening on 50051
>>>
```
