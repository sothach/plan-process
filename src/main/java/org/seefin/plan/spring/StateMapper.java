package org.seefin.plan.spring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Type converter for Spring to use when setting <code>State</code> values from String 
 * configuration properties
 * 
 * @author phillipsr
 *
 */
public class StateMapper
{
	/**
	 * Return a <code>State</code> object whose String value is that
	 * of the supplied <code>stateLabel</code> in the <code>stateClass</code>
	 * 
	 * @param stateClass class of the state values
	 * @param stateLabel the state's string form
	 * @return
	 * @throws IllegalArgumentException if any of the arguments are null, or if the state class
	 * 						is not an enum value
	 */
	static Object
	mapState ( Class<?> stateClass, String stateLabel)
	{
		if (  stateClass == null || stateClass.isEnum () == false)
		{
			throw new IllegalArgumentException ( "state-class must be non-null and an enum");
		}
		if ( stateLabel == null)
		{
			throw new IllegalArgumentException ("String-form of state must be provided");
		}

		return getStates ( stateClass).get ( stateLabel);
	}
	
	/**
	 * Answer with a set of states, initialized from the supplied state class
	 * 
	 * @param stateClass class of the state values
	 * @param setList comma-separated list of state labels to be converted to their state from
	 * @return a set object containing the state values represented in the supplied list
	 * @throws IllegalArgumentException if any of the arguments are null, or if the state class
	 * 						is not an enum value
	 */
	static Set<Object>
	labelsToStateSet ( Class<?> stateClass, String setList)
	{
		if (  stateClass == null || stateClass.isEnum () == false)
		{
			throw new IllegalArgumentException ( "state-class must be non-null and an enum");
		}
		if ( setList == null)
		{
			throw new IllegalArgumentException ("String-form of set must be provided");
		}
		final String[] items = setList.split ( ",");
		if ( items == null || items.length < 1)
		{
			throw new IllegalArgumentException ("String-form must be comma-seperated list");
		}

		final Map<String, Object> stateMap = getStates ( stateClass);
		final Set<Object> result = new HashSet<Object>(items.length);
		for ( String terminal : items)
		{
			Object state = stateMap.get ( terminal.trim());
			if ( state != null)
			{
				result.add ( state);
			}
		}
		return result;
	}
	
	/**
	 * Return a set of state objects representing the list of states,
	 * using the supplied map
	 * 
	 * @param stateMapa map of state labels to state instances
	 * @param stateList a comma-separated list of state labels
	 * @return a set object containing the state values represented in the supplied list
	 */
	public static Set<Object>
	convertToStateSet ( Map<String, Object> stateMap, String stateList)
	{
		final String[] items = stateList.split ( ",");
		final Set<Object> result = new HashSet<Object>(items.length);
		for ( String terminal : items)
		{
			Object state = stateMap.get ( terminal.trim());
			if ( state != null)
			{
				result.add ( state);
			}
		}
		return result;
	}
	
	/**
	 * Return a map of state labels to state object, representing the values 
	 * of the supplied state (enum) class
	 * 
	 * @param stateClass a State sub-class implemented as an enum
	 * @return a map of state labels to state instances
	 * @throws IllegalArgumentException if stateClass is null, or is not an enum value
	 */
	static Map<String, Object> 
	getStates ( Class<?> stateClass)
	{
		if (  stateClass == null || stateClass.isEnum () == false)
		{
			throw new IllegalArgumentException ( "state-class must be non-null and an enum");
		}
		final Object[] allStates = stateClass.getEnumConstants ();
		final Map<String,Object> stateMap = new HashMap<String,Object>(allStates.length);
		for ( Object terminal : allStates)
		{
			stateMap.put ( terminal.toString(), terminal);
		}
		return stateMap;
	}

}