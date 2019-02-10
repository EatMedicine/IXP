package cn.eatmedicine.minecraft.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

import cn.eatmedicine.minecraft.Database.Database;
import cn.eatmedicine.minecraft.Database.SignData;
import cn.eatmedicine.minecraft.utils.BlockAnalysis;

public class SignManager {
    private final JavaPlugin plugin;
    public List<SignData> signData = new ArrayList<>();
    public HashMap<Location, Block> attachBlockData = new HashMap<>();

    public SignManager(JavaPlugin plugin) {
        this.plugin = plugin;
        updateSignData();
    }

    public void updateSignData() {
        Database db = new Database(plugin);
        signData = db.selectAllSign();
        plugin.getLogger().info("signDataNum:" + signData.size());
        updateAttachBlockData();
    }

    public void updateAttachBlockData() {
        attachBlockData = new HashMap<>();
        if (signData.isEmpty() == true)
            return;
        for (SignData s : signData) {
            Location l = new Location(Bukkit.getWorld(s.world), s.x, s.y, s.z);
            Block attach = getAttachedBlock(l.getBlock());
            plugin.getLogger().info("AttachBlock:" + attach.toString());
            attachBlockData.put(l, attach);
        }
    }

    //��ȡ���������ŵķ���
    public static Block getAttachedBlock(Block block) {
        Sign s = BlockAnalysis.GetSign(block);
        if (s != null) {
            org.bukkit.material.Sign sign = (org.bukkit.material.Sign) block.getState().getData();
            return block.getRelative(sign.getAttachedFace());
        }
        return null;
    }

    //���block��һ���Ѽ�¼��IXP���ӣ���ȡ��SignData
    public SignData getIXPSign(Block block) {
        if (block == null)
            return null;
        for (SignData sd : signData) {
            Location local = block.getLocation();
            if (sd.x == local.getBlockX() &&
                    sd.y == local.getBlockY() &&
                    sd.z == local.getBlockZ() &&
                    sd.world.equals(block.getWorld().getName())) {
                return sd;
            }
        }
        return null;
    }

    //block�Ƿ���һ���Ѿ���¼��IXP����
    public boolean isIXPSign(Block block) {
        if (block == null)
            return false;
        for (SignData sd : signData) {
            if (sd.x == block.getX() &&
                    sd.y == block.getY() &&
                    sd.z == block.getZ() &&
                    sd.world.equals(block.getWorld().getName())) {
                return true;
            }
        }
        return false;
    }

    //�ж�Block�Ƿ����ڱ�IXP����
    public boolean isAttachBlock(Block block) {
        if (block == null)
            return false;
        return attachBlockData.containsValue(block);
    }
}
