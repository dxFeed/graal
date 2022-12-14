/*
 * Copyright (c) 2017, 2017, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.svm.core.jni;

import org.graalvm.nativeimage.c.struct.SizeOf;

import com.oracle.svm.core.jni.functions.JNIFunctionTables;
import com.oracle.svm.core.jni.headers.JNIEnvironment;
import com.oracle.svm.core.threadlocal.FastThreadLocalBytes;
import com.oracle.svm.core.threadlocal.FastThreadLocalFactory;

/**
 * Handles accesses to each thread's JNI environment.
 */
public class JNIThreadLocalEnvironment {

    /*
     * The per-thread JNI environment is located at offset 0 of each thread's {@link VMThread}
     * structure. For that reason, it has the same address as the VMThread and can be used to
     * restore the designated VMThread register when transitioning from native to Java.
     */
    static final FastThreadLocalBytes<JNIEnvironment> jniFunctions = FastThreadLocalFactory.createBytes(() -> SizeOf.get(JNIEnvironment.class), "JNIThreadLocalEnvironment.jniFunctions")
                    .setMaxOffset(0);

    public static JNIEnvironment getAddress() {
        JNIEnvironment env = jniFunctions.getAddress();
        if (env.getFunctions().isNull()) {
            env.setFunctions(JNIFunctionTables.singleton().getGlobalFunctionTable());
        }
        return env;
    }
}
