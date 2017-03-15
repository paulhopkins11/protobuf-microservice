package uk.co.frameoworktraining.grpc;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import uk.co.frameworktraining.grpc.proto.BookServiceGrpc;
import uk.co.frameworktraining.grpc.proto.Bookservice.Book;
import uk.co.frameworktraining.grpc.proto.Bookservice.BookReply;
import uk.co.frameworktraining.grpc.proto.Bookservice.BookRequest;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class BookService {
	private static final Logger logger = Logger.getLogger(BookService.class.getName());

	/* The port on which the server should run */
	private int port = 50051;
	private Server server;

	private void start() throws IOException {
		server = ServerBuilder.forPort(port).addService(new BookServiceImpl()).build().start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its
				// JVM shutdown hook.
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				BookService.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		final BookService server = new BookService();
		server.start();
		server.blockUntilShutdown();
	}

	private class BookServiceImpl extends BookServiceGrpc.AbstractBookService {

		@Override
		public void getAllBooks(BookRequest req, StreamObserver<BookReply> responseObserver) {
			System.out.println(">>>");
			Book book = Book.newBuilder().setTitle("title").setDescription("desc").setIsbn("isbn").setId(123).build();
			BookReply reply = BookReply.newBuilder().addBooks(book).build();
			responseObserver.onNext(reply);
			responseObserver.onCompleted();
		}
	}
}