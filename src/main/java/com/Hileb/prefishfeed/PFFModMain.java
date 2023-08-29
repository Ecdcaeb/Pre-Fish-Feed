package com.Hileb.prefishfeed;


import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PFFModMain.MODID, name = PFFModMain.NAME, version = PFFModMain.VERSION)//dependencies = "required-after:Forge@[14.23.5.2705,)"
public class PFFModMain {
    public static final String MODID = "prefishfeed";
    public static final String NAME = "Pre Fish Feed";
    public static final String VERSION = "1.0.1";

    public static Logger logger= LogManager.getLogger(MODID);

    @Mod.Instance
    public static PFFModMain instance;
}