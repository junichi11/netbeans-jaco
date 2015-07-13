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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author junichi11
 */
public class JacoTest {

    public JacoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of katakanize method, of class Jaco.
     */
    @Test
    public void testKatakanize() {
        assertEquals("カタカナ", Jaco.getInstance().katakanize("ｶﾀｶﾅ"));
        assertEquals("カタカナ", Jaco.getInstance().katakanize("かたかな"));
        assertEquals("カタカナカタカナ", Jaco.getInstance().katakanize("かたかなカタカナ"));
        assertEquals("abc123カタカナ日本語!$%&-", Jaco.getInstance().katakanize("abc123かたかな日本語!$%&-"));
        assertEquals(null, Jaco.getInstance().katakanize(null));
    }

    /**
     * Test of hiraganize method, of class Jaco.
     */
    @Test
    public void testHiraganize() {
        assertEquals("ひらがな", Jaco.getInstance().hiraganize("ﾋﾗｶﾞﾅ"));
        assertEquals("ひらがな", Jaco.getInstance().hiraganize("ヒラガナ"));
        assertEquals("ひらがなひらがな", Jaco.getInstance().hiraganize("ヒラガナひらがな"));
        assertEquals("abc123ひらがな日本語!$%&-", Jaco.getInstance().hiraganize("abc123ヒラガナ日本語!$%&-"));
        assertEquals(null, Jaco.getInstance().hiraganize(null));
    }

}
