package org.seefin.plan.tests;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Configure a process in this class's default context, and execute it
 * 
 * @author royp
 *
 */

public class NegativeProcessDefs
{
	private static final String BAD01_CONTEXT 
		= "org/seefin/plan/tests/BadProcess01-context.xml";
	private static final String BAD02_CONTEXT 
		= "org/seefin/plan/tests/BadProcess02-context.xml";
	private static final String BAD03_CONTEXT 
		= "org/seefin/plan/tests/BadProcess03-context.xml";	
	private static final String BAD04_CONTEXT 
		= "org/seefin/plan/tests/BadProcess04-context.xml";	
	private static final String BAD05_CONTEXT 
		= "org/seefin/plan/tests/BadProcess05-context.xml";	
	private static final String BAD06_CONTEXT 
		= "org/seefin/plan/tests/BadProcess06-context.xml";	
	
	@Test
	public void
	testMissingAction()
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD01_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( BeanDefinitionStoreException e)
		{
			Assert.assertTrue ( e.getMessage ().startsWith (  
					"Unexpected exception parsing XML document from class path resource ["
					+ BAD01_CONTEXT +"]"));
			Assert.assertEquals ( e.getCause ().getMessage (), 
					"expected node action to be defined by a (single) process element");
		}
	}
	
	@Test
	public void
	testEmptyProcess()
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD02_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( Exception e)
		{
			Assert.assertTrue ( e.getMessage ().contains ( 
					"The content of element 'plan:process' is not complete"));
		}
	}
	
	@Test
	public void
	testUnknownState()
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD03_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( Exception e)
		{
			Assert.assertTrue ( e.getMessage ().contains ( 
					"State [STRANGE] not defined in process's state-class"));
		}
	}
	
	@Test
	public void
	testUnknownActionClass()
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD04_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( Exception e)
		{
			Assert.assertTrue ( e.getMessage ().contains ( 
					"could load action class for action: validator"));
		}
	}	
	
	@Test(expected=java.lang.IllegalArgumentException.class)
	public void
	testInvalidStates()
		throws Throwable
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD05_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( BeanDefinitionStoreException e)
		{
			throw e.getCause ();
		}
	}	
	
	@Test
	public void
	testBeanrefMissing()
	{
		try
		{
			new ClassPathXmlApplicationContext(BAD06_CONTEXT);  
			Assert.assertTrue ( "context loading should have failed", false);
		}
		catch ( Exception e)
		{
			Assert.assertTrue ( e.getMessage ().contains ( 
					"Cannot resolve reference to bean 'validator' while setting bean property 'action'"));
		}
	}	
	
}
