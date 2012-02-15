/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.arquillian.extension.byteman.impl.common;

import java.util.Arrays;

import org.jboss.arquillian.extension.byteman.api.BMRule;
import org.jboss.arquillian.extension.byteman.impl.dsl.BytemanRuleModel;
import org.jboss.byteman.agent.LocationType;

/**
 * Convert the values in a {@link BMRule} annotation to a {@link BytemanRuleModel}.
 *
 * @author Davide D'Alto
 */
class BytemanRuleModelConverter extends BytemanRuleModel {

    BytemanRuleModelConverter(BMRule bmRule) {
        super(
            bmRule.name(),
            bmRule.targetClass(),
            bmRule.isInterface(),
            bmRule.targetMethod(),
            Arrays.asList(bmRule.action()),
            locationType(bmRule),
            locationParams(bmRule),
            bmRule.condition(),
            bmRule.isOverriding(),
            Arrays.asList(bmRule.binding()),
            bmRule.helper(),
            bmRule.stringParsingEnabled()
        );
    }

    private static String locationParams(BMRule bmRule) {
        if (bmRule.targetLocation() == null)
            return null;

        String location = bmRule.targetLocation();
        String[] split = location.trim().split(" ");
        if (split.length > 2) {
            int indexOf = location.indexOf(split[1]);
            return location.substring(indexOf + split[1].length()).trim();
        }

        return null;
    }

    private static LocationType locationType(BMRule bmRule) {
        if (bmRule.targetLocation() == null)
            return null;

        String location = bmRule.targetLocation();
        String[] split = location.trim().split(" ");
        if (split.length < 2)
            return null;

        String loc = split[1].toUpperCase();
        if ("AFTER".equals(split[0]))
            loc += "_COMPLETED";

        return LocationType.valueOf(loc);
    }
}
