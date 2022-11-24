/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.sql.parser.internal.asserts.segment.distsql.rdl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.sharding.distsql.parser.segment.strategy.AuditStrategySegment;
import org.apache.shardingsphere.sharding.distsql.parser.segment.strategy.ShardingAuditorSegment;
import org.apache.shardingsphere.test.sql.parser.internal.asserts.SQLCaseAssertContext;
import org.apache.shardingsphere.test.sql.parser.internal.asserts.segment.distsql.AlgorithmAssert;
import org.apache.shardingsphere.test.sql.parser.internal.cases.parser.domain.segment.impl.distsql.rdl.ExpectedAuditStrategy;
import org.apache.shardingsphere.test.sql.parser.internal.cases.parser.domain.segment.impl.distsql.rdl.ExpectedShardingAuditor;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Audit strategy assert.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditStrategyAssert {
    
    /**
     * Assert audit strategy is correct with expected parser result.
     *
     * @param assertContext assert context
     * @param actual actual audit strategy
     * @param expected expected audit strategy test case
     */
    public static void assertIs(final SQLCaseAssertContext assertContext, final AuditStrategySegment actual, final ExpectedAuditStrategy expected) {
        if (null == expected) {
            assertNull(assertContext.getText("Actual strategy should not exist."), actual);
        } else {
            assertThat(actual.isAllowHintDisable(), is(expected.isAllowHintDisable()));
            assertNotNull(assertContext.getText("Actual sharding auditor segment should exist."), actual.getAuditorSegments());
            assertThat(actual.getAuditorSegments().size(), is(expected.getAuditors().size()));
            Iterator<ShardingAuditorSegment> auditorSegmentIterator = actual.getAuditorSegments().iterator();
            Iterator<ExpectedShardingAuditor> expectedShardingAuditorIterator = expected.getAuditors().iterator();
            while (auditorSegmentIterator.hasNext() && expectedShardingAuditorIterator.hasNext()) {
                ShardingAuditorSegment shardingAuditorSegment = auditorSegmentIterator.next();
                ExpectedShardingAuditor expectedShardingAuditor = expectedShardingAuditorIterator.next();
                assertThat(shardingAuditorSegment.getAuditorName(), is(expectedShardingAuditor.getAuditorName()));
                AlgorithmAssert.assertIs(assertContext, shardingAuditorSegment.getAlgorithmSegment(), expectedShardingAuditor.getAlgorithmSegment());
            }
        }
    }
}