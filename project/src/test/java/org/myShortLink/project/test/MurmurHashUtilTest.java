package org.myShortLink.project.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.myShortLink.project.utils.MurmurHashUtil;

@Slf4j
public class MurmurHashUtilTest {

    @Test
    void testConvertWithGuava() {
        String str = "http://qrrxi.ba/qqqzr";
        String hashed = MurmurHashUtil.convertWithGuava(str);
        log.debug("see hashed with guava: {}", hashed);
    }
}
