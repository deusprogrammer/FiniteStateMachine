package com.trinary.fsm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class FiniteStateMachine {
	public class FiniteState {
		protected String name;
		protected Map<String, FiniteState> transitions = new HashMap<String, FiniteState>();
		
		public FiniteState(String name) {
			super();
			this.name = name;
		}

		protected void link(FiniteState state) {
			this.transitions.put(state.name, state);
		}

		public Map<String, FiniteState> getTransitions() {
			return transitions;
		}
		
		public String getName() {
			return name;
		}
	}
	
	protected FiniteState currentState = null;
	protected Map<String, FiniteState> allStates = new HashMap<String, FiniteState>();;

	public FiniteStateMachine(String fsmDescriptorFile) throws IOException {
		super();
		
		Properties props = new Properties();
		InputStream stream = new FileInputStream(new File(fsmDescriptorFile));
		props.load(stream);
		
		for (Entry<Object, Object> entry : props.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			
			// Create new state or get if already exists
			FiniteState state = null;
			if (allStates.containsKey(key)) {
				state = allStates.get(key);
			} else {
				state = new FiniteState(key);
				allStates.put(key, state);
			}
			
			if (key.equals("START")) {
				currentState = state;
			}
			
			// Link all valid transitions
			String[] linkNames = val.split(",");
			for (String linkName : linkNames) {
				linkName = linkName.trim();
				FiniteState link = null;
				if (allStates.containsKey(linkName)) {
					link = allStates.get(linkName);
					state.link(link);
				} else {
					link = new FiniteState(linkName);
					state.link(link);
					allStates.put(linkName, link);
				}
			}
		}
	}

	public void validateTransition(String fromStateName, String toStateName) {
		// Get the transitions named by fromStateName and toStateName.
		FiniteState from = allStates.get(fromStateName);
		FiniteState to   = allStates.get(toStateName);
		
		// Check to see if the named transitions even exist.
		if (from == null) {
			throw new IllegalStateException(fromStateName + " is not a valid state");
		}
		if (to == null) {
			throw new IllegalStateException(toStateName + " is not a valid state");
		}
		
		// Check if the named transition point is valid and that the transition and the target are the same.
		FiniteState transition = from.getTransitions().get(toStateName);
		if (transition != null && transition == to) {
			return;
		} else {
			throw new IllegalStateException(fromStateName + " to " + toStateName + " is an invalid transition.");
		}
	}
	
	public FiniteState transition(String toStateName) {
		if (!allStates.containsKey(toStateName)) {
			throw new IllegalStateException(toStateName + " is not a valid state");
		}
		
		FiniteState state = currentState.getTransitions().get(toStateName);
		
		if (state == null) {
			throw new IllegalStateException(currentState.getName() + " to " + toStateName + " is an invalid transition.");
		}
		
		currentState = state;
		
		return currentState;
	}
	
	public FiniteState getCurrentState() {
		return currentState;
	}
}