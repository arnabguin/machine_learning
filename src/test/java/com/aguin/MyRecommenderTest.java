package com.aguin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple MyRecommender.
 */
public class MyRecommenderTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MyRecommenderTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MyRecommenderTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testMyRecommender()
    {
        assertTrue( true );
    }
}
