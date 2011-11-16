package net.codjo.control.server.plugin;
import net.codjo.agent.DFService;
import net.codjo.control.common.loader.TransfertData;
import net.codjo.control.common.message.TransferJobRequest;
import net.codjo.sql.server.ConnectionPool;
import net.codjo.sql.server.JdbcServiceUtil;
import net.codjo.workflow.common.message.JobException;
import net.codjo.workflow.common.message.JobRequest;
import net.codjo.workflow.common.protocol.JobProtocolParticipant;
import net.codjo.workflow.server.api.JobAgent;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;
/**
 *
 */
class TransferJobAgent extends JobAgent {
    private static final Logger LOG = Logger.getLogger(TransferJobAgent.class);


    TransferJobAgent(JdbcServiceUtil jdbc, ControlPreference controlPreference, MODE mode) {
        super(new TransferParticipant(jdbc, controlPreference),
              new DFService.AgentDescription(getTransfertDescription()), mode);
    }


    TransferJobAgent(JdbcServiceUtil jdbc, ControlPreference controlPreference) {
        this(jdbc, controlPreference, MODE.NOT_DELEGATE);
    }


    private static DFService.ServiceDescription getTransfertDescription() {
        return new DFService.ServiceDescription(ControlServerPlugin.QUARANTINE_TRANSFER_TYPE,
                                                "control-service");
    }


    private static class TransferParticipant extends JobProtocolParticipant {
        private final JdbcServiceUtil jdbc;
        private final ControlPreference preference;


        TransferParticipant(JdbcServiceUtil jdbc, ControlPreference preference) {
            this.jdbc = jdbc;
            this.preference = preference;
        }


        @Override
        protected void executeJob(JobRequest jobRequest) throws JobException {
            TransferJobRequest request = new TransferJobRequest(jobRequest);
            try {
                TransfertData data = preference.getTransfertData(request);

                ConnectionPool connectionPool = jdbc.getConnectionPool(getAgent(), getRequestMessage());
                Connection connection = connectionPool.getConnection();
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String query;
                    if (TransferJobRequest.Transfer.QUARANTINE_TO_USER == request.getTransferType()) {
                        query = data.getQuarantineToUserQuery(connection);
                    }
                    else if (TransferJobRequest.Transfer.USER_TO_QUARANTINE == request.getTransferType()) {

                        query = data.getUserToQuarantineQuery(connection);
                    }
                    else {
                        throw new JobException("Type de transfert inconnu.");
                    }

                    statement.executeUpdate(query);
                }
                finally {
                    connectionPool.releaseConnection(connection, statement);
                }
            }
            catch (Throwable cause) {
                String message = "Impossible de transferer les donn�es : "
                                 + request.getTransferType()
                                 + "(" + request.getQuarantine() + ", " + request.getUserQuarantine() + ")";
                LOG.error(message, cause);
                throw new JobException(message, cause);
            }
        }
    }
}
