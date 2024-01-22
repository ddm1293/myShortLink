package org.myShortLink.admin.config;

import cn.hutool.core.lang.Snowflake;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import static cn.hutool.core.util.IdUtil.getSnowflake;

public class SnowflakeIdGenerator implements IdentifierGenerator {
    public static long WORKED_ID = 1;

    public static long DATACENTER_ID = 1;

    private final Snowflake snowflake = getSnowflake(WORKED_ID, DATACENTER_ID);

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return snowflakeId();
    }
}
