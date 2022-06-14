/*
 * Copyright (c) 2022, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.svm.core.util;

public interface CustomFieldValueProvider {

    enum ValueAvailability {
        /** Value is independent of analysis/compilation results. */
        BeforeAnalysis,
        /** Value depends on data computed by the analysis. */
        AfterAnalysis,
        /** Value depends on data computed during compilation. */
        AfterCompilation
    }

    /**
     * When is the value for this custom computation available? By default, it is assumed that the
     * value is available {@link ValueAvailability#BeforeAnalysis before analysis}, i.e., it doesn't
     * depend on analysis results.
     */
    ValueAvailability valueAvailability();

    /**
     * Specify types that this field can take if the value is not available for analysis. The
     * concrete type of the computed value can be a subtype of one of the specified types as
     * specified by {@link Class#isAssignableFrom(Class)}. If the array contains `null` then the
     * field value can also be null.
     */
    default Class<?>[] types() {
        if (valueAvailability() != ValueAvailability.BeforeAnalysis) {
            throw new IllegalArgumentException("Custom value field whose value is not available during analysis " +
                            "must override CustomFieldValueProvider.types() and specify types for analysis.");
        }
        return null;
    }
}
