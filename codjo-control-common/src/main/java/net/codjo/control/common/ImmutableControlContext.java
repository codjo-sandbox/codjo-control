/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.control.common;
import java.sql.Connection;
/**
 * Contexte utilis� par le plan d'int�gration pour �viter que les controles puissent modifier les donn�es.
 */
class ImmutableControlContext extends ControlContext {
    private static final String IMMUTABLE_ERROR =
          "M�thode immutable ! Il est interdit de changer le contexte.";
    private ControlContext subContext;


    ImmutableControlContext(ControlContext subContext) {
        this.subContext = subContext;
    }


    @Override
    public String getUser() {
        return subContext.getUser();
    }


    @Override
    public String getCurrentRequestId() {
        return subContext.getCurrentRequestId();
    }


    @Override
    public String getPathOfRequest() {
        return subContext.getPathOfRequest();
    }


    @Override
    public Connection getConnection() {
        return subContext.getConnection();
    }


    @Override
    public void setPathOfRequest(String pathOfRequest) {
        throw new UnsupportedOperationException(IMMUTABLE_ERROR);
    }


    @Override
    public void setUser(String user) {
        throw new UnsupportedOperationException(IMMUTABLE_ERROR);
    }


    @Override
    public void setCurrentRequestId(String currentRequestId) {
        throw new UnsupportedOperationException(IMMUTABLE_ERROR);
    }


    @Override
    public void setConnection(Connection connection) {
        throw new UnsupportedOperationException(IMMUTABLE_ERROR);
    }
}
