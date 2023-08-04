package com.Hileb.prefishfeed.fish;

import com.Hileb.prefishfeed.PFFModMain;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @Project FishFeed
 * @Author Hileb
 * @Date 2023/8/3 19:01
 **/
@Mod.EventBusSubscriber
public class PFFFishHandler {
    public static List<HookState> livingHooks=new LinkedList<>();
    @SubscribeEvent
    public static void onHookSpawn(EntityJoinWorldEvent event){
        World world=event.getWorld();
        if (!world.isRemote){
            if (event.getEntity() instanceof EntityFishHook){
                EntityFishHook entity=(EntityFishHook) event.getEntity();
                HookState hook=new HookState();
                hook.entity=entity;
                hook.luck=entity.luck;
                hook.lureSpeed=entity.lureSpeed;
                livingHooks.add(hook);
            }
        }
    }
    @SubscribeEvent
    public static void onWorldUpdate(TickEvent.WorldTickEvent event){
        //post world tick
        World world=event.world;
        if (!world.isRemote){
            List<HookState> remove=new LinkedList<>();
            for(HookState hook:livingHooks){
                if (hook==null || hook.entity==null || hook.entity.isDead) {
                    remove.add(hook);
                }else {
                    if (hook.entity.isInWater()){
                        List<EntityItem> nearbyItems=world.getEntitiesWithinAABB(EntityItem.class,
                                new AxisAlignedBB(
                                        hook.entity.getPosition().add(-3,1,-3),
                                        hook.entity.getPosition().add(3,-1,3)
                                ),
                                (entity)->{
                                    if (entity.isInWater() && !entity.isDead && !entity.getItem().isEmpty()){
                                        ItemStack stack=entity.getItem();
                                        Item item=stack.getItem();
                                        if (item instanceof ItemFood){
                                            return true;
                                        }else if (item instanceof ItemSeeds){
                                            return true;
                                        }else return false;
                                    }else return false;
                                }
                        );
                        Random random=new Random();
                        int count=0;
                        for(EntityItem entityItem:nearbyItems){
                            count+=entityItem.getItem().getCount();
                            if (MathHelper.getInt(random,0,1000)<=1){
                                entityItem.getItem().shrink(1);
                            }
                        }

                        boolean overFlag=false;
                        int speedValue=0;
                        int luckValue=0;
                        if (count==1){
                            speedValue=1;
                        }else if (count==2){
                            speedValue=1;
                            luckValue=1;
                        }else if (count==3){
                            speedValue=2;
                            luckValue=1;
                        }else if (count==4){
                            speedValue=3;
                            luckValue=2;
                        }else if (count==5){
                            speedValue=4;
                            luckValue=2;
                        }else if (count>=6 & count <10){
                            speedValue=5;
                            luckValue=2;
                        }else {
                            overFlag=true;
                            speedValue=5;
                            luckValue=3;
                        }

                        hook.entity.luck=hook.luck+luckValue;
                        hook.entity.lureSpeed=hook.lureSpeed+speedValue;
                        if (hook.entity.lureSpeed>5)hook.entity.lureSpeed=5;
                        if (overFlag)hook.entity.lureSpeed=114514;
                    }
                }
            }
            for(HookState hook:remove){
                livingHooks.remove(hook);
                PFFModMain.logger.warn("aaa");
            }
        }
    }
    public static class HookState{
        public EntityFishHook entity=null;
        public int luck=0;
        public int lureSpeed=0;
    }
}
