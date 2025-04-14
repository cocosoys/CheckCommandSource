package soys.plugin.checkcommandsource;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;

public final class CheckCommandSource extends JavaPlugin {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("checkcommandsource") || label.equalsIgnoreCase("checkms")) {
            if (!sender.isOp()) {
                return true;
            }
            if (args.length == 1) {
                String commandName = args[0];
                Plugin plugin = findAliases(commandName);
                if (plugin == null) {
                    sender.sendMessage("§4无法找到指令来源: " + commandName);
                    return true;
                }
                sender.sendMessage("§2指令来自: " + plugin.getName());
                sender.sendMessage("§2插件文件路径: " + plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getFile());
            }
        }
        return true;
    }

    public Plugin findAliases(String command){
        Plugin result=null;
        PluginCommand pluginCommand=Bukkit.getPluginCommand(command);
        if(pluginCommand!=null){
            return pluginCommand.getPlugin();
        }
        for (Map.Entry<String, String[]> stringEntry : Bukkit.getCommandAliases().entrySet()) {
            if(result!=null){
                break;
            }
            String[] aliases=stringEntry.getValue();
            for(String alias : aliases){
                if(alias.equals(command)){
                    result=Bukkit.getPluginManager().getPlugin(alias);
                    break;
                }
            }
        }
        return result;
    }

}
