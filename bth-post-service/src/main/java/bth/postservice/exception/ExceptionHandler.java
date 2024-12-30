package bth.postservice.exception;

import bth.common.exception.PostNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error(ex.getMessage(), ex);
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
