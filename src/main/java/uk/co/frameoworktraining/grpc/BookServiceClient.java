package uk.co.frameoworktraining.grpc;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import uk.co.frameworktraining.grpc.proto.BookServiceGrpc;
import uk.co.frameworktraining.grpc.proto.Bookservice.Book;
import uk.co.frameworktraining.grpc.proto.Bookservice.BookReply;
import uk.co.frameworktraining.grpc.proto.Bookservice.BookRequest;

/**
 * A simple client that requests a greeting from the {@link BookService}.
 */
public class BookServiceClient {
	private static final Logger logger = Logger.getLogger(BookServiceClient.class.getName());

	private final ManagedChannel channel;
	private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public BookServiceClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
		blockingStub = BookServiceGrpc.newBlockingStub(channel);
	}

	public void getAllBooks(String title) {
		BookRequest request = BookRequest.newBuilder().setTitle(title).build();
		BookReply response;
		try {
			response = blockingStub.getAllBooks(request);
		} catch (StatusRuntimeException e) {
			logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
			return;
		}
		for (int i = 0; i < response.getBooksCount(); i++) {
			Book book = response.getBooks(i);
			logger.info("Book: " + book.getTitle() + " " + book.getDescription() + " " + book.getIsbn() + " "
					+ book.getId());

		}
	}

	/**
	 * Greet server. If provided, the first element of {@code args} is the name
	 * to use in the greeting.
	 */
	public static void main(String[] args) throws Exception {
		BookServiceClient client = new BookServiceClient("localhost", 50051);
		try {
			/* Access a service running on the local machine on port 50051 */
			String title = "Harry Potter";
			if (args.length > 0) {
				title = args[0]; /*
									 * Use the arg as the name to greet if
									 * provided
									 */
			}
			client.getAllBooks(title);
		} finally {
			client.shutdown();
		}
	}
}