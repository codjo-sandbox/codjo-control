package net.codjo.control.server.plugin;
/**
 * Exception g�n�rique propag�e hors du module control.
 */
class QuarantineControlException extends Exception {
    private final PostControlAudit postControlAudit;


    QuarantineControlException(PostControlAudit postControlAudit, Throwable cause) {
        super(cause);
        this.postControlAudit = postControlAudit;
    }


    public PostControlAudit getPostControlAudit() {
        return postControlAudit;
    }
}
