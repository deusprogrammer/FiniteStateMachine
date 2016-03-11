package com.trinary.fsm;

import java.io.IOException;

public class Application {
	public static void main(String[] args) throws IOException {
		FiniteStateMachine fsm = new FiniteStateMachine("fsm_descriptor.txt");
		
		// Passing scenario
		try {
			fsm.validateTransition("TransactionCreated", "DocumentsAdded");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		// Failing scenario
		try {
			fsm.validateTransition("UserDeclined", "TransactionSigned");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		// Non existent states
		try {
			fsm.validateTransition("MakingPopcorn", "ApplyingButter");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		// Advance the finite state machine
		try {
			fsm.transition("DocumentsAdded");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		// Fail to advance the finite state machine
		try {
			fsm.transition("TransactionCommited");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		// Advance to invalid state name
		try {
			fsm.transition("TheCheesening");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
}