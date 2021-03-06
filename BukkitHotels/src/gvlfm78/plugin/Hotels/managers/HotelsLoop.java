package gvlfm78.plugin.Hotels.managers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import gvlfm78.plugin.Hotels.HotelsMain;
import gvlfm78.plugin.Hotels.handlers.HotelsCommandHandler;
import gvlfm78.plugin.Hotels.handlers.HotelsConfigHandler;

public class HotelsLoop extends BukkitRunnable {

	FilenameFilter SignFileFilter;
	public HotelsLoop(){}
	
	HotelsMain plugin = new HotelsMain();
	HotelsMessageManager HMM = new HotelsMessageManager();
	SignManager SM = new SignManager();
	HotelsFileFinder HFF = new HotelsFileFinder();
	WorldGuardManager WGM = new WorldGuardManager();
	HotelsConfigHandler HConH = new HotelsConfigHandler();

	public HotelsLoop(HotelsCommandHandler hotelsCommandHandler) {}

	@Override
	public void run() {
		File dir = new File("plugins"+File.separator+"Hotels"+File.separator+"Signs");
		if(!(dir.exists()))
			dir.mkdir();

		ArrayList<String> fileslist = HFF.listFiles("plugins"+File.separator+"Hotels"+File.separator+"Signs");
		for(String x: fileslist){
			File file = HConH.getFile("Signs"+File.separator+x);
			if(file.getName().matches("Reception-.+-.+")){
				//It's a reception sign
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				World world = Bukkit.getWorld(config.getString("Reception.location.world").trim());
				int locx = config.getInt("Reception.location.x");
				int locy = config.getInt("Reception.location.y");
				int locz = config.getInt("Reception.location.z");
				Block b = world.getBlockAt(locx,locy,locz);
				Location l = b.getLocation();
				if(SM.updateReceptionSign(l)==true){
					file.delete();
					b.setType(Material.AIR);
					plugin.getLogger().info(HMM.mesnopre("sign.delete.reception").replaceAll("%filename%", file.getName()));
				}
			}
			else{
				//Room sign
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				String hotelName = config.getString("Sign.hotel");
				World world = Bukkit.getWorld(config.getString("Sign.location.world"));
				int roomNum = config.getInt("Sign.room");
				int locx = config.getInt("Sign.location.coords.x");
				int locy = config.getInt("Sign.location.coords.y");
				int locz = config.getInt("Sign.location.coords.z");
				Block signblock = world.getBlockAt(locx, locy, locz);
				if((signblock.getType().equals(Material.WALL_SIGN))||(signblock.getType().equals(Material.SIGN_POST))||(signblock.getType().equals(Material.SIGN))){
					Sign sign = (Sign) signblock.getState();
					if(hotelName.equalsIgnoreCase(ChatColor.stripColor(sign.getLine(0)))){
						String[] Line2parts = ChatColor.stripColor(sign.getLine(1)).split("\\s");
						int roomNumfromSign = Integer.valueOf(Line2parts[1].trim()); //Room Number
						if(roomNum==roomNumfromSign){
							//Room numbers match
							if(config.get("Sign.expiryDate")!=null){
								long expirydate = config.getLong("Sign.expiryDate");
								if(expirydate!=0){
									if(expirydate<=((System.currentTimeMillis())/1000/60)){//If rent has expired
										String r = config.getString("Sign.region");
										ProtectedRegion region = WGM.getRM(world).getRegion(r);
										if(config.getString("Sign.renter")!=null){
											OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(UUID.fromString(config.getString("Sign.renter")));
											WGM.removeMember(p, region);

											//Removing friends
											List<String> stringList = config.getStringList("Sign.friends");
											for(String currentFriend : stringList){
												OfflinePlayer cf = Bukkit.getServer().getOfflinePlayer(UUID.fromString(currentFriend));
												WGM.removeMember(cf, region);
											}
											//If set in config, make room accessible to all players now that it is not rented
											WGM.makeRoomAccessible(region);
											if(HConH.getconfigyml().getBoolean("settings.stopOwnersEditingRentedRooms")){
												
												region.setFlag(DefaultFlag.BLOCK_BREAK, null);
												region.setFlag(DefaultFlag.BLOCK_PLACE, null);
												region.setPriority(1);
											}

											sign.setLine(3, ChatColor.GREEN+HMM.mesnopre("sign.vacant"));
											sign.update();
											plugin.getLogger().info(HMM.mesnopre("sign.rentExpiredConsole").replaceAll("%room%", String.valueOf(roomNum)).replaceAll("%hotel%", hotelName).replaceAll("%player%", p.getName()));
											if(p.isOnline()){
												Player op = Bukkit.getServer().getPlayer(UUID.fromString(config.getString("Sign.renter")));
												op.sendMessage(HMM.mes("sign.rentExpiredPlayer").replaceAll("%room%", String.valueOf(roomNum)).replaceAll("%hotel%", hotelName).replaceAll("(?i)&([a-fk-r0-9])", "\u00A7$1"));
											}
											else{
												YamlConfiguration queue = HConH.getMessageQueue();
												if(!queue.contains("messages.expiry")){
													queue.createSection("messages.expiry");
													HConH.saveMessageQueue(queue);
												}
												Set<String> expiryMessages = queue.getConfigurationSection("messages.expiry").getKeys(false);
												int expiryMessagesSize = expiryMessages.size();
												String pathToPlace = "messages.expiry."+(expiryMessagesSize+1);
												queue.set(pathToPlace+".UUID", p.getUniqueId().toString());
												queue.set(pathToPlace+".message", HMM.mes("sign.rentExpiredPlayer").replaceAll("%room%", String.valueOf(roomNum)).replaceAll("%hotel%", hotelName).replaceAll("(?i)&([a-fk-r0-9])", "\u00A7$1"));
												HConH.saveMessageQueue(queue);
											}
											config.set("Sign.renter", null);
											config.set("Sign.timeRentedAt", null);
											config.set("Sign.expiryDate", null);
											config.set("Sign.friends", null);
											config.set("Sign.extended", null);
											config.set("Sign.userHome.x", null);
											config.set("Sign.userHome.y", null);
											config.set("Sign.userHome.z", null);
											try {
												config.save(file);
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
										//Resetting time on sign to default
										sign.setLine(2, SM.TimeFormatter(config.getLong("Sign.time")));
										sign.update();
									}
									else{
										//Updating time remaining till expiry
										long expiryDate = config.getLong("Sign.expiryDate");
										long currentmins = System.currentTimeMillis()/1000/60;
										//Time remaining
										sign.setLine(2, SM.TimeFormatter(expiryDate-currentmins));
										sign.update();
									}
								}
							}
							else{
								config.set("Sign.renter", null);
								config.set("Sign.timeRentedAt", null);
								config.set("Sign.expiryDate", null);
								config.set("Sign.friends", null);
								config.set("Sign.extended", null);
								try {
									config.save(file);
								} catch (IOException e) {
									e.printStackTrace();
								}
								sign.setLine(3, ChatColor.GREEN+HMM.mesnopre("sign.vacant"));
								sign.setLine(2, SM.TimeFormatter(config.getLong("Sign.time")));
								sign.update();
							}
						}
						else{
							file.delete();
							plugin.getLogger().info(HMM.mesnopre("sign.delete.roomNum").replaceAll("%filename%", file.getName()));}
					}
					else{
						file.delete();
						plugin.getLogger().info(HMM.mesnopre("sign.delete.hotelName").replaceAll("%filename%", file.getName()));}
				}
				else{
					file.delete();
					plugin.getLogger().info(HMM.mesnopre("sign.delete.location").replaceAll("%filename%", file.getName()));}
			}
		}
		//Handling hotel files
		File hdir = new File("plugins"+File.separator+"Hotels"+File.separator+"Hotels");
		if(!(hdir.exists()))
			hdir.mkdir();

		ArrayList<String> hfileslist = HFF.listFiles("plugins"+File.separator+"Hotels"+File.separator+"Hotels");
		for(String h:hfileslist){
			File file = HConH.getFile("Hotels"+File.separator+h);
			YamlConfiguration hcon = YamlConfiguration.loadConfiguration(file);
			String buyeruuid = hcon.getString("Hotel.sell.buyer");
			if(buyeruuid!=null){
				String hotelName = file.getName().replaceFirst(".yml", "");
				OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(buyeruuid));
				if(op.isOnline()){
					Player oop = (Player) op;
					oop.sendMessage(HMM.mes("hotels.commands.buyhotel.expired").replaceAll("%hotel%", hotelName));
				}
				List<World> worlds = Bukkit.getWorlds();
				for(World currentw:worlds){
					if(WGM.hasRegion(currentw, "hotel-"+hotelName)){
						ProtectedRegion hr = WGM.getRegion(currentw, "hotel-"+hotelName);
						Set<String> owners = hr.getOwners().getPlayers();
						for(String co:owners){
							@SuppressWarnings("deprecation")
							OfflinePlayer cof = Bukkit.getOfflinePlayer(co);
							if(cof.isOnline()){
								Player cop = (Player) cof;
								cop.sendMessage(HMM.mes("hotels.commands.sellhotel.expired").replaceAll("%hotel%", hotelName));
							}
						}
					}
				}
				
				hcon.set("Hotel.sell.buyer", null);
				hcon.set("Hotel.sell.price", null);
				try {
					hcon.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
