package cn.eatmedicine.minecraft.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.plugin.java.JavaPlugin;

import cn.eatmedicine.minecraft.IXPData.IXPData;
import cn.eatmedicine.minecraft.IXPData.IXPType;


public class BlockAnalysis {

    private static final String IXPFirstLine = "[IXP]";
    private static final String IXPSecondLineSend = "SEND";
    private static final String IXPSecondLineReceive = "RECEIVE";


    public static boolean isSign(Block block) {
        if (block != null &&
                (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN))) {
            return true;
        }
        return false;
    }

    //��block��ȡһ��Sign���ӵĶ������block������Sign�򷵻�null
    public static Sign GetSign(Block block) {
        BlockState bs = block.getState();
        //���block����Sign
        if (bs instanceof Sign) {
            Sign sign = (Sign) bs;
            return sign;
        }
        return null;
    }

    //�ж��Ƿ��Ƿ���IXP��ʽ������
    public static IXPData GetIXP(Sign sign, String fromServer, JavaPlugin plugin) {
        String line1 = sign.getLine(0);
        String line2 = sign.getLine(1);
        String line3 = sign.getLine(2);
        String line4 = sign.getLine(3);

        //ǰ���в�ƥ��
        if (!line1.equals(IXPFirstLine)) {
            return null;
        }
        IXPType type = IXPType.getIXPType(line2);
        if (type == IXPType.UNDEFINED)
            return null;

        //����Ƿ��������ҵ�����Ϊ�� �����
        if (type == IXPType.SEND && line3.equals(""))
            return null;

        //�������Ƿ�ƥ��Ϊ����
        double fee;
        try {
            fee = Double.parseDouble(line4);
        } catch (NumberFormatException e) {
            return null;
        }
        IXPData data = new IXPData(type, fee, fromServer, line3);
        //���ﻹȱ��һ���жϷ����������Ƿ���ȷ
        //

        return data;
    }

    //�ж��Ƿ��Ƿ���IXP��ʽ������
    public static IXPData GetIXP(String[] lines, String fromServer, JavaPlugin plugin) {
        if (lines.length < 3)
            return null;
        String line1 = lines[0];
        String line2 = lines[1];
        String line3 = lines[2];
        String line4 = lines[3];

        //ǰ���в�ƥ��
        if (!line1.equals(IXPFirstLine)) {
            return null;
        }
        IXPType type = IXPType.getIXPType(line2);
        if (type == IXPType.UNDEFINED)
            return null;

        //����Ƿ��������ҵ�����Ϊ�� �����
        if (type == IXPType.SEND && line3.equals(""))
            return null;

        //�������Ƿ�ƥ��Ϊ����
        int fee;
        try {
            fee = Integer.parseInt(line4);
        } catch (NumberFormatException e) {
            return null;
        }
        IXPData data;
        if(type==IXPType.SEND){
             data = new IXPData(type, fee, fromServer, line3);
        }
        else{
            data = new IXPData(type, fee, fromServer, null);
        }


        return data;
    }
}
