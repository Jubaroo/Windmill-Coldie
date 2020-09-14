package net.WAC.wurmunlimited.mods.windmill;

import com.wurmonline.server.Features;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.behaviours.Seat;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.ProtoConstants;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import net.WAC.wurmunlimited.mods.windmill.util.SeatsFacadeImpl;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacadeImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagonHook {
    public static Logger logger = Logger.getLogger(WagonHook.class.getName());

    public static void registerWagonHook() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        if (item.getTemplateId() == Windmill.loggingWagonTemplateId) {
                            Vehicle vehicle = (Vehicle) args[1];
                            VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                            if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                vehfacade.createPassengerSeats(1);
                            } else {
                                vehfacade.createPassengerSeats(0);
                            }
                            vehfacade.setCreature(false);
                            vehfacade.setEmbarkString("ride");
                            vehicle.name = item.getName();
                            vehicle.setSeatFightMod(0, 0.9f, 0.3f);
                            vehicle.setSeatOffset(0, 0f, 0f, 0f, 1.453f);
                            if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                vehicle.setSeatFightMod(1, 1f, 0.4f);
                                vehicle.setSeatOffset(1, 4.05f, 0f, 0.84f);
                            }
                            vehicle.maxHeightDiff = 0.07f;
                            vehicle.maxDepth = -0.7f;
                            vehicle.skillNeeded = 21f;
                            vehfacade.setMaxSpeed(1f);
                            vehicle.commandType = ProtoConstants.TELE_START_COMMAND_CART;
                            SeatsFacadeImpl seatfacad = new SeatsFacadeImpl();

                            final Seat[] hitches = {
                                    seatfacad.CreateSeat((byte) 2),
                                    seatfacad.CreateSeat((byte) 2),
                                    seatfacad.CreateSeat((byte) 2),
                                    seatfacad.CreateSeat((byte) 2)
                            };
                            hitches[0].offx = -2f;
                            hitches[0].offy = -1f;
                            hitches[1].offx = -2f;
                            hitches[1].offy = 1f;
                            hitches[2].offx = -5f;
                            hitches[2].offy = -1f;
                            hitches[3].offx = -5f;
                            hitches[3].offy = 1f;
                            vehicle.addHitchSeats(hitches);
                            vehicle.setMaxAllowedLoadDistance(4);
                            return null;
                        }

                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            logger.log(Level.FINE, (String.format("Vehicle hook: %s", e.toString())));
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
                                        if (item.getTemplateId() == Windmill.loggingWagonTemplateId) {
                                            permissions.add(Actions.actionEntrys[Actions.MANAGE_WAGON]);
                                        }
                                    }
                                    if (item.maySeeHistory(performer)) {
                                        if (item.getTemplateId() == Windmill.loggingWagonTemplateId) {
                                            permissions.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Wagon", "viewing"));
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
            logger.log(Level.WARNING, (String.format("Permission hook: %s", e.toString())));
        }
    }

}
