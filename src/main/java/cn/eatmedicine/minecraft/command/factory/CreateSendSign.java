package cn.eatmedicine.minecraft.command.factory;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cn.eatmedicine.minecraft.Main;
import cn.eatmedicine.minecraft.Database.Database;
import cn.eatmedicine.minecraft.IXPData.IXPData;
import cn.eatmedicine.minecraft.IXPData.IXPType;
import cn.eatmedicine.minecraft.config.serverIds;
import cn.eatmedicine.minecraft.utils.BlockAnalysis;

public class CreateSendSign implements IHandleCommand {
    public String serverName;
    private Main plugin;
    CommandSender sender;

    public CreateSendSign(Main plugin, CommandSender sender, String serverName) {
        this.plugin = plugin;
        this.sender = sender;
        this.serverName = serverName;
    }

    @Override
    public boolean handleCommand() {
        //����Ƿ����ƣ���ⷢ�����Ƿ��ڷ�����������
        serverIds server = plugin.cm.hasServer(serverName);
        if (server == null) {
            sender.sendMessage("�÷���������Ч\n");
            return false;
        }
        if (server.isEnable() == false) {
            sender.sendMessage("�÷������������ڻ���ע��\n");
            return false;
        }
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage("You must be a player!\n");
            return false;
        }
        Block block = player.getTargetBlock(null, 20);
        Sign sign = BlockAnalysis.GetSign(block);
        if (sign == null) {
            sender.sendMessage("ָ��ķ��鲻��һ������\n");
            return false;
        }

        IXPData ixp = BlockAnalysis.GetIXP(sign, plugin.getConfig().getString("id"), plugin);
        if (ixp == null) {
            player.sendMessage("�����Ӳ���IXP����\n");
            return false;
        }
        if (player.hasPermission("ixp.admin") == false) {
            player.sendMessage("��Ȩ�޴���IXP����\n");
            return false;
        }
        Location site = block.getLocation();
        Database db = new Database(plugin);
        String worldName = block.getWorld().getName();
        if (db.selectSign(site.getBlockX(), site.getBlockY(), site.getBlockZ(), worldName) != null) {
            sender.sendMessage("�������Ѵ���");
            return false;
        }
        sign.setLine(2, serverName);
        sign.update();
        db.addSign(site.getBlockX(), site.getBlockY(), site.getBlockZ(), worldName, serverName, 0, ixp.getFee(), 1);
        plugin.sm.updateSignData();
        plugin.sm.updateAttachBlockData();

        player.sendMessage("�ɹ�����һ��IXP����\n");
        return true;
    }

}
