/*
 * codjo.net
 *
 * Common Apache License 2.0
 */
package net.codjo.control.common;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Classe r�alisant le transfert de donn�es de la quarantaine vers la table de controle.
 *
 * @version $Revision: 1.2 $
 */
public interface ShipmentProcessor {
    void execute(Connection con, Dictionary dico, Shipment shipment)
            throws SQLException;
}
