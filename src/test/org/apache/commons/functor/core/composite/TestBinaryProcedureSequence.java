/* 
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons-sandbox//functor/src/test/org/apache/commons/functor/core/composite/Attic/TestBinaryProcedureSequence.java,v 1.2 2003/02/24 11:48:09 rwaldhoff Exp $
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived 
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.commons.functor.core.composite;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.functor.BaseFunctorTest;
import org.apache.commons.functor.BinaryProcedure;
import org.apache.commons.functor.core.NoOp;

/**
 * @version $Revision: 1.2 $ $Date: 2003/02/24 11:48:09 $
 * @author Rodney Waldhoff
 */
public class TestBinaryProcedureSequence extends BaseFunctorTest {

    // Conventional
    // ------------------------------------------------------------------------

    public TestBinaryProcedureSequence(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestBinaryProcedureSequence.class);
    }

    // Functor Testing Framework
    // ------------------------------------------------------------------------

    protected Object makeFunctor() {
        return new BinaryProcedureSequence(new NoOp(),new NoOp());
    }

    // Lifecycle
    // ------------------------------------------------------------------------

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    // Tests
    // ------------------------------------------------------------------------
    
    public void testRunZero() throws Exception {
        BinaryProcedureSequence seq = new BinaryProcedureSequence();
        seq.run(null,null);
        seq.run("xyzzy","xyzzy");
    }

    public void testRunOne() throws Exception {
        RunCounter counter = new RunCounter();
        BinaryProcedureSequence seq = new BinaryProcedureSequence(counter);
        assertEquals(0,counter.count);
        seq.run(null,null);
        assertEquals(1,counter.count);
        seq.run("xyzzy","xyzzy");
        assertEquals(2,counter.count);
    }

    public void testRunTwo() throws Exception {
        RunCounter[] counter = { new RunCounter(), new RunCounter() };
        BinaryProcedureSequence seq = new BinaryProcedureSequence(counter[0],counter[1]);
        assertEquals(0,counter[0].count);
        assertEquals(0,counter[1].count);
        seq.run(null,null);
        assertEquals(1,counter[0].count);
        assertEquals(1,counter[1].count);
        seq.run("xyzzy","xyzzy");
        assertEquals(2,counter[0].count);
        assertEquals(2,counter[1].count);
    }
    
    public void testThen() throws Exception {
        List list = new ArrayList();
        BinaryProcedureSequence seq = new BinaryProcedureSequence();
        seq.run(null,null);        
        for(int i=0;i<10;i++) {
            RunCounter counter = new RunCounter();
            seq.then(counter);
            list.add(counter);
            seq.run("xyzzy","xyzzy");
            for(int j=0;j<list.size();j++) {
                assertEquals(list.size()-j,(((RunCounter)(list.get(j))).count));
            }
        }
    }
    
    public void testEquals() throws Exception {
        BinaryProcedureSequence p = new BinaryProcedureSequence();
        assertEquals(p,p);
        BinaryProcedureSequence q = new BinaryProcedureSequence();
        assertObjectsAreEqual(p,q);

        for(int i=0;i<3;i++) {
            p.then(new NoOp());
            assertObjectsAreNotEqual(p,q);
            q.then(new NoOp());
            assertObjectsAreEqual(p,q);
            p.then(new BinaryProcedureSequence(new NoOp(),new NoOp()));
            assertObjectsAreNotEqual(p,q);            
            q.then(new BinaryProcedureSequence(new NoOp(),new NoOp()));
            assertObjectsAreEqual(p,q);            
        }
                
        assertObjectsAreNotEqual(p,new NoOp());
    }

    // Classes
    // ------------------------------------------------------------------------
    
    static class RunCounter implements BinaryProcedure {        
        public void run(Object a, Object b) {
            count++;    
        }        
        public int count = 0;
    }
}
