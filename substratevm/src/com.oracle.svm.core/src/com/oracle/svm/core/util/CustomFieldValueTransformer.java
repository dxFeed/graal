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

import com.oracle.svm.core.annotate.Inject;
import com.oracle.svm.core.annotate.RecomputeFieldValue;

import jdk.vm.ci.meta.MetaAccessProvider;
import jdk.vm.ci.meta.ResolvedJavaField;

/**
 * Custom recomputation of field values. A class implementing this interface must have a no-argument
 * constructor, which is used to instantiate it before invoking {@link #transform}.
 * <p>
 * In contrast to {@link CustomFieldValueComputer}, the {@link #transform} method also has the
 * original field value as a parameter. This is convenient if the new value depends on the original
 * value, but also requires the original field to be present, e.g., it cannot be use for
 * {@link Inject injected fields}.
 */
public interface CustomFieldValueTransformer extends CustomFieldValueProvider {
    /**
     * Computes the new field value. This method can already be invoked during the analysis,
     * especially when it computes the value of an object field that needs to be visited.
     *
     * @param metaAccess The {@code AnalysisMetaAccess} instance during the analysis or
     *            {@code HostedMetaAccess} instance after the analysis.
     * @param original The original field.
     * @param annotated The field annotated with {@link RecomputeFieldValue}.
     * @param receiver The original object for instance fields, or {@code null} for static fields.
     * @return The new field value.
     */
    Object transform(MetaAccessProvider metaAccess, ResolvedJavaField original, ResolvedJavaField annotated, Object receiver, Object originalValue);
}
