package com.leverx.olingoodata2menagerieservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import static com.leverx.olingoodata2menagerieservice.config.JerseyConfig.EntityManagerFilter.*;
import static org.apache.olingo.odata2.api.processor.ODataContext.HTTP_SERVLET_REQUEST_OBJECT;

@Component
@Slf4j
public class ODataJPAServiceFactoryAdapter extends ODataJPAServiceFactory {

    @Override
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
        ODataJPAContext oDataJPAContext = getODataJPAContext();
        ODataContext oDataContext = oDataJPAContext.getODataContext();

        HttpServletRequest request = (HttpServletRequest) oDataContext.getParameter(
                HTTP_SERVLET_REQUEST_OBJECT);
        EntityManager em = (EntityManager) request
                .getAttribute(EM_REQUEST_ATTRIBUTE);

        oDataJPAContext.setEntityManager(em);
        oDataJPAContext.setPersistenceUnitName("default");
        oDataJPAContext.setContainerManaged(true);

        return oDataJPAContext;
    }

}
