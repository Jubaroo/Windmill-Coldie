package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.sounds.SoundPlayer;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import net.WAC.wurmunlimited.mods.windmill.actions.PlankAction;
import net.WAC.wurmunlimited.mods.windmill.actions.ShaftAction;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.ItemTemplatesCreatedListener;
import org.gotti.wurmunlimited.modloader.interfaces.ServerStartedListener;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

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

    public static int windmillTemplateId;
    public static int sawmillTemplateId;
    public static int loggingWagonTemplateId;


    @Override
    public void configure(Properties properties) {
        windmillTemplateId = Integer.parseInt(properties.getProperty("Windmill_templateId", String.valueOf(5557)));
        sawmillTemplateId = Integer.parseInt(properties.getProperty("Sawmill_templateId", String.valueOf(5558)));
        loggingWagonTemplateId = Integer.parseInt(properties.getProperty("Logging_Wagon_templateId", String.valueOf(5559)));
        WagonFactory.registerWagonManageHook();
        //TODO add configs from properties file
        // make the sawmill take small damage every time it creates an item
        // make an enchant for the mill to work?
    }

    @Override
    public void init() {
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
                                logger.log(Level.INFO,(String.format("Hooking Item.moveToItem at %d", m.getLineNumber())));
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
                            for (int id : WagonFactory.wagonList) {
                                if (item.getTemplateId() == id) {
                                return item.getItemCount() < 200;
                            }
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

    @Override
    public void onServerStarted() {
        ModActions.registerAction(new ShaftAction());
        ModActions.registerAction(new PlankAction());
        WagonFactory.createCreationEntries();
    }


    @Override
    public void onItemTemplatesCreated() {
        new ItemsWindmill();
        WagonFactory.addAllWagons();
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

    public static final String[] WAGON_LIST = {
            "model.transports.medium.wagon.logging",
    };

    public static final String[] WAGON_NAMES = {
            "logging"
    };

    // Block items from being moved to certain items
    public static boolean blockMove(Item source, Item target, Creature performer) {
        for (int id : WagonFactory.wagonList) {
            if (target.getTemplateId() == id) {
                if (source.getTemplateId() != ItemList.logHuge) {
                    performer.getCommunicator().sendNormalServerMessage("Only felled trees can be put into a logging wagon.");
                    return true;
                }
            }
        }
        return false;
    }

}