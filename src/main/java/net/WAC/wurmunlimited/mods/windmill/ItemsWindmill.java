package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;

import java.io.IOException;

public class ItemsWindmill implements WurmServerMod, ItemTypes, MiscConstants {

	public static int SAWMILL_ID, WINDMILL_ID, LOGGING_WAGON_ID;

	private static final short[] sharedItemTypes = new short[]{
			ITEM_TYPE_WOOD,
			ITEM_TYPE_REPAIRABLE,
			ITEM_TYPE_NOT_MISSION,
			ITEM_TYPE_HOLLOW,
			ITEM_TYPE_NOTAKE,
			ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
			ITEM_TYPE_DESTROYABLE,
			ITEM_TYPE_VISIBLEDECAY,
			ITEM_TYPE_COLORABLE,
			ITEM_TYPE_DECAYDESTROYS
	};

	public static void registerDecorativeItems() throws IOException {
		registerSawmill();
		registerWindmill();
		registerLoggingWagon();
	}

	public static void registerSawmill() throws IOException {
		ItemTemplate temp = new ItemTemplateBuilder("WAC.structure.sawmill")
				.name("Sawmill", "Sawmills", "A structure that creates wooden items. A strong wind will speed up the cutting process.")
				.modelName("model.structure.sawmill.")
				.imageNumber((short) IconConstants.ICON_NONE)
				.weightGrams(1000000)
				.value(10000)
				.difficulty(30.0F)
				.dimensions(1000, 1000, 1000)
				.decayTime(TimeConstants.DECAYTIME_WOOD)
				.material(ItemMaterials.MATERIAL_WOOD_BIRCH)
				.behaviourType(BehaviourList.itemBehaviour)
				.itemTypes(sharedItemTypes)
				.containerSize(500, 500, 500)
				.build();

		SAWMILL_ID = temp.getTemplateId();
	}

	public static void registerWindmill() throws IOException {
		ItemTemplate temp = new ItemTemplateBuilder("WAC.structure.windmill")
				.name("Windmill", "Windmills", "A structure that creates flour from grains. A strong wind will speed up the grinding process.")
				.modelName("model.structure.windmill.")
				.imageNumber((short) IconConstants.ICON_NONE)
				.weightGrams(840000)
				.value(10000)
				.difficulty(30.0F)
				.dimensions(1000, 1000, 1000)
				.decayTime(TimeConstants.DECAYTIME_WOOD)
				.material(ItemMaterials.MATERIAL_WOOD_BIRCH)
				.behaviourType(BehaviourList.itemBehaviour)
				.itemTypes(sharedItemTypes)
				.containerSize(500, 500, 500)
				.build();

		WINDMILL_ID = temp.getTemplateId();
	}

	public static void registerLoggingWagon() throws IOException {
		ItemTemplate temp = new ItemTemplateBuilder("WAC.wagon.logging")
				.name("Logging Wagon", "Logging Wagon", "A fairly large wagon designed to be dragged by four animals. This one was made specifically for hauling lots of felled trees.")
				.modelName("model.transports.medium.wagon.logging.")
				.imageNumber((short) IconConstants.ICON_NONE)
				.weightGrams(240000)
				.value(5000)
				.difficulty(70.0F)
				.dimensions(550, 300, 410)
				.decayTime(TimeConstants.DECAYTIME_WOOD)
				.material(ItemMaterials.MATERIAL_WOOD_BIRCH)
				.behaviourType(BehaviourList.vehicleBehaviour)
				.itemTypes(new short[]{
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
				})
				.containerSize(800, 800, 800)
				.isTraded(false)
				.build();

		LOGGING_WAGON_ID = temp.getTemplateId();
	}

	public static void initCreationEntry() {
		final AdvancedCreationEntry Sawmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.marbleSlab, ItemList.woodBeam, SAWMILL_ID, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Sawmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 35, true));
		Sawmill.addRequirement(new CreationRequirement(2, ItemList.shaft, 250, true));
		Sawmill.addRequirement(new CreationRequirement(3, ItemList.log, 18, true));
		Sawmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 60, true));
		Sawmill.addRequirement(new CreationRequirement(5, ItemList.plank, 800, true));
		Sawmill.addRequirement(new CreationRequirement(6, ItemList.ironBand, 20, true));
		Sawmill.addRequirement(new CreationRequirement(7, ItemList.stoneBrick, 200, true));
		Sawmill.addRequirement(new CreationRequirement(8, ItemList.shingleWood, 100, true));
		Sawmill.addRequirement(new CreationRequirement(9, ItemList.clothYard, 40, true));
		final AdvancedCreationEntry Windmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.shaft, ItemList.woodBeam, WINDMILL_ID, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Windmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 15, true));
		Windmill.addRequirement(new CreationRequirement(2, ItemList.shaft, 250, true));
		Windmill.addRequirement(new CreationRequirement(3, ItemList.log, 8, true));
		Windmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 60, true));
		Windmill.addRequirement(new CreationRequirement(5, ItemList.plank, 750, true));
		Windmill.addRequirement(new CreationRequirement(6, ItemList.ironBand, 25, true));
		Windmill.addRequirement(new CreationRequirement(7, ItemList.stoneBrick, 250, true));
		Windmill.addRequirement(new CreationRequirement(8, ItemList.colossusPart, 50, true));
		Windmill.addRequirement(new CreationRequirement(9, ItemList.shingleWood, 210, true));
		Windmill.addRequirement(new CreationRequirement(10, ItemList.clothYard, 40, true));
		if (net.WAC.wurmunlimited.mods.windmill.Windmill.wagonsEnabled) {
			final AdvancedCreationEntry Wagon = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.wheelAxleSmall, LOGGING_WAGON_ID, false, false, 0f, true, true, 0, 50.0D, CreationCategories.CARTS);
			Wagon.addRequirement(new CreationRequirement(1, ItemList.wheelAxleSmall, 1, true));
			Wagon.addRequirement(new CreationRequirement(2, ItemList.plank, 30, true));
			Wagon.addRequirement(new CreationRequirement(3, ItemList.shaft, 8, true));
			Wagon.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 15, true));
			Wagon.addRequirement(new CreationRequirement(5, ItemList.yoke, 2, true));
		}
	}

}