package com.erin.sample;

import com.erin.sample.data.Author;
import com.erin.sample.data.Book;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class GraphQLDataFetchers {
	
	Book book  = new Book();
	Author author = new Author();

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return book.getBook(bookId);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return author.getAuthor(authorId);
        };
    }
}