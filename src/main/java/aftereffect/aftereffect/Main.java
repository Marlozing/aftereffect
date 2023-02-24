package aftereffect.aftereffect;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Main extends JavaPlugin implements Listener {
    static ConsoleCommandSender consol = Bukkit.getConsoleSender();
    Inventory inv = Bukkit.createInventory(null,9,"스타트 물품");
    HashMap<Player,String> pitem = new HashMap<>();
    HashMap<Player,String> plimit = new HashMap<>();
    HashMap<Player,Integer> 수분량 = new HashMap<>();
    HashMap<Player,Integer> 포만감 = new HashMap<>();
    String difficulty = "쉬움";
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(this.getCommand("게임")).setExecutor(new command());

        ItemStack i = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) i.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE+"정화된 물");
        meta.setColor(Color.BLUE);
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + "섭취 시 수분량을 10 채워준다.");
        list.add(ChatColor.YELLOW+"소유량 : 0개");
        list.add(ChatColor.RED+"한도량 : 0개");
        list.add(ChatColor.GRAY+"좌클릭 시 개수가 증가합니다");
        list.add(ChatColor.GRAY+"우클릭 시 개수가 감소합니다");
        meta.setLore(list);
        i.setItemMeta(meta);
        inv.setItem(0,i);
        inv.setItem(1,item(Material.MUSHROOM_STEW,"식량","섭취 시 포만감을 10 채워준다.",null,null));
        inv.setItem(2,item(Material.CHAINMAIL_HELMET,"방독면","소지 시 야외에서 4분간 활동할 수 있다.",null,null));
        inv.setItem(3,item(Material.MILK_BUCKET,"양동이","물을 담을 수 있다",null,null));
        inv.setItem(4,item(Material.IRON_SWORD,"칼","말 그대로 칼",null,null));
        inv.setItem(5,item(Material.LIGHT,"손전등","어두운 건물 안에서 활동할 때 밝힐 수 있다.",null,null));
        inv.setItem(6,item(Material.MUSIC_DISC_13,"정화제","방사능에 오염된 물을 정화시켜준다.",null,null));
        inv.setItem(7,item(Material.FLOWER_BANNER_PATTERN,"지도","마을의 지도",null,null));
        inv.setItem(8,item(Material.EMERALD,"에메랄드","화폐",null,null));
        new BukkitRunnable(){
            @Override
            public void run(){
                for(Player p : Bukkit.getOnlinePlayers()){
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(피로(p)));
                }
            }
        }.runTaskTimer(this,20L,20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    class command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player p1 = (Player) sender;
            if(args.length>=1) {
                if (args[0].equals("시작")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getGameMode() != GameMode.SPECTATOR) {
                            pitem.put(p,"000000000");
                            plimit.put(p,"");
                            Inventory inv2 = Bukkit.createInventory(null,9,"스타트 물품");
                            inv2.setContents(inv.getContents());
                            p.openInventory(inv2);
                            Random ran = new Random();
                            for(int i = 0; i < 6; i++){
                                while(true){
                                    int a = ran.nextInt(9);
                                    ItemMeta meta = inv2.getItem(a).getItemMeta();
                                    List<String> list;
                                    list = meta.getLore();
                                    if(list.get(2).equals(ChatColor.RED+"한도량 : 0개")){
                                        int b = ran.nextInt(3);
                                        b++;
                                        list.set(2,ChatColor.RED+"한도량 : "+b+"개");
                                        meta.setLore(list);
                                        inv2.getItem(a).setItemMeta(meta);
                                        break;
                                    }
                                }
                            }
                            for(int i = 0; i < 9; i++){
                                String[] str = ChatColor.stripColor(inv2.getItem(i).getItemMeta().getLore().get(2)).split(" ");
                                plimit.put(p,plimit.get(p)+str[2].replace("개",""));
                            }
                        }
                    }
                    return true;
                }
                if(args[0].equals("설정")){
                    if(args.length==2){
                        if(args[1].equals("난이도")){
                            if(difficulty.equals("쉬움")){
                                difficulty = "보통";
                            }else if(difficulty.equals("보통")){
                                difficulty = "어려움";
                            }else if(difficulty.equals("어려움")){
                                difficulty = "쉬움";
                            }
                        }
                    }
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        for(int i = 0; i < 20; i++){
                            p.sendMessage("");
                        }
                    }
                    TextComponent msg1 = new TextComponent(ChatColor.YELLOW + "난이도 : ");
                    TextComponent msg2 = new TextComponent(ChatColor.RED + difficulty);
                    msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/게임 설정 난이도"));
                    msg1.addExtra(msg2);
                    p1.spigot().sendMessage(msg1);
                    String difficult = ChatColor.YELLOW+ "난이도 : " + ChatColor.RED+difficulty;
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if (p1 != p) {
                            p.sendMessage(difficult);
                        }
                    }
                }
                if(args[0].equals("test")){
                    수분량.put(p1,수분량.get(p1) - 5);
                }
            }
            return false;
        }
    }
    @EventHandler
    public void a(InventoryClickEvent a){
        Player p = (Player) a.getWhoClicked();
        if (a.getRawSlot() >= a.getInventory().getSize()) {
            return;
        }
        if(a.getCurrentItem()!=null&&a.getCurrentItem()!= new ItemStack(Material.AIR)){
            if(p.getOpenInventory().getTitle().equals("스타트 물품")){
                a.setCancelled(true);
                Integer[] ints = stoi(pitem.get(p).split(""));
                int i = 0;
                if(a.getClick()==ClickType.LEFT){
                    i = 0;
                    for(Integer i1 : ints){
                        i+=i1;
                    }
                    if(i>=dint()){
                        p.sendMessage(ChatColor.RED+String.valueOf(dint())+"개 이상 가져갈 수 없습니다");
                        return;
                    }
                    Integer[] limit = stoi(plimit.get(p).split(""));
                    if(ints[a.getRawSlot()] == limit[a.getRawSlot()]){
                        p.sendMessage(ChatColor.RED+"한도량을 초과할 수 없습니다");
                        return;
                    }
                    ints[a.getRawSlot()] += 1;
                }else if(a.getClick()==ClickType.RIGHT){
                    if(ints[a.getRawSlot()]>0){
                        ints[a.getRawSlot()]-=1;
                    }else{
                        p.sendMessage(ChatColor.RED+"0개 이하로 설정할 수 없습니다");
                        return;
                    }
                }else{
                    return;
                }
                List<String> lore = a.getCurrentItem().getItemMeta().getLore();
                lore.set(1,ChatColor.YELLOW+"소유량 : "+ints[a.getRawSlot()]+"개");
                ItemMeta meta = a.getCurrentItem().getItemMeta();
                meta.setLore(lore);
                a.getCurrentItem().setItemMeta(meta);
                String str = itos(ints);
                pitem.put(p,str);
            }
        }

    }
    public ItemStack item(Material material,String name,String lore,ChatColor color1,ChatColor color2){
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
            meta.setLore(list);
        }
        item.setItemMeta(meta);
        return item;
    }
    public Integer[] stoi(String[] list){
        Integer[] ints = new Integer[9];
        int i = 0;
        for(String str : list){
            ints[i] = Integer.parseInt(str);
            i++;
        }
        return ints;
    }
    public String itos(Integer[] list){
        StringBuilder str = new StringBuilder();
        for(Integer i1 : list){
            str.append(i1);
        }
        return str.toString();
    }
    public String 피로(Player p) {
        if (수분량.containsKey(p)) {
            if (수분량.get(p) > 100) {
                수분량.put(p, 100);
            }
        } else {
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
        return text.toString();
    }
    public Integer dint(){
        Integer i;
        if(difficulty.equals("쉬움")){
           i = 8;
        }else if(difficulty.equals("보통")){
            i = 6;
        }else{
            i = 5;
        }
        return i;
    }
}
