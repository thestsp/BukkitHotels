package managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class WorldGuardManager {

	public static WorldGuardPlugin getWorldGuard(){
		Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");


		if (p instanceof WorldGuardPlugin) return (WorldGuardPlugin) p;
		else return null;
	}

	public static void addOwner(Player p, ProtectedCuboidRegion r){
		DefaultDomain owners = new DefaultDomain();
		owners.addPlayer(WorldGuardManager.getWorldGuard().wrapPlayer(p));
		r.setOwners(owners);
	}

	public static void addMember(Player p, ProtectedCuboidRegion r){
		DefaultDomain members = new DefaultDomain();
		members.addPlayer(WorldGuardManager.getWorldGuard().wrapPlayer(p));
		r.setMembers(members);
	}

	public static void removeOwner(Player p, ProtectedCuboidRegion r){
		DefaultDomain owners = new DefaultDomain();
		owners.removePlayer(WorldGuardManager.getWorldGuard().wrapPlayer(p));
		r.setOwners(owners);
	}

	public static void removeMember(Player p, ProtectedCuboidRegion r){
		DefaultDomain members = new DefaultDomain();
		members.removePlayer(WorldGuardManager.getWorldGuard().wrapPlayer(p));
		r.setMembers(members);
	}

	public static void addRegion(Player p, ProtectedCuboidRegion r){
		getWorldGuard().getRegionManager(p.getWorld()).addRegion(r);
	}

	public static void saveRegions(World world){
		RegionManager regionManager = getWorldGuard().getRegionManager(world);

		try {
			regionManager.save();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	public static void hotelFlags(ProtectedCuboidRegion r,String hotelName){
		r.setFlag(DefaultFlag.PASSTHROUGH, State.ALLOW);
		r.setFlag(DefaultFlag.BUILD, State.DENY);
		r.setFlag(DefaultFlag.PVP, State.DENY);
		//r.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
		r.setFlag(DefaultFlag.PISTONS, State.DENY);
		r.setFlag(DefaultFlag.TNT, State.DENY);
		r.setFlag(DefaultFlag.LIGHTER, State.DENY);
		//r.setFlag(DefaultFlag.USE, State.DENY);
		r.setFlag(DefaultFlag.PLACE_VEHICLE, State.DENY);
		r.setFlag(DefaultFlag.DESTROY_VEHICLE, State.DENY);
		//r.setFlag(DefaultFlag.SLEEP, State.DENY);
		r.setFlag(DefaultFlag.MOB_DAMAGE, State.DENY);
		r.setFlag(DefaultFlag.MOB_SPAWNING, State.DENY);
		//r.setFlag(DefaultFlag.DENY_SPAWN, State.DENY);
		r.setFlag(DefaultFlag.INVINCIBILITY, State.DENY);
		//r.setFlag(DefaultFlag.EXP_DROPS, State.DENY);
		r.setFlag(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
		r.setFlag(DefaultFlag.OTHER_EXPLOSION, State.DENY);
		r.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, State.DENY);
		r.setFlag(DefaultFlag.GHAST_FIREBALL, State.DENY);
		r.setFlag(DefaultFlag.ENDER_BUILD, State.DENY);
		r.setFlag(DefaultFlag.GREET_MESSAGE, ("&cWelcome to the "+hotelName+" hotel"));
		r.setFlag(DefaultFlag.FAREWELL_MESSAGE, ("&gCome back soon to the "+hotelName+" hotel"));
		//r.setFlag(DefaultFlag.NOTIFY_ENTER, Boolean.FALSE);
		//r.setFlag(DefaultFlag.NOTIFY_LEAVE, Boolean.FALSE);
		r.setFlag(DefaultFlag.EXIT, State.ALLOW);
		r.setFlag(DefaultFlag.ENTRY, State.ALLOW);
		r.setFlag(DefaultFlag.LIGHTNING, State.DENY);
		r.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, State.DENY);
		r.setFlag(DefaultFlag.ENDERPEARL, State.DENY);
		r.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, State.DENY);
		r.setFlag(DefaultFlag.ITEM_DROP, State.ALLOW);
		/*r.setFlag(DefaultFlag.HEAL_AMOUNT, 0);
		r.setFlag(DefaultFlag.HEAL_DELAY, 0);
		r.setFlag(DefaultFlag.MIN_HEAL, 0);
		r.setFlag(DefaultFlag.MAX_HEAL, 20);
		r.setFlag(DefaultFlag.FEED_DELAY, 0);
		r.setFlag(DefaultFlag.FEED_AMOUNT, 20);
		r.setFlag(DefaultFlag.MIN_FOOD, 0);
		r.setFlag(DefaultFlag.MAX_FOOD, 20);*/
		r.setFlag(DefaultFlag.SNOW_FALL, State.DENY);
		r.setFlag(DefaultFlag.SNOW_MELT, State.DENY);
		r.setFlag(DefaultFlag.ICE_FORM, State.DENY);
		r.setFlag(DefaultFlag.ICE_MELT, State.DENY);
		r.setFlag(DefaultFlag.SOIL_DRY, State.DENY);
		//r.setFlag(DefaultFlag.GAME_MODE, State.DENY);
		r.setFlag(DefaultFlag.MUSHROOMS, State.DENY);
		r.setFlag(DefaultFlag.LEAF_DECAY, State.DENY);
		r.setFlag(DefaultFlag.GRASS_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.VINE_GROWTH, State.DENY);
		r.setFlag(DefaultFlag.SEND_CHAT, State.ALLOW);
		r.setFlag(DefaultFlag.RECEIVE_CHAT, State.ALLOW);
		r.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.LAVA_FIRE, State.DENY);
		r.setFlag(DefaultFlag.LAVA_FLOW, State.DENY);
		r.setFlag(DefaultFlag.WATER_FLOW, State.DENY);
		//r.setFlag(DefaultFlag.TELE_LOC, State.DENY);
		//r.setFlag(DefaultFlag.SPAWN_LOC, State.DENY);
		//r.setFlag(DefaultFlag.POTION_SPLASH, State.DENY);
		//r.setFlag(DefaultFlag.BLOCKED_CMDS, SetFlag<T>);
		//r.setFlag(DefaultFlag.ALLOWED_CMDS, State.DENY);
		//Double price = 0.0;
		//r.setFlag(DefaultFlag.PRICE, price);
		r.setFlag(DefaultFlag.BUYABLE, Boolean.FALSE);
	}
	public static void roomFlags(ProtectedCuboidRegion r,String hotelName,Player p,int roomNum){

		
		//TODO Optimise this messy code here
		r.setFlag(DefaultFlag.PASSTHROUGH, State.DENY);
		r.setFlag(DefaultFlag.BUILD, State.DENY);
		r.setFlag(DefaultFlag.PVP, State.DENY);
		
		r.setFlag(DefaultFlag.CHEST_ACCESS, State.DENY);
		r.setFlag(DefaultFlag.USE, State.DENY);
		r.setFlag(DefaultFlag.SLEEP, State.DENY);
		r.setFlag(DefaultFlag.POTION_SPLASH, State.DENY);
		
		RegionGroupFlag groupFlag1 = DefaultFlag.CHEST_ACCESS.getRegionGroupFlag();
		RegionGroupFlag groupFlag2 = DefaultFlag.USE.getRegionGroupFlag();
		RegionGroupFlag groupFlag3 = DefaultFlag.SLEEP.getRegionGroupFlag();
		RegionGroupFlag groupFlag4 = DefaultFlag.POTION_SPLASH.getRegionGroupFlag();
		
		try {
			RegionGroup groupValue = groupFlag1.parseInput(WorldGuardManager.getWorldGuard(), null, "non_members");
			r.setFlag(groupFlag1, groupValue);
		} catch (InvalidFlagFormat e) {
			e.printStackTrace();
		}
		try {
			RegionGroup groupValue = groupFlag2.parseInput(WorldGuardManager.getWorldGuard(), null, "non_members");
			r.setFlag(groupFlag2, groupValue);
		} catch (InvalidFlagFormat e) {
			e.printStackTrace();
		}
		try {
			RegionGroup groupValue = groupFlag3.parseInput(WorldGuardManager.getWorldGuard(), null, "non_members");
			r.setFlag(groupFlag3, groupValue);
		} catch (InvalidFlagFormat e) {
			e.printStackTrace();
		}
		try {
			RegionGroup groupValue = groupFlag4.parseInput(WorldGuardManager.getWorldGuard(), null, "non_members");
			r.setFlag(groupFlag4, groupValue);
		} catch (InvalidFlagFormat e) {
			e.printStackTrace();
		}
		
		
		r.setFlag(DefaultFlag.PISTONS, State.DENY);
		r.setFlag(DefaultFlag.TNT, State.DENY);
		r.setFlag(DefaultFlag.LIGHTER, State.DENY);
		
		//r.setFlag(DefaultFlag.PLACE_VEHICLE, State.DENY);
		//r.setFlag(DefaultFlag.DESTROY_VEHICLE, State.DENY);
		
		//r.setFlag(DefaultFlag.MOB_DAMAGE, State.DENY);
		r.setFlag(DefaultFlag.MOB_SPAWNING, State.DENY);
		//r.setFlag(DefaultFlag.DENY_SPAWN, State.DENY);
		//r.setFlag(DefaultFlag.INVINCIBILITY, State.DENY);
		r.setFlag(DefaultFlag.EXP_DROPS, State.ALLOW);
		r.setFlag(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
		r.setFlag(DefaultFlag.OTHER_EXPLOSION, State.DENY);
		r.setFlag(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, State.DENY);
		r.setFlag(DefaultFlag.GHAST_FIREBALL, State.DENY);
		//r.setFlag(DefaultFlag.ENDER_BUILD, State.DENY);
		r.setFlag(DefaultFlag.GREET_MESSAGE, ("&cWelcome to room "+roomNum+" , "+p.getName()));
		r.setFlag(DefaultFlag.FAREWELL_MESSAGE, ("&gCome back soon to your room"));
		//r.setFlag(DefaultFlag.NOTIFY_ENTER, State.DENY);
		//r.setFlag(DefaultFlag.NOTIFY_LEAVE, State.DENY);
		//r.setFlag(DefaultFlag.EXIT, State.ALLOW);
		//r.setFlag(DefaultFlag.ENTRY, State.ALLOW);
		r.setFlag(DefaultFlag.LIGHTNING, State.DENY);
		//r.setFlag(DefaultFlag.ENTITY_PAINTING_DESTROY, State.DENY);
		//r.setFlag(DefaultFlag.ENDERPEARL, State.DENY);
		//r.setFlag(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, State.DENY);
		r.setFlag(DefaultFlag.ITEM_DROP, State.ALLOW);
		/*r.setFlag(DefaultFlag.HEAL_AMOUNT, State.DENY);
		r.setFlag(DefaultFlag.HEAL_DELAY, State.DENY);
		r.setFlag(DefaultFlag.MIN_HEAL, State.DENY);
		r.setFlag(DefaultFlag.MAX_HEAL, State.DENY);
		r.setFlag(DefaultFlag.FEED_DELAY, State.DENY);
		r.setFlag(DefaultFlag.FEED_AMOUNT, State.DENY);
		r.setFlag(DefaultFlag.MIN_FOOD, State.DENY);
		r.setFlag(DefaultFlag.MAX_FOOD, State.DENY);
		r.setFlag(DefaultFlag.SNOW_FALL, State.DENY);
		r.setFlag(DefaultFlag.SNOW_MELT, State.DENY);
		r.setFlag(DefaultFlag.ICE_FORM, State.DENY);
		r.setFlag(DefaultFlag.ICE_MELT, State.DENY);
		r.setFlag(DefaultFlag.SOIL_DRY, State.DENY);
		//r.setFlag(DefaultFlag.GAME_MODE, State.DENY);
		r.setFlag(DefaultFlag.MUSHROOMS, State.DENY);
		r.setFlag(DefaultFlag.LEAF_DECAY, State.DENY);
		r.setFlag(DefaultFlag.GRASS_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.MYCELIUM_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.VINE_GROWTH, State.DENY);
		r.setFlag(DefaultFlag.SEND_CHAT, State.ALLOW);
		r.setFlag(DefaultFlag.RECEIVE_CHAT, State.ALLOW);
		r.setFlag(DefaultFlag.FIRE_SPREAD, State.DENY);
		r.setFlag(DefaultFlag.LAVA_FIRE, State.DENY);
		r.setFlag(DefaultFlag.LAVA_FLOW, State.DENY);
		r.setFlag(DefaultFlag.WATER_FLOW, State.DENY);*/
		//r.setFlag(DefaultFlag.TELE_LOC, com.sk89q.worldedit.Location);
		//r.setFlag(DefaultFlag.SPAWN_LOC, State.DENY);
		//r.setFlag(DefaultFlag.BLOCKED_CMDS, State.DENY);
		//r.setFlag(DefaultFlag.ALLOWED_CMDS, State.DENY);
		//Double price = 0.0;
		//r.setFlag(DefaultFlag.PRICE, price);
		r.setFlag(DefaultFlag.BUYABLE, Boolean.FALSE);
	}
}
