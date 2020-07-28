package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import com.wurmonline.server.sounds.SoundPlayer;

import net.WAC.wurmunlimited.mods.windmill.actions.PlankAction;
import net.WAC.wurmunlimited.mods.windmill.actions.ShaftAction;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Properties;

public class Windmill implements WurmServerMod, Configurable, ServerStartedListener, ItemTemplatesCreatedListener {


    public String getVersion() {
        return "v3.0";
    }

    public static int windmillTemplateId;
    public static int sawmillTemplateId;


    @Override
    public void configure(Properties properties) {
        windmillTemplateId = Integer.parseInt(properties.getProperty("Windmill_templateId", String.valueOf(5557)));
        sawmillTemplateId = Integer.parseInt(properties.getProperty("Sawmill_templateId", String.valueOf(5558)));
        //TODO add configs from properties file
        // number of items produced?
        // amount of time the machines produce?
    }


    @Override
    public void onServerStarted() {
        ModActions.registerAction(new ShaftAction());
        ModActions.registerAction(new PlankAction());
    }

    @Override
    public void onItemTemplatesCreated() {
        new ItemsWindmill();
    }


    public static boolean itemCreate(Creature performer, Item item, int templateProduce, int templateConsume, int weightconsume, int maxNums, int maxItems, String SoundName) {

        Item[] currentItems = item.getAllItems(true);
        int produceTally = 0;
        int consumeTally = 0;

        for (Item i : currentItems) {
            if (templateProduce == i.getTemplateId()) {
                produceTally++;
            } else if (templateConsume == i.getTemplateId()) {
                consumeTally++;
            }
        }

        maxNums = Math.min(maxNums, consumeTally);
        //stop if reached maxItems to make in inventory
        if (produceTally >= maxItems) {
            return true;
        }
        int countcreated = 0;
        String createdname = "";
        if (templateConsume != 0) {
            consumeTally = Math.min(maxNums, consumeTally);
            boolean playsound = false;
            //this has the item info we need, so use it here.
            for (Item i : currentItems) {
                if (consumeTally > 0 && i.getTemplateId() == templateConsume && i.getWeightGrams() >= weightconsume) {
                    Item toInsert;
                    //only destroy items if we successfully create items
                    try {
                        byte material = 0;
                        material = i.getMaterial();
                        if (material != 0) {
                            toInsert = ItemFactory.createItem(templateProduce, i.getQualityLevel(), material, i.getRarity(), null);
                        } else {
                            toInsert = ItemFactory.createItem(templateProduce, i.getQualityLevel(), i.getRarity(), null);
                        }
                        if (i.getRarity() != 0) {
                            i.setRarity((byte) 0);
                        }
                        playsound = true;
                        item.insertItem(toInsert, true);
                        countcreated++;
                        createdname = toInsert.getName();
                        i.setWeight(i.getWeightGrams() - weightconsume, false);
                        //only destroy item if weight is 100 grams or less
                        if (100 >= i.getWeightGrams()) {
                            Items.destroyItem(i.getWurmId());
                        }
                        consumeTally--;
                    } catch (FailedException | NoSuchTemplateException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (countcreated > 1) createdname = createdname + "s";
            if (playsound) {
                performer.getCommunicator().sendSafeServerMessage(String.format("Created %d %s", countcreated, createdname));
                SoundPlayer.playSound(SoundName, item, 0);
                return false;
            }
        }
        return true;
    }

}