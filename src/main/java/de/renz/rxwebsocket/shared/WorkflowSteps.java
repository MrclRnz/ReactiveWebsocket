package de.renz.rxwebsocket.shared;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WorkflowSteps {

	public static final int STEP_ESTABLISH_WEBSOCKET = -1;
	public static final int STEP_CHECK_CLIENT_PROCESS = 0;
	public static final int STEP_CREATE_LOBBY = 1;
	public static final int STEP_INVITE_PLAYERS = 2;
	public static final int STEP_ACCEPT_INVITATION = 3;
	public static final int STEP_START_CHAMP_SELECT = 4;
	public static final int STEP_SELECT_CHAMPION = 5;
	public static final int STEP_CREATE_RUNEPAGE = 6;
	public static final int STEP_SELECT_RUNEPAGE = 7;
	public static final int STEP_SELECT_SUMMONERS = 8;
	public static final int STEP_READY = 9;

	private static final int[] OWNER_STEPS_ONLY = new int[]{STEP_CREATE_LOBBY, STEP_INVITE_PLAYERS, STEP_START_CHAMP_SELECT};
	private static final int[] PARTICIPANTS_STEPS_ONLY = new int[]{STEP_ACCEPT_INVITATION};

	private static Map<String, Integer> playerWorkflowSteps = new HashMap<>();
	private static String owner;
	private static Gson gson = new Gson();


	public static void addPlayer(String player){
		playerWorkflowSteps.put(player, STEP_ESTABLISH_WEBSOCKET);
	}

	public static void setOwner(String player){
		owner = player;
	}

	private static boolean stepIsRestricted(String player, int newStep){
		if(player.equals(owner)){
			if(Arrays.stream(PARTICIPANTS_STEPS_ONLY).anyMatch(step -> step == newStep)){
				return true;
			}
		} else {
			if(Arrays.stream(OWNER_STEPS_ONLY).anyMatch(step -> step == newStep)){
				return true;
			}
		}

		return false;
	}

	public static void setStepCompleted(String player){
		int newStep = playerWorkflowSteps.get(player);
		while (stepIsRestricted(player, newStep)) {
			newStep++;
		}

		playerWorkflowSteps.put(player, newStep);
	}

	public static int getStep(String player){
		return playerWorkflowSteps.get(player);
	}
}
