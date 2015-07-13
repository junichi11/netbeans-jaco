/*
 * The MIT License
 *
 * Copyright 2015 junichi11.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.junichi11.netbeans.modules.jaco;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.netbeans.api.annotations.common.CheckForNull;

/**
 * Represent for jaco lib.
 *
 * @author junichi11
 */
public final class Jaco {

    private static final Jaco INSTACNE = new Jaco();
    private static Context context;
    private static Scriptable scope;
    private static final String RESOURCES = "resources"; // NOI18N
    private static final Logger LOGGER = Logger.getLogger(Jaco.class.getName());

    private Jaco() {
        Global global = new Global();
        context = ContextFactory.getGlobal().enterContext();
        global.init(context);
        scope = context.initStandardObjects(global);
        Reader reader = getReaderForLocalFile("jaco.js"); // NOI18N
        if (reader != null) {
            try {
                context.evaluateReader(scope, reader, "jaco.js", 1, null); // NOI18N
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Can't read jaco.js: {0}", ex.getMessage()); // NOI18N
            }
        }
    }

    public static Jaco getInstance() {
        return INSTACNE;
    }

    public Object execJSFunction(String name, Object... vargs) {
        Object wrappedObj;
        StringBuilder jsArgs = new StringBuilder();
        for (int i = 0; i < vargs.length; i++) {
            wrappedObj = Context.javaToJS(vargs[i], scope);
            ScriptableObject.putProperty(scope, "javaObjectParam" + i, wrappedObj); // NOI18N
            if (i > 0) {
                jsArgs.append(','); // NOI18N
            }
            jsArgs.append("javaObjectParam").append(i); // NOI18N
        }

        Object result = context.evaluateString(scope, name + "(" + jsArgs.toString() + ");", "<cmd>", 1, null); // NOI18N

        for (int i = 0; i < vargs.length; i++) {
            ScriptableObject.deleteProperty(scope, "javaObjectParam" + i);
        }

        return result;
    }

    public String katakanize(String text) {
        return runStaticMethod("katakanize", text); // NOI18N
    }

    public String hiraganize(String text) {
        return runStaticMethod("hiraganize", text); // NOI18N
    }

    @CheckForNull
    public String runStaticMethod(String name, String text) {
        if (text == null) {
            return null;
        }
        return Context.toString(execJSFunction("global.jaco." + name, text)); // NOI18N
    }

    @CheckForNull
    private Reader getReaderForLocalFile(String fileName) {
        String localFilePath = getLocalFilePath(fileName);
        InputStream inputStream = this.getClass().getResourceAsStream(localFilePath);
        try {
            return new InputStreamReader(inputStream, "UTF-8"); // NOI18N
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        return null;
    }

    private static String getLocalFilePath(String fileName) {
        return RESOURCES + "/" + fileName; // NOI18N
    }

}
