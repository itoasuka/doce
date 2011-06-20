package jp.osd.doce.internal.tx;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import jp.osd.doce.Doma;
import jp.osd.doce.DoceException;
import jp.osd.doce.Transaction;
import jp.osd.doce.internal.logging.Logger;
import jp.osd.doce.internal.logging.LoggerFactory;
import jp.osd.doce.internal.logging.MessageCodes;

import com.google.inject.Inject;

/**
 * JTA の {@link UserTransaction} をラップするトランザクションクラスです。
 *
 * @author asuka
 */
public class JtaUserTransaction implements Transaction {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JtaUserTransaction.class);

    private final UserTransaction tx;

    /**
     * 新たにオブジェクトを構築します。
     *
     * @param tx
     *            ラップ対象のユーザトランザクション
     */
    @Inject
    public JtaUserTransaction(@Doma UserTransaction tx) {
        LOGGER.logConstructor(UserTransaction.class);
        this.tx = tx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        try {
            tx.begin();
            LOGGER.debug(MessageCodes.DG016);
        } catch (Exception e) {
            LOGGER.error(e, MessageCodes.DG017);
            throw new DoceException("Transaction begin error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        try {
            if (isActive()) {
                tx.commit();
                LOGGER.debug(MessageCodes.DG018);
            }
        } catch (Exception e) {
            LOGGER.error(e, MessageCodes.DG019);
            throw new DoceException("Transaction commit error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() {
        try {
            if (isActive()) {
                tx.rollback();
                LOGGER.debug(MessageCodes.DG020);
            }
        } catch (Exception e) {
            LOGGER.error(e, MessageCodes.DG021);
            throw new DoceException("Transaction rollback error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive() {
        try {
            return tx.getStatus() == Status.STATUS_ACTIVE;
        } catch (Exception e) {
            throw new DoceException("Transaction status getting error", e);
        }
    }

}
