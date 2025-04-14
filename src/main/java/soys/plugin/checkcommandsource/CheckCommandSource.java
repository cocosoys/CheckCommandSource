package soys.plugin.checkcommandsource;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
        // 简单查询
        if(pluginCommand!=null){
            return pluginCommand.getPlugin();
        }
        // 遍历别名
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
        // 精准查找
//        if(result==null){
//            result=getCommandReflection(command);
//        }
        return result;
    }

//    public Plugin getCommandReflection(String command){
//        Plugin result=null;
//        SimplePluginManager simplePluginManager=((SimplePluginManager)Bukkit.getPluginManager());
//        try {
//            SimpleCommandMap commandMap;
//            Field field_commandMap=SimplePluginManager.class.getDeclaredField("commandMap");
//            field_commandMap.setAccessible(true);
//            commandMap = (SimpleCommandMap) field_commandMap.get(simplePluginManager);
//            Map<String, Command> knownCommands;
//            Field field_knownCommands=SimpleCommandMap.class.getDeclaredField("knownCommands");
//            field_knownCommands.setAccessible(true);
//            knownCommands = (Map<String, Command>) field_knownCommands.get(commandMap);
//            for (Map.Entry<String, Command> stringCommandEntry : knownCommands.entrySet()) {
//                if(stringCommandEntry.getValue().getName().equals(command)){
//                    result=getPluginWithName(stringCommandEntry.getKey());
//                    break;
//                }else{
//                    for (String alias : stringCommandEntry.getValue().getAliases()) {
//                        if(alias.equals(command)){
//                            result=getPluginWithName(stringCommandEntry.getKey());
//                            break;
//                        }
//                    }
//                }
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException("反射获取cmd产生异常",e);
//        }
//        return result;
//    }
//
//    public Plugin getPluginWithName(String name) {
//        return Bukkit.getPluginManager().getPlugin(name);
//    }
//
//    public static void print(String mess){
//        System.out.println("[checkcommandsource]"+mess);
//    }

}
