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

@Mod(modid = "ChatPetLevelBlockVX", name = "ChatPetLevelBlockVX", version = "1.5")
public class ChatPetLevelBlockVX {

    public static boolean blockEnabled = true;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("ChatPetLevelBlockVX carregado!");
        ClientCommandHandler.instance.registerCommand(new TogglePetLevelBlockCommand());

        // Registra os eventos
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!blockEnabled) return;

        String msg = event.message.getUnformattedText();
        if (msg.contains("Seu pet está nível máximo!")) {
            event.setCanceled(true);
        }
    }

    public static class TogglePetLevelBlockCommand implements ICommand {

        @Override
        public String getCommandName() {
            return "ocultarpetmax";
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/ocultarpetmax";
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            blockEnabled = !blockEnabled;
            String status = blockEnabled ? "§aativado" : "§cdesativado";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§bPetChat §8➜ §eBloqueio de mensagens do pet nível máximo " + status + "§e!"));
        }

        @Override public int compareTo(ICommand o) { return 0; }
        @Override public List getCommandAliases() { return Collections.emptyList(); }
        @Override public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
        @Override public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) { return null; }
        @Override public boolean isUsernameIndex(String[] args, int index) { return false; }
    }
}
