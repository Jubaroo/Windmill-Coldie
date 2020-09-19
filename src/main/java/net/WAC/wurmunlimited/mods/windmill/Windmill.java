package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import com.wurmonline.server.sounds.SoundPlayer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import net.WAC.wurmunlimited.mods.windmill.actions.FlourAction;
import net.WAC.wurmunlimited.mods.windmill.actions.PlankAction;
import net.WAC.wurmunlimited.mods.windmill.actions.ShaftAction;
import net.WAC.wurmunlimited.mods.windmill.util.WagonHook;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Windmill implements WurmServerMod, Configurable, ServerStartedListener, ItemTemplatesCreatedListener {

    public static Logger logger = Logger.getLogger(Windmill.class.getName());

    public String getVersion() {
        return "v4.0";
    }

    public static boolean wagonsEnabled = false;
    public static int windmillHoldSize = 200;
    public static int sawmillHoldSize = 200;
    public static int loggingWagonHoldSize = 200;
    public static int damageToTarget = 2;

    @Override
    public void configure(Properties properties) {
        damageToTarget = Integer.parseInt(properties.getProperty("Max_Damage_Per_Creation", String.valueOf(damageToTarget)));
        windmillHoldSize = Integer.parseInt(properties.getProperty("Windmill_Hold_Size", String.valueOf(windmillHoldSize))) - 100;
        sawmillHoldSize = Integer.parseInt(properties.getProperty("Sawmill_Hold_Size", String.valueOf(sawmillHoldSize))) - 100;
        loggingWagonHoldSize = Integer.parseInt(properties.getProperty("Logging_Wagon_Hold_Size", String.valueOf(loggingWagonHoldSize))) - 100;
        wagonsEnabled = Boolean.parseBoolean(properties.getProperty("Logging_Wagon_Enabled", String.valueOf(wagonsEnabled)));
        if (wagonsEnabled) {
            WagonHook.registerWagonManageHook();
            WagonHook.registerWagonHook();
            logger.log(Level.INFO, "Logging Wagon's are enabled");
            logger.log(Level.INFO, (String.format("Logging Wagon Hold Size = %d", loggingWagonHoldSize)));
        }
        logger.log(Level.INFO, (String.format("Windmill Hold Size = %d", windmillHoldSize)));
        logger.log(Level.INFO, (String.format("Sawmill Hold Size = %d", sawmillHoldSize)));
        //TODO
        // make an enchant for the mill to work
        // make items have auto generated id's

    }

    @Override
    public void init() {
        if (wagonsEnabled) {

            final ClassPool classPool = HookManager.getInstance().getClassPool();

            // block all items except felled trees from being put into the logging wagon
            try {

                classPool.getCtClass("com.wurmonline.server.items.Item")
                        .getMethod("moveToItem", "(Lcom/wurmonline/server/creatures/Creature;JZ)Z")
                        .instrument(new ExprEditor() {
                            boolean patched = false;

                            @Override
                            public void edit(MethodCall m) throws CannotCompileException {
                                if (!patched && m.getMethodName().equals("getItem")) {
                                    m.replace("$_=$proceed($$); if (net.WAC.wurmunlimited.mods.windmill.Windmill.blockMove(this, $_, mover)) return false;");
                                    logger.log(Level.INFO, (String.format("Hooking Item.moveToItem at %d", m.getLineNumber())));
                                    patched = true;
                                }
                            }
                        });

                // control the max number of items that can be put into the wagon
                HookManager.getInstance().registerHook("com.wurmonline.server.items.Item", "mayCreatureInsertItem", "()Z", new InvocationHandlerFactory() {
                    @Override
                    public InvocationHandler createInvocationHandler() {
                        return new InvocationHandler() {
                            @Override
                            public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                                Item item = (Item) object;
                                if (item.getTemplateId() == ItemsWindmill.LOGGING_WAGON_ID) {
                                    return item.getItemCount() < loggingWagonHoldSize;
                                }
                                return method.invoke(object, args);
                            }
                        };
                    }
                });

            } catch (CannotCompileException | NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServerStarted() {
        ModActions.registerAction(new ShaftAction());
        ModActions.registerAction(new PlankAction());
        ModActions.registerAction(new FlourAction());
    }

    @Override
    public void onItemTemplatesCreated() {
        try {
            ItemsWindmill.registerDecorativeItems();
            ItemsWindmill.initCreationEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean itemCreate(Creature performer, Item item, int templateProduce, int templateConsume, int weightconsume, int maxNums, int maxItems, String SoundName) throws NoSuchTemplateException {

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
            performer.getCommunicator().sendNormalServerMessage(String.format("There are too many items in the %s already. Remove some and try again. The maximum number of items that can fit is %d.", item.getName(), maxItems));
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
            if (countcreated > 1) createdname = String.format("%ss", createdname);
            // added stamina check
            if (performer.getStatus().getStamina() < 5000) {
                performer.getCommunicator().sendNormalServerMessage("You must rest.");
                return true;
            }
            if (playsound) {
                performer.getCommunicator().sendSafeServerMessage(String.format("Created %d %s", countcreated, createdname));
                SoundPlayer.playSound(SoundName, item, 0);
                // added to damage target when stuff is created
                float dmg = damageToTarget * Server.rand.nextFloat();
                item.setDamage(item.getDamage() + dmg);
                if (performer.getPower() >= 3) {
                    performer.getCommunicator().sendSafeServerMessage(String.format("Added %s damage to %s", dmg, item.getName()));
                }
                // added stamina drain
                performer.getStatus().modifyStamina(-4000.0F);
                return false;
            }
        } else {
            String name = ItemTemplateFactory.getInstance().getTemplate(templateConsume).getName();
            performer.getCommunicator().sendSafeServerMessage(String.format("No %s found, stopping action.", name));
        }
        return true;
    }

    // Block items from being moved to certain items
    public static boolean blockMove(Item source, Item target, Creature performer) {
        if (target.getTemplateId() == ItemsWindmill.LOGGING_WAGON_ID) {
            if (source.getTemplateId() != ItemList.logHuge) {
                performer.getCommunicator().sendNormalServerMessage("Only felled trees can be put into a logging wagon.");
                return true;
            }
        }
        return false;
    }

}