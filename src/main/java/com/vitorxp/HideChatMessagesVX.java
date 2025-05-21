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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@Mod(modid = "HideChatMessagesVX", name = "HideChatMessagesVX", version = "1.8")
public class HideChatMessagesVX {

    // Flags para ativar/desativar bloqueios
    private static boolean blockPetMessages = true;
    private static boolean blockInventoryMessages = true;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("HideChatMessagesVX carregado!");

        // Registrar comandos para ativar/desativar os bloqueios
        ClientCommandHandler.instance.registerCommand(new ToggleBlockCommand("petmaxblock",
                "Bloqueia mensagens do pet nível máximo", () -> blockPetMessages, val -> blockPetMessages = val,
                "PetChat", "bloqueio de mensagens do pet nível máximo"));

        ClientCommandHandler.instance.registerCommand(new ToggleBlockCommand("inventoryblock",
                "Bloqueia mensagens de inventário cheio", () -> blockInventoryMessages, val -> blockInventoryMessages = val,
                "InventoryChat", "bloqueio de mensagens do inventário"));

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String rawMessage = event.message.getUnformattedText();

        if (!rawMessage.contains(":")) {
            if (blockPetMessages && rawMessage.equals("Seu pet está nível máximo!")) {
                event.setCanceled(true);
            }

            if (blockInventoryMessages && rawMessage.equals("Seu inventário está cheio!")) {
                event.setCanceled(true);
            }
        }
    }

    // Classe genérica para comandos de toggle (ativar/desativar)
    public static class ToggleBlockCommand implements ICommand {
        private final String commandName;
        private final String description;
        private final BooleanSupplier getter;
        private final Consumer<Boolean> setter;
        private final String chatPrefix;
        private final String messageDescription;

        public ToggleBlockCommand(String commandName, String description, BooleanSupplier getter, Consumer<Boolean> setter, String chatPrefix, String messageDescription) {
            this.commandName = commandName;
            this.description = description;
            this.getter = getter;
            this.setter = setter;
            this.chatPrefix = chatPrefix;
            this.messageDescription = messageDescription;
        }

        @Override
        public String getCommandName() {
            return commandName;
        }

        @Override
        public String getCommandUsage(ICommandSender sender) {
            return "/" + commandName;
        }

        @Override
        public void processCommand(ICommandSender sender, String[] args) {
            boolean newValue = !getter.getAsBoolean();
            setter.accept(newValue);
            String status = newValue ? "§aativado" : "§cdesativado";
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                    "§b" + chatPrefix + " §8➜ §eBloqueio de mensagens do " + messageDescription + " " + status + "§e!"));
        }

        @Override
        public int compareTo(ICommand o) {
            return commandName.compareTo(o.getCommandName());
        }

        @Override
        public List<String> getCommandAliases() {
            return Collections.emptyList();
        }

        @Override
        public boolean canCommandSenderUseCommand(ICommandSender sender) {
            return true;
        }

        @Override
        public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }

        @Override
        public boolean isUsernameIndex(String[] args, int index) {
            return false;
        }
    }
}