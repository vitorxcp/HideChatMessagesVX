package com.vitorxp;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;

@Mod(modid = "HideChatMessagesVX", name = "HideChatMessagesVX", version = "1.7")
public class HideChatMessagesVX {

    public static boolean blockEnabledPet = true;
    public static boolean blockEnabledInventory = true;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("HideChatMessagesVX carregado!");
        ClientCommandHandler.instance.registerCommand(new TogglePetLevelBlockCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleInventoryFullBlockCommand());

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();

        if(blockEnabledPet) {
            if (msg.contains("Seu pet está nível máximo!")) {
                event.setCanceled(true);
            }
        }
        if(blockEnabledInventory) {
            if (msg.contains("Seu inventário está cheio!")) {
                event.setCanceled(true);
            }
        }
    }

    public static class TogglePetLevelBlockCommand implements ICommand {

        @Override
        public String getCommandName() {
            return "petmaxblock";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/petmaxblock";
        }


        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            blockEnabledPet = !blockEnabledPet;
            String status = blockEnabledPet ? "§aativado" : "§cdesativado";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§bPetChat §8➜ §eBloqueio de mensagens do pet nível máximo " + status + "§e!"));
        }

        @Override public int compareTo(ICommand o) { return 0; }
        @Override public List getCommandAliases() { return Collections.emptyList(); }
        @Override public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
        @Override public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) { return null; }
        @Override public boolean isUsernameIndex(String[] args, int index) { return false; }
    }

    public static class ToggleInventoryFullBlockCommand implements ICommand {

        @Override
        public String getCommandName() {
            return "inventoryblock";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/inventoryblock";
        }


        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            blockEnabledInventory = !blockEnabledInventory;
            String status = blockEnabledInventory ? "§aativado" : "§cdesativado";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§bInventoryChat §8➜ §eBloqueio de mensagens do inventário " + status + "§e!"));
        }

        @Override public int compareTo(ICommand o) { return 0; }
        @Override public List getCommandAliases() { return Collections.emptyList(); }
        @Override public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
        @Override public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) { return null; }
        @Override public boolean isUsernameIndex(String[] args, int index) { return false; }
    }
}
