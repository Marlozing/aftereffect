package aftereffect.aftereffect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.*;

import java.util.*;

import static aftereffect.aftereffect.Main.*;
import static aftereffect.aftereffect.day.*;
public class function {
    public static ItemStack item(Material material, String name, String lore, ChatColor color1, ChatColor color2){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(color1==null){
            meta.setDisplayName(ChatColor.WHITE+name);
        }else{
            meta.setDisplayName(color1+name);
        }
        List<String> list = new ArrayList<>();
        if(color1==null){
            list.add(ChatColor.WHITE+lore);
            meta.setLore(list);
        }else {
            list.add(color2 + lore);
        }
        list.add(ChatColor.YELLOW+"소유량 : 0개");
        list.add(ChatColor.RED+"한도량 : 0개");
        list.add(ChatColor.GRAY+"좌클릭 시 개수가 증가합니다");
        list.add(ChatColor.GRAY+"우클릭 시 개수가 감소합니다");
        if(color1==null){
            meta.setLore(list);
        }else{
            List<String> s = new ArrayList<>();
            for(String str : list){
                s.add(color1+str);
            }
            meta.setLore(s);
        }
        item.setItemMeta(meta);
        return item;
    }
    public static Integer[] stoi(String[] list){
        Integer[] ints = new Integer[7];
        int i = 0;
        for(String str : list){
            ints[i] = Integer.parseInt(str);
            i++;
        }
        return ints;
    }
    public static String itos(Integer[] list){
        StringBuilder str = new StringBuilder();
        for(Integer i1 : list){
            str.append(i1);
        }
        return str.toString();
    }
    public static String 수분(Player p) {
        if (!수분량.containsKey(p)) {
            수분량.put(p, 100);
        }
        int tired = 수분량.get(p) * 10;
        tired = 1000 - tired;
        tired /= 10;
        int count = 10;

        StringBuilder text;
        for(text = new StringBuilder(); count != 110; count += 10) {
            if (tired >= count) {
                text.append("\ue003");
            } else if (tired >= count - 5) {
                text.append("\ue002");
            } else {
                text.append("\ue001");
            }
        }
        text.insert(0, "                         ");
        text.append(" ");
        return text.toString();
    }
    public static void 포만(Player p) {
        if (!포만감.containsKey(p)) {
            포만감.put(p, 100);
        }
        int i = 포만감.get(p) / 5;
        p.setFoodLevel(i);
    }
    public static Integer dint(){
        int i;
        if(difficulty.equals("쉬움")){
            i = 8;
        }else if(difficulty.equals("보통")){
            i = 6;
        }else{
            i = 5;
        }
        return i;
    }
    public static ItemStack stoi(String str){
        ItemStack item = null;
        if(str.equals("물")) item = 오염된물;
        if(str.equals("식량")) item = 식량;
        if(str.equals("방독면")) item = 방독면;
        if(str.equals("정화제")) item = 정화제;
        if(str.equals("에메랄드")) item = 에메랄드;
        if(str.equals("양동이")) item = 양동이;
        return item;
    }
    public static void setting(){
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() != GameMode.SPECTATOR) {
                pitem.put(p,"0000000");
                plimit.put(p,"");
                Inventory inv2 = Bukkit.createInventory(null,9,"스타트 물품");
                inv2.setContents(inv.getContents());
                p.openInventory(inv2);
                List<Integer> limit = new ArrayList<>();
                for(int i = 1; i < 8; i++){
                    limit.add(i);
                }
                Collections.shuffle(limit);
                for(Integer i : limit){
                    int i2 = new Random().nextInt(3);
                    i2++;
                    ItemStack item = inv2.getItem(i);
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.set(2,ChatColor.RED+"한도량 : "+i2+"개");
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    inv2.setItem(i,item);
                }
                for(int i = 1; i < 8; i++){
                    String[] str = ChatColor.stripColor(inv2.getItem(i).getItemMeta().getLore().get(2)).split(" ");
                    plimit.put(p,plimit.get(p)+str[2].replace("개",""));
                }
            }
        }
    }
    static public void createBoard(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("test", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GRAY+"후유증");
        Score d = objective.getScore("Day : "+day);
        d.setScore(4);
        Score score1 = objective.getScore("점수 : "+score+"점");
        score1.setScore(0);
        Score score2 = objective.getScore("");
        score2.setScore(1);
        Integer player = 0;
        for(Player p1 : Bukkit.getOnlinePlayers()){
            if(p1.getGameMode()!=GameMode.SPECTATOR){
                player++;
            }
        }
        Score score3 = objective.getScore("남은 인원 : "+player+"명");
        score3.setScore(2);
        if(!time.containsKey(p)){
            time.put(p,0);
        }
        Score score4 = objective.getScore(" ");
        score4.setScore(3);
        Score score5 = objective.getScore("  ");
        score5.setScore(-1);
        if(time.get(p)>0){
            Score score6 = objective.getScore("남은 시간 : "+time.get(p)+"초");
            score6.setScore(-2);
        }
        p.setScoreboard(board);
    }
    static public void chest(){
        String[] strs = getCustomConfig().getString("chest").split("/");
        ArrayList<Chest> chests = new ArrayList<>();
        for(String str : strs){
            String[] str2s = str.split(" ");
            Block block = Bukkit.getWorld("world").getBlockAt(Integer.parseInt(str2s[0]),Integer.parseInt(str2s[1]),Integer.parseInt(str2s[2]));
            if (block.getState() instanceof Chest) {
                Chest chest = (Chest) block.getState();
                chest.getBlockInventory().clear();
                chests.add(chest);
            }
        }
        int i = new Random().nextInt(5);
        Collections.shuffle(chests);
        if (subbookbox.getState() instanceof Chest) {
            Chest subbox = (Chest) subbookbox.getState();
            for(int n  = 0; n < i; n++){
                int i2 = new Random().nextInt(27);
                int i3 = new Random().nextInt(9);
                chests.get(n).getBlockInventory().setItem(i2,subbox.getBlockInventory().getItem(i3));
            }
        }
        Collections.shuffle(chests);
        ArrayList<String> itemlist = new ArrayList<>();
        itemlist.add("물");
        itemlist.add("식량");
        itemlist.add("방독면");
        itemlist.add("정화제");
        itemlist.add("에메랄드");
        itemlist.add("양동이");
        Integer chestnumber;
        Integer loop;
        에메랄드.setAmount(1);
        for(String loopstr : itemlist){
            Collections.shuffle(chests);
            chestnumber = 0;
            loop = 0;
            while(loop < getCustomConfig().getInt(loopstr+"."+difficulty)){
                if(loopstr.equals("방독면")){
                    i = 1;
                }else{
                    if(loopstr.equals("에메랄드")){
                        i = new Random().nextInt(7);
                    }else{
                        i = new Random().nextInt(2);
                    }
                    i++;
                }
                loop+=i;
                for(int ni = 0; ni < i; ni++){
                    int n1 = new Random().nextInt(27);
                    while (chests.get(chestnumber).getBlockInventory().getItem(n1)!=new ItemStack(Material.AIR)&& chests.get(chestnumber).getBlockInventory().getItem(n1)!=null)
                        n1 = new Random().nextInt(27);
                    chests.get(chestnumber).getBlockInventory().setItem(n1,RemoveLore(stoi(loopstr)));
                }
                chestnumber++;
            }
        }
    }
    static public ItemStack 지도(){
        Integer i = new Random().nextInt(5);
        i++;
        ItemStack item = RemoveLore(지도);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(i);
        item.setItemMeta(meta);
        return item;
    }
}
