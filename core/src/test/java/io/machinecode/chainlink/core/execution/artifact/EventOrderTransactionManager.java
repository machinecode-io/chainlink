package io.machinecode.chainlink.core.execution.artifact;

import io.machinecode.chainlink.core.transaction.LocalTransactionManager;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class EventOrderTransactionManager extends LocalTransactionManager {

    public EventOrderTransactionManager(final long duration, final TimeUnit unit) {
        super(duration, unit);
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {
        EventOrderAccumulator._order.add(OrderEvent.BEGIN_TRANSACTION);
        super.begin();
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        EventOrderAccumulator._order.add(OrderEvent.COMMIT_TRANSACTION);
        super.commit();
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        EventOrderAccumulator._order.add(OrderEvent.ROLLBACK_TRANSACTION);
        super.rollback();
    }
}
