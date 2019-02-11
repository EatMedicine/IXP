package cn.eatmedicine.minecraft.command.factory;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.eatmedicine.minecraft.Main;
import cn.eatmedicine.minecraft.Database.Database;
import cn.eatmedicine.minecraft.IXPData.IXPData;
import cn.eatmedicine.minecraft.config.serverIds;
import cn.eatmedicine.minecraft.utils.BlockAnalysis;

public class CreateReceiveSign implements IHandleCommand {
    private Main plugin;
    CommandSender sender;

    public CreateReceiveSign(Main plugin, CommandSender sender) {
        this.plugin = plugin;
        this.sender = sender;
    }

    @Override
    public boolean handleCommand() {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }
        Block block = player.getTargetBlock(null, 20);
        Sign sign = BlockAnalysis.GetSign(block);
        if (sign == null) {
            sender.sendMessage("ָ��ķ��鲻��һ������");
            return false;
        }

        IXPData ixp = BlockAnalysis.GetIXP(sign, plugin.getConfig().getString("id"), plugin);
        if (ixp == null) {
            player.sendMessage("�����Ӳ���IXP����");
            return false;
        }
        if (player.hasPermission("ixp.admin") == false) {
            player.sendMessage("��Ȩ�޴���IXP����");
            return false;
        }

        Location site = block.getLocation();
        Database db = new Database(plugin);
        String worldName = block.getWorld().getName();
        if (db.selectSign(site.getBlockX(), site.getBlockY(), site.getBlockZ(), worldName) != null) {
            sender.sendMessage("�������Ѵ���");
            return false;
        }
        db.addSign(site.getBlockX(), site.getBlockY(), site.getBlockZ(), worldName, null, 0, ixp.getFee(), 1);
        plugin.sm.updateSignData();
        plugin.sm.updateAttachBlockData();

        player.sendMessage("������һ��IXP����");
        return true;
    }
}
