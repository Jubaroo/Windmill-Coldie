package net.WAC.wurmunlimited.mods.windmill;

import java.io.IOException;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.items.*;
import com.wurmonline.shared.constants.IconConstants;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.skills.SkillList;

public class ItemsWindmill implements WurmServerMod, ItemTypes, MiscConstants {

	public ItemsWindmill() {

		try {

			com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(Windmill.sawmillTemplateId, "Sawmill", "Sawmills", "superb", "good", "ok", "poor",
					"A structure that creates wooden items. A strong wind will speed up the cutting process.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_NONE, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 1000, 1000, 1000, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.sawmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {

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

			com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(Windmill.windmillTemplateId, "Windmill", "Windmills", "superb", "good", "ok", "poor",
					"A structure that creates flour from grains. A strong wind will speed up the grinding process.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_NONE, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 1000, 1000, 1000, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.windmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {

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

}