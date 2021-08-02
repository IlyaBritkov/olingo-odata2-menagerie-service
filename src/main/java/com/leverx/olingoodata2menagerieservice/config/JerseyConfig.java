package com.leverx.olingoodata2menagerieservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.rest.ODataRootLocator;
import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Component
@ApplicationPath("/odata")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(ODataJPAServiceFactoryAdapter serviceFactory, EntityManagerFactory emf) {
        ODataApplication app = new ODataApplication();

        app
                .getClasses()
                .forEach(clazz -> {
                    if (!ODataRootLocator.class.isAssignableFrom(clazz)) {
                        register(clazz);
                    }
                });

        register(new CustomODataRootLocator(serviceFactory));
        register(new EntityManagerFilter(emf));
    }

    @RequiredArgsConstructor
    @Path("/")
    public static class CustomODataRootLocator extends ODataRootLocator {

        private final ODataJPAServiceFactoryAdapter serviceFactory;

        @Override
        public ODataServiceFactory getServiceFactory() {
            return this.serviceFactory;
        }
    }

    @RequiredArgsConstructor
    @Provider
    public static class EntityManagerFilter implements ContainerRequestFilter, ContainerResponseFilter {

        public static final String EM_REQUEST_ATTRIBUTE = EntityManagerFilter.class.getName() + "_ENTITY_MANAGER";

        private final EntityManagerFactory emf;

        @Context
        private HttpServletRequest httpRequest;


        @Override
        public void filter(ContainerRequestContext context) {
            EntityManager em = this.emf.createEntityManager();

            httpRequest.setAttribute(EM_REQUEST_ATTRIBUTE, em);

            if (!"GET".equalsIgnoreCase(context.getMethod())) {
                em.getTransaction().begin();
            }

        }

        @Override
        public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
            EntityManager em = (EntityManager) httpRequest.getAttribute(EM_REQUEST_ATTRIBUTE);

            if (!"GET".equalsIgnoreCase(requestContext.getMethod())) {
                EntityTransaction transaction = em.getTransaction();

                if (transaction.isActive() && !transaction.getRollbackOnly()) {
                    transaction.commit();
                }

            }

            em.close();
        }
    }

}
