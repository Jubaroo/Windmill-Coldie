package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import javassist.CtClass;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagonFactory {
    public static Logger logger = Logger.getLogger(WagonFactory.class.getName());

    public static ArrayList<Integer> wagonList = new ArrayList<>();

    public static void addAllWagons() {
        for (int i = 0; i < Windmill.WAGON_LIST.length; i++) {
            int id = ItemsWindmill.addWagon(Windmill.WAGON_LIST[i], Windmill.WAGON_NAMES[i]);
            if (id != 0) {
                wagonList.add(id);
            } else {
                logger.log(Level.INFO, String.format("%s wagon - cant' be created, id is 0", Windmill.WAGON_NAMES[i]));
            }
        }
    }

    public static void createCreationEntries() {
        for (int id : wagonList) {
            ItemsWindmill.createCreationEntry(id);
        }
    }

    public static void registerWagonManageHook() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item")
            };
            CtClass output = HookManager.getInstance().getClassPool().get("java.util.List");

            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.VehicleBehaviour", "getVehicleBehaviours",
                    Descriptor.ofMethod(output, input), new InvocationHandlerFactory() {
                        @Override
                        public InvocationHandler createInvocationHandler() {
                            return new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    List<ActionEntry> original = (List<ActionEntry>) method.invoke(proxy, args);
                                    Item item = (Item) args[1];
                                    Creature performer = (Creature) args[0];
                                    LinkedList<ActionEntry> permissions = new LinkedList<ActionEntry>();

                                    if (item.mayManage(performer)) {
                                        int itemId = item.getTemplateId();
                                        for (int id : WagonFactory.wagonList) {
                                            if (id == itemId) {
                                                permissions.add(Actions.actionEntrys[Actions.MANAGE_WAGON]);
                                            }
                                        }
                                    }
                                    if (item.maySeeHistory(performer)) {
                                        int itemId = item.getTemplateId();
                                        for (int id : WagonFactory.wagonList) {
                                            if (id == itemId) {
                                                permissions.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Wagon", "viewing"));
                                            }
                                        }
                                    }
                                    if (!permissions.isEmpty()) {
                                        if (permissions.size() > 1) {
                                            Collections.sort(permissions);
                                            original.add(new ActionEntry((short) (-permissions.size()), "Permissions", "viewing"));
                                        }
                                        original.addAll(permissions);
                                    }
                                    return original;
                                }

                            };
                        }
                    });
        } catch (Exception e) {
            logger.log(Level.WARNING,(String.format("Permission hook: %s", e.toString())));
        }
    }

}
