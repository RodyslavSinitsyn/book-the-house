package bth.postservice.exception;

import bth.models.exception.PostNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof IllegalArgumentException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Illegal Argument: " + ex.getMessage())
                    .build();
        }
        if (ex instanceof PostNotFoundException postNotFoundException) {
            return GraphqlErrorBuilder.newError(env)
                    .message("Post not exist: " + postNotFoundException.getPostId())
                    .build();
        }
        return GraphqlErrorBuilder.newError(env)
                .message("Internal Server Error")
                .build();
    }
}
