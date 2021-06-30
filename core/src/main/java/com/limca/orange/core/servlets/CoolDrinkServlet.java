package com.limca.orange.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.mail.MessagingException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

@Component(service = Servlet.class, immediate = true, property = { "sling.servlet.methods=GET",
		"sling.servlet.paths=/api/getcooldrinks", "sling.servlet.paths=/api/gethotdrinks" })
public class CoolDrinkServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2839966454844829996L;

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@Reference
	ResourceResolverFactory resolverFactory;
	
	@Reference
	MessageGatewayService gatewayService;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.debug("Some called Cool Drink Servlet");
			Map<String, Object> authMap = new HashMap<>();
			authMap.put(ResourceResolverFactory.SUBSERVICE, "ninosub");
			ResourceResolver serviceResourceResolver = resolverFactory.getServiceResourceResolver(authMap);
			Session serviceSession = serviceResourceResolver.adaptTo(Session.class);
			MailTemplate mailTemplate = MailTemplate.create("/apps/orange/mailtemplates/en.txt", serviceSession);
			Map<String, String> lookupMap = new HashMap<>();
			lookupMap.put("firstname","peansir");
			HtmlEmail email = mailTemplate.getEmail(StrLookup.mapLookup(lookupMap), HtmlEmail.class);
			email.addTo("penasir@gmail.com");
			email.setFrom("nivaz@gmal.com");
			MessageGateway<HtmlEmail> gateway = gatewayService.getGateway(HtmlEmail.class);
			gateway.send(email);
			response.getWriter().write("Some called Cool Drink Servlet " + request.getRequestURI());

		} catch (LoginException e) {
			logger.error(e.getMessage());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}
}
