package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemsWindmill implements WurmServerMod, ItemTypes, MiscConstants {

	public static Logger logger = Logger.getLogger(ItemsWindmill.class.getName());

	public static int addWagon(String model, String name) {
		try {
			return createWagon(model, name);
		} catch (Exception e) {
			logger.log(Level.INFO, (String.format("Initialization of wagon failed: %s", e.toString())));
		}
		return 0;
	}

	public ItemsWindmill() {

		try {

			ItemTemplateCreator.createItemTemplate(Windmill.sawmillTemplateId, "Sawmill", "Sawmills", "superb", "good", "ok", "poor",
					"A structure that creates wooden items. A strong wind will speed up the cutting process.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_NONE, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 1000, 1000, 1000, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.sawmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AdvancedCreationEntry Sawmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.marbleSlab, ItemList.woodBeam, Windmill.sawmillTemplateId, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Sawmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 35, true));
		Sawmill.addRequirement(new CreationRequirement(2, ItemList.shaft, 250, true));
		Sawmill.addRequirement(new CreationRequirement(3, ItemList.log, 18, true));
		Sawmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 60, true));
		Sawmill.addRequirement(new CreationRequirement(5, ItemList.plank, 800, true));
		Sawmill.addRequirement(new CreationRequirement(6, ItemList.ironBand, 20, true));
		Sawmill.addRequirement(new CreationRequirement(7, ItemList.stoneBrick, 200, true));
		Sawmill.addRequirement(new CreationRequirement(8, ItemList.shingleWood, 100, true));


		try {

			ItemTemplateCreator.createItemTemplate(Windmill.windmillTemplateId, "Windmill", "Windmills", "superb", "good", "ok", "poor",
					"A structure that creates flour from grains. A strong wind will speed up the grinding process.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_NONE, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 1000, 1000, 1000, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.windmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AdvancedCreationEntry Windmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.shaft, ItemList.woodBeam, net.WAC.wurmunlimited.mods.windmill.Windmill.windmillTemplateId, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Windmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 15, true));
		Windmill.addRequirement(new CreationRequirement(2, ItemList.shaft, 250, true));
		Windmill.addRequirement(new CreationRequirement(3, ItemList.log, 8, true));
		Windmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 60, true));
		Windmill.addRequirement(new CreationRequirement(5, ItemList.plank, 750, true));
		Windmill.addRequirement(new CreationRequirement(6, ItemList.ironBand, 25, true));
		Windmill.addRequirement(new CreationRequirement(7, ItemList.stoneBrick, 250, true));
		Windmill.addRequirement(new CreationRequirement(8, ItemList.colossusPart, 50, true));
		Windmill.addRequirement(new CreationRequirement(9, ItemList.shingleWood, 210, true));
	}

	private static int createWagon(String model, String name) throws IOException {
		logger.log(Level.INFO, (String.format("id :  requiem.wagon.%s", name)));
		ItemTemplateBuilder builder = new ItemTemplateBuilder(String.format("requiem.wagon.%s", name));
		builder.size(ItemSizes.ITEM_SIZE_MEDIUM);
		builder.name(String.format("%s wagon", name), String.format("%s wagons", name), "A fairly large wagon designed to be dragged by four animals.");
		builder.descriptions("almost full", "somewhat occupied", "half-full", "emptyish");
		builder.itemTypes(new short[]{
				ItemTypes.ITEM_TYPE_NAMED,
				ItemTypes.ITEM_TYPE_HOLLOW,
				ItemTypes.ITEM_TYPE_NOTAKE,
				ItemTypes.ITEM_TYPE_WOOD,
				ItemTypes.ITEM_TYPE_TURNABLE,
				ItemTypes.ITEM_TYPE_DECORATION,
				ItemTypes.ITEM_TYPE_REPAIRABLE,
				ItemTypes.ITEM_TYPE_VEHICLE,
				ItemTypes.ITEM_TYPE_CART,
				ItemTypes.ITEM_TYPE_VEHICLE_DRAGGED,
				ItemTypes.ITEM_TYPE_LOCKABLE,
				ItemTypes.ITEM_TYPE_HASDATA,
				ItemTypes.ITEM_TYPE_TRANSPORTABLE,
				ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
				ItemTypes.ITEM_TYPE_NOWORKPARENT,
				ItemTypes.ITEM_TYPE_NORENAME,
				ItemTypes.ITEM_TYPE_COLORABLE
		});
		builder.imageNumber((short) IconConstants.ICON_NONE);
		builder.behaviourType(BehaviourList.vehicleBehaviour);
		builder.combatDamage(0);
		builder.decayTime(TimeConstants.DECAYTIME_WOOD);
		builder.dimensions(550, 300, 410);
		builder.primarySkill((int) Skill.NOID);
		builder.modelName(model + ".");
		builder.difficulty(70f);
		builder.weightGrams(240000);
		builder.material(ItemMaterials.MATERIAL_WOOD_BIRCH);
		builder.value(5000);
		builder.isTraded(true);
		//builder.armourType(ArmourTemplate.ARMOUR_TYPE_NONE);
		builder.dyeAmountOverrideGrams((short) 0);
		builder.containerSize(400, 400, 400);
		ItemTemplate resultTemplate = builder.build();
		logger.log(Level.INFO, (String.format("%s; Template ID: %d; vehicle? %s", name, resultTemplate.getTemplateId(), resultTemplate.isVehicle())));

		return resultTemplate.getTemplateId();
	}

	public static void createCreationEntry(int templateId) {
		AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.wheelAxleSmall, templateId, false, false, 0f, true, true, 0, 40.0D, CreationCategories.CARTS);
		entry.addRequirement(new CreationRequirement(1, ItemList.wheelAxleSmall, 1, true));
		entry.addRequirement(new CreationRequirement(2, ItemList.plank, 20, true));
		entry.addRequirement(new CreationRequirement(3, ItemList.shaft, 4, true));
		entry.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 10, true));
		entry.addRequirement(new CreationRequirement(5, ItemList.yoke, 2, true));
	}


}