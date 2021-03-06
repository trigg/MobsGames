/*
 * This file is part of MobsGames.

    MobsGames is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MobsGames is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MobsGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.mobsoc.MobsGames.Player;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import uk.co.mobsoc.MobsGames.MobsGames;
import uk.co.mobsoc.MobsGames.Data.BlockData;
import uk.co.mobsoc.MobsGames.Game.Race;

@SuppressWarnings("deprecation")
public class RaceRunner extends AbstractPlayerClass {

	public RaceRunner(String player) {
		super(player);
	}
	@Override
	public void onEnable(){
		getPlayer().setAllowFlight(false);
		getPlayer().setGameMode(GameMode.SURVIVAL);
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setBoots(null);
		getPlayer().getInventory().setChestplate(null);
		getPlayer().getInventory().setHelmet(null);
		getPlayer().getInventory().setLeggings(null);
		getPlayer().updateInventory();
		getPlayer().setHealth(20);
		getPlayer().setFoodLevel(20);
	}
	@Override
	public void onDisable(){
		if(getPlayer()!=null){
			getPlayer().setAllowFlight(false);
			getPlayer().setGameMode(GameMode.SURVIVAL);
			getPlayer().getInventory().clear();
			getPlayer().getInventory().setBoots(null);
			getPlayer().getInventory().setChestplate(null);
			getPlayer().getInventory().setHelmet(null);
			getPlayer().getInventory().setLeggings(null);
			getPlayer().updateInventory();
			getPlayer().setHealth(20);
			getPlayer().setFoodLevel(20);
		}
		
	}
	
	public ArrayList<BlockData> taggedBlocks = new ArrayList<BlockData>();	
	@Override
	public void onEvent(Event event){
		if(event instanceof PlayerBucketFillEvent){
		}else if(event instanceof PlayerInteractEvent){
			Race race = (Race) MobsGames.getGame();
			PlayerInteractEvent e2 = (PlayerInteractEvent) event;
			if(e2.hasBlock()){
				Block b = e2.getClickedBlock();
				BlockData bd = new BlockData(b);
				if(race.isWinningBlock(bd)){
					e2.setCancelled(true);
					for(BlockData bd2 : taggedBlocks){
						if(bd2.isEqualLocation(bd)){
							return;
						}
					}
					taggedBlocks.add(bd);
				}
			}
		}else if(event instanceof PlayerBucketEmptyEvent){
		}else if(event instanceof PaintingBreakByEntityEvent){
			((PaintingBreakByEntityEvent) event).setCancelled(true);
		}else if(event instanceof PaintingPlaceEvent){
			((PaintingPlaceEvent) event).setCancelled(true);
		}else if(event instanceof EntityDamageByEntityEvent){
			Entity att = ((EntityDamageByEntityEvent) event).getDamager();
			Entity def = ((EntityDamageByEntityEvent) event).getEntity();
			if(att instanceof Projectile){
				att = ((Projectile) att).getShooter();
			}
			if(def instanceof Tameable){
				def = (Entity) ((Tameable) def).getOwner();
				if(def == null){ ((EntityDamageByEntityEvent) event).setCancelled(true); return; }
			}
			if(att instanceof Tameable){
				def = (Entity) ((Tameable) def).getOwner();
				if(def == null){ ((EntityDamageByEntityEvent) event).setCancelled(true); return; }
			}
			if(((Race)MobsGames.getGame()).canPvP){
				if(att instanceof HumanEntity && def instanceof HumanEntity){
					return;
				}
			}
			if(((Race) MobsGames.getGame()).canPvM){
				if((att instanceof HumanEntity && !(def instanceof HumanEntity)) ||
			       (!(att instanceof HumanEntity) && def instanceof HumanEntity)){
					return;
				}
			}
			((EntityDamageByEntityEvent) event).setCancelled(true);
		}else if(event instanceof EntityDamageEvent){
			return;
		}else if(event instanceof PlayerRespawnEvent){
			if(!canRespawn()){
				MobsGames.getGame().setPlayerClass(new GhostClass(getPlayerName()));
			}else{
				((PlayerRespawnEvent) event).setRespawnLocation(MobsGames.getGame().getNextStartSpawn(this));
			}
		}
	}
}

