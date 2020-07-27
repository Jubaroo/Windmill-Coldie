package net.WAC.wurmunlimited.mods.windmill;

import java.io.IOException;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.items.*;
import com.wurmonline.shared.constants.IconConstants;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.skills.SkillList;

public class windmillitems implements WurmServerMod, ItemTypes, MiscConstants {

	public windmillitems() {

		try {

			com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(net.WAC.wurmunlimited.mods.windmill.windmill.sawmilltemplateid, "Sawmill", "Sawmills", "superb", "good", "ok", "poor",
					"A structure that creates wooden items.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_TOOL_WHEEL, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 100, 100, 100, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.sawmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {

		}
		AdvancedCreationEntry Sawmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.marbleSlab, ItemList.woodBeam, net.WAC.wurmunlimited.mods.windmill.windmill.sawmilltemplateid, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Sawmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 10, true));
		Sawmill.addRequirement(new CreationRequirement(2, ItemList.sheetIron, 4, true));
		Sawmill.addRequirement(new CreationRequirement(3, ItemList.log, 4, true));
		Sawmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 40, true));
		Sawmill.addRequirement(new CreationRequirement(5, ItemList.plank, 60, true));
		Sawmill.addRequirement(new CreationRequirement(6, ItemList.saw, 1, true));
		Sawmill.addRequirement(new CreationRequirement(7, ItemList.ironBand, 8, true));
		Sawmill.addRequirement(new CreationRequirement(8, ItemList.metalRivet, 20, true));


		try {

			com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(net.WAC.wurmunlimited.mods.windmill.windmill.windmilltemplateid, "Windmill", "Windmills", "superb", "good", "ok", "poor",
					"A structure that creates wooden items.",
					new short[]{ITEM_TYPE_WOOD, ITEM_TYPE_REPAIRABLE, ITEM_TYPE_NOT_MISSION,
							ITEM_TYPE_HOLLOW, ITEM_TYPE_NOTAKE, ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
							ITEM_TYPE_DESTROYABLE},
					(short) IconConstants.ICON_TOOL_WHEEL, BehaviourList.itemBehaviour, 0, TimeConstants.DECAYTIME_WOOD, 100, 100, 100, (int) MiscConstants.NOID,
					MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.windmill.", 30.0F, 50, Materials.MATERIAL_WOOD_BIRCH, 10000, true).setContainerSize(500, 500, 500);
		} catch (IOException e) {

		}
		AdvancedCreationEntry Windmill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.shaft, ItemList.woodBeam, net.WAC.wurmunlimited.mods.windmill.windmill.windmilltemplateid, true, true, 0.0f, true, true, CreationCategories.PRODUCTION);
		Windmill.addRequirement(new CreationRequirement(1, ItemList.woodBeam, 3, true));
		Windmill.addRequirement(new CreationRequirement(2, ItemList.shaft, 10, true));
		Windmill.addRequirement(new CreationRequirement(3, ItemList.log, 4, true));
		Windmill.addRequirement(new CreationRequirement(4, ItemList.nailsIronLarge, 20, true));
		Windmill.addRequirement(new CreationRequirement(5, ItemList.plank, 10, true));
		Windmill.addRequirement(new CreationRequirement(6, ItemList.ironBand, 4, true));
		Windmill.addRequirement(new CreationRequirement(7, ItemList.stoneBrick, 10, true));
	}

}