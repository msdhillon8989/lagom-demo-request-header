package com.mani.demo;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.apache.http.HttpRequest;
import play.mvc.Http;

import static com.lightbend.lagom.javadsl.api.Service.named;

/**
 * Created by msdhillon8989 on 24/02/17.
 */
public interface ServiceAPI extends Service {

    ServiceCall<NotUsed, String> method1();
    /**
     *
     * @return
     */
    @Override
    default Descriptor descriptor() {
        // @formatter:off
        return named("demo_service").withCalls(
                com.lightbend.lagom.javadsl.api.Service.pathCall("/readParams", this::method1)

        ).withAutoAcl(true);
        // @formatter:on
    }

}
