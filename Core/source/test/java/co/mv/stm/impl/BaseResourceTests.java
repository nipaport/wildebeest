package co.mv.stm.impl;

import co.mv.stm.model.AssertionFailedException;
import co.mv.stm.model.AssertionResult;
import co.mv.stm.model.AssertionType;
import co.mv.stm.model.IndeterminateStateException;
import co.mv.stm.model.State;
import co.mv.stm.model.Transition;
import co.mv.stm.model.TransitionFailedException;
import co.mv.stm.model.TransitionNotPossibleException;
import co.mv.stm.model.impl.ImmutableState;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class BaseResourceTests
{
	
	//
	// assertState()
	//
	
	@Test public void assertStateWithNoAssertionsSuccessful() throws IndeterminateStateException
	{
		
		//
		// Fixture Setup
		//
		
		UUID knownStateId = UUID.randomUUID();
		
		State state = new ImmutableState(knownStateId);
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource", state);
		resource.getStates().add(state);
		
		FakeResourceInstance instance = new FakeResourceInstance();

		//
		// Execute
		//

		List<AssertionResult> results = resource.assertState(instance);

		//
		// Assert Results
		//

		Assert.assertNotNull("results", results);
		Assert.assertEquals("results.size", 0, results.size());
			
	}
	
	@Test public void assertStateWithOneAssertionSuccessful() throws IndeterminateStateException
	{
		
		//
		// Fixture Setup
		//
		
		UUID knownStateId = UUID.randomUUID();
		
		State state = new ImmutableState(knownStateId);
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource", state);
		UUID assertionId = UUID.randomUUID();
		state.getAssertions().add(new FakeAssertion(
			assertionId,
			"Fake1",
			0,
			AssertionType.DatabaseRowExists,
			true,
			"Fake1 passed"));
		resource.getStates().add(state);
		
		FakeResourceInstance instance = new FakeResourceInstance();

		//
		// Execute
		//

		List<AssertionResult> results = resource.assertState(instance);

		//
		// Assert Results
		//

		Assert.assertNotNull("results", results);
		Assert.assertEquals("results.size", 1, results.size());
		assertAssertionResult(assertionId, true, "Fake1 passed", results.get(0), "results[0]");
	}
	
	@Test public void assertStateWithMultipleAssertionsSuccessful() throws IndeterminateStateException
	{
		
		//
		// Fixture Setup
		//
		
		UUID knownStateId = UUID.randomUUID();
		
		State state = new ImmutableState(knownStateId);
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource", state);

		UUID assertion1Id = UUID.randomUUID();
		state.getAssertions().add(new FakeAssertion(
			assertion1Id,
			"Fake1",
			0,
			AssertionType.DatabaseRowExists,
			true,
			"Fake1 passed"));
		
		UUID assertion2Id = UUID.randomUUID();
		state.getAssertions().add(new FakeAssertion(
			assertion2Id,
			"Fake2",
			1,
			AssertionType.DatabaseRowExists,
			false,
			"Fake2 failed"));

		resource.getStates().add(state);
		
		FakeResourceInstance instance = new FakeResourceInstance();

		//
		// Execute
		//

		List<AssertionResult> results = resource.assertState(instance);

		//
		// Assert Results
		//

		Assert.assertNotNull("results", results);
		Assert.assertEquals("results.size", 2, results.size());
		assertAssertionResult(assertion1Id, true, "Fake1 passed", results.get(0), "results[0]");
		assertAssertionResult(assertion2Id, false, "Fake2 failed", results.get(1), "results[1]");
	}

	/**
	 * Verifies that when the internal call to currentState() results in an IndeterminateStateException, assertState
	 * handles that properly.
	 */
	@Test public void assertStateForResourceIndeterminateState()
	{
		throw new UnsupportedOperationException();
	}

	@Test public void assertStateStateForFaultingAssertion()
	{
		throw new UnsupportedOperationException();
	}
	
	//
	// transitionTo()
	//

	@Test public void transitionFromNullToFirstState() throws
		IndeterminateStateException,
		AssertionFailedException,
		TransitionNotPossibleException,
		TransitionFailedException
	{
		
		//
		// Fixture Setup
		//

		// The resource
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource");

		// State 1
		UUID state1Id = UUID.randomUUID();
		State state = new ImmutableState(state1Id);
		state.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "foo"));
		resource.getStates().add(state);
		
		// Transition 1
		UUID transition1Id = UUID.randomUUID();
		Transition tran1 = new FakeTransition(transition1Id, state1Id, "foo");
		resource.getTransitions().add(tran1);
		
		// Instance
		FakeResourceInstance instance = new FakeResourceInstance();
		
		//
		// Execute
		//
		
		resource.transition(instance, state1Id);
		
		//
		// Assert Results
		//
		
		Assert.assertEquals("instance.tag", "foo", instance.getTag());
		
	}
	
	@Test public void transitionFromNullToDeepState() throws
		IndeterminateStateException,
		AssertionFailedException,
		TransitionNotPossibleException,
		TransitionFailedException
	{
		
		//
		// Fixture Setup
		//

		// The resource
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource");

		// State 1
		UUID state1Id = UUID.randomUUID();
		State state1 = new ImmutableState(state1Id);
		state1.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "foo"));
		resource.getStates().add(state1);

		// State 2
		UUID state2Id = UUID.randomUUID();
		State state2 = new ImmutableState(state2Id);
		state2.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "bar"));
		resource.getStates().add(state2);

		// State 3
		UUID state3Id = UUID.randomUUID();
		State state3 = new ImmutableState(state3Id);
		state3.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "bup"));
		resource.getStates().add(state3);
		
		// Transition null -> State1
		UUID transition1Id = UUID.randomUUID();
		Transition tran1 = new FakeTransition(transition1Id, state1Id, "foo");
		resource.getTransitions().add(tran1);
		
		// Transition State1 -> State2
		UUID transition2Id = UUID.randomUUID();
		Transition tran2 = new FakeTransition(transition2Id, state1Id, state2Id, "bar");
		resource.getTransitions().add(tran2);
		
		// Transition State2 -> State3
		UUID transition3Id = UUID.randomUUID();
		Transition tran3 = new FakeTransition(transition3Id, state2Id, state3Id, "bar");
		resource.getTransitions().add(tran3);
		
		
		// Instance
		FakeResourceInstance instance = new FakeResourceInstance();
		
		//
		// Execute
		//
		
		resource.transition(instance, state3Id);
		
		//
		// Assert Results
		//
		
		Assert.assertEquals("instance.tag", "bup", instance.getTag());
		
	}
	
	@Test public void transitionFromNullToDeepStateWithMultipleBranches() throws
		IndeterminateStateException,
		AssertionFailedException,
		TransitionNotPossibleException,
		TransitionFailedException
	{
		
		//
		// Fixture Setup
		//

		// The resource
		FakeResource resource = new FakeResource(UUID.randomUUID(), "Resource");

		// State 1
		UUID state1Id = UUID.randomUUID();
		State state1 = new ImmutableState(state1Id);
		state1.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "state1"));
		resource.getStates().add(state1);

		// State B2
		UUID stateB2Id = UUID.randomUUID();
		State stateB2 = new ImmutableState(stateB2Id);
		stateB2.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "stateB2"));
		resource.getStates().add(stateB2);

		// State B3
		UUID stateB3Id = UUID.randomUUID();
		State stateB3 = new ImmutableState(stateB3Id);
		stateB3.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "stateB3"));
		resource.getStates().add(stateB3);

		// State C2
		UUID stateC2Id = UUID.randomUUID();
		State stateC2 = new ImmutableState(stateC2Id);
		stateC2.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "stateC2"));
		resource.getStates().add(stateC2);

		// State C3
		UUID stateC3Id = UUID.randomUUID();
		State stateC3 = new ImmutableState(stateC3Id);
		stateC3.getAssertions().add(new TagAssertion(UUID.randomUUID(), "Check tag", 0, "stateC3"));
		resource.getStates().add(stateC3);
		
		// Transition null -> State1
		UUID transition1Id = UUID.randomUUID();
		Transition tran1 = new FakeTransition(transition1Id, state1Id, "state1");
		resource.getTransitions().add(tran1);
		
		// Transition State1 -> StateB2
		UUID transition2Id = UUID.randomUUID();
		Transition tran2 = new FakeTransition(transition2Id, state1Id, stateB2Id, "stateB2");
		resource.getTransitions().add(tran2);
		
		// Transition StateB2 -> StateB3
		UUID transition3Id = UUID.randomUUID();
		Transition tran3 = new FakeTransition(transition3Id, stateB2Id, stateB3Id, "stateB3");
		resource.getTransitions().add(tran3);
		
		// Transition State1 -> StateC2
		UUID transition4Id = UUID.randomUUID();
		Transition tran4 = new FakeTransition(transition4Id, state1Id, stateC2Id, "stateC2");
		resource.getTransitions().add(tran4);
		
		// Transition StateC2 -> StateC3
		UUID transition5Id = UUID.randomUUID();
		Transition tran5 = new FakeTransition(transition5Id, stateC2Id, stateC3Id, "stateC3");
		resource.getTransitions().add(tran5);
		
		// Instance
		FakeResourceInstance instance = new FakeResourceInstance();
		
		//
		// Execute
		//
		
		resource.transition(instance, stateB3Id);
		
		//
		// Assert Results
		//
		
		Assert.assertEquals("instance.tag", "stateB3", instance.getTag());
		
	}
	
	@Test public void transitionFromStateToState()
	{
		throw new UnsupportedOperationException();
	}
	
	@Test public void transitionFromStateToDeepState()
	{
		throw new UnsupportedOperationException();
	}
	
	@Test public void transitionWithCircularDependencyFails()
	{
		throw  new UnsupportedOperationException();
	}
	
	public static void assertAssertionResult(
		UUID expectedAssertionId,
		boolean expectedResult,
		String expectedMessage,
		AssertionResult actual,
		String name)
	{
		if (expectedAssertionId == null) { throw new IllegalArgumentException("expectedAssertionId"); }
		if (expectedMessage == null) { throw new IllegalArgumentException("expectedMessage"); }
		if (actual == null) { throw new IllegalArgumentException("actual"); }
		if (name == null) { throw new IllegalArgumentException("name"); }
		if ("".equals(name)) { throw new IllegalArgumentException("name cannot be blank"); }

		Assert.assertEquals(name + ".assertionId", expectedAssertionId, actual.getAssertionId());
		Assert.assertEquals(name + ".result", expectedResult, actual.getResult());
		Assert.assertEquals(name + ".message", expectedMessage, actual.getMessage());
	}
}