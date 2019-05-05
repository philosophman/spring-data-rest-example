package bankaccount.config;

import bankaccount.domain.BankAccount;
import bankaccount.web.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Configuration
public class WebConfig {
    public static final Link URL_TRANSFER = linkTo(methodOn(Controller.class).transferMoney(null)).withRel("transfers");

    @Bean // add transfer-link to bank account resource.
    public ResourceProcessor<Resource<BankAccount>> bankAccountResource() {
        return resource -> {
            resource.add(URL_TRANSFER);
            return resource;
        };
    }
}
