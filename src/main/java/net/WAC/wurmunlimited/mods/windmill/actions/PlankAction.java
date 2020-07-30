package net.WAC.wurmunlimited.mods.windmill.actions;


import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;
import net.WAC.wurmunlimited.mods.windmill.Windmill;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Arrays;
import java.util.List;

public class PlankAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
	public static short actionId;
	static ActionEntry actionEntry;

	public PlankAction() {
		actionId = (short) ModActions.getNextActionId();
		actionEntry = ActionEntry.createEntry(actionId, "Create planks", "Creating planks", new int[]{
		});
		ModActions.registerAction(actionEntry);
	}

	@Override
	public BehaviourProvider getBehaviourProvider() {
		return this;
	}

	@Override
	public ActionPerformer getActionPerformer() {
		return this;
	}

	@Override
	public short getActionId() {
		return actionId;
	}

	@Override
	public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
		return getBehavioursFor(performer, target);
	}

	@Override
	public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
		// add check for if event active
		if (performer instanceof Player) {
			if (target.getTemplateId() == Windmill.sawmillTemplateId)
				return (List<ActionEntry>) Arrays.asList(actionEntry);
		}
		return null;
	}

	@Override
	public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
		return action(act, performer, target, action, counter);
	}

	@Override
	public boolean action(Action act, Creature performer, Item target, short action, float counter) {
		String actionstring = act.getActionEntry().getActionString().toLowerCase();
		//max time on action, in 10th of seconds, 600 = 60 seconds.
		int actiontime = 1800;
		//seconds between each code usage
		int tickTimes = 10;
		//wind power calculation
		int absolutewindpower = (int) (10 * Math.abs(Server.getWeather().getWindPower()));
		//subtract wind seconds, 0-5, gale being 5 seconds
		tickTimes = tickTimes - absolutewindpower;
		//max number of items to make each time.
		int itemstomake = 20;

		if (target.getTemplateId() == Windmill.sawmillTemplateId && target.getCurrentQualityLevel() >= 25) {
			if (counter == 1.0F) {
				performer.sendActionControl(actionstring, true, actiontime);
				performer.getCommunicator().sendNormalServerMessage(String.format("You will make a maximum of %d planks every %d seconds.", itemstomake, tickTimes));
			}
			if (act.currentSecond() % tickTimes == 0) {
				if (performer.getStatus().getStamina() < 5000) {
					performer.getCommunicator().sendNormalServerMessage("You must rest.");
					return true;
				}
				performer.getStatus().modifyStamina(-4000.0F);
				// damage the sawmill a bit when used
				float dam1 = Server.rand.nextFloat() * 0.2f;
				target.setDamage(target.getDamage() + dam1);
				return Windmill.itemCreate(performer, target, ItemList.plank, ItemList.log, (int) (3000 - (target.getCurrentQualityLevel() * 10)), 20, 1000, SoundNames.CARPENTRY_SAW_SND);
			}

		}
		return false;
	}
}