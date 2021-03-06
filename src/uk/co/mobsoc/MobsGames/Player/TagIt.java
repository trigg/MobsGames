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

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import uk.co.mobsoc.MobsGames.MobsGames;


@SuppressWarnings("deprecation")
public class TagIt extends AbstractPlayerClass {

	public TagIt(String player) {
		super(player);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onEnable(){
		if(getPlayer()!=null){
			getPlayer().getInventory().clear();
			getPlayer().setItemInHand(new ItemStack(Material.STICK));
			getPlayer().updateInventory();
		}
		MobsGames.announce(getPlayer().getName()+" is it!");
		getPlayer().setAllowFlight(false);
		getPlayer().setGameMode(GameMode.SURVIVAL);
	}
	@Override
	public void onDisable(){
		if(getPlayer()!=null){
			getPlayer().getInventory().clear();
			getPlayer().updateInventory();
		}
	}
	@Override
	public void onEvent(Event event){
		if(event instanceof PlayerBucketFillEvent){
			((PlayerBucketFillEvent) event).setCancelled(true);
		}else if(event instanceof PlayerBucketEmptyEvent){
			((PlayerBucketEmptyEvent) event).setCancelled(true);
		}else if(event instanceof PaintingBreakByEntityEvent){
			((PaintingBreakByEntityEvent) event).setCancelled(true);
		}else if(event instanceof PaintingPlaceEvent){
			((PaintingPlaceEvent) event).setCancelled(true);
		}else if(event instanceof EntityDamageByEntityEvent){
			((EntityDamageByEntityEvent) event).setDamage(0);
			if(!MobsGames.getGame().hasBegun()){ return; }
			EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) event;
			if(e2.getDamager() instanceof Player && e2.getEntity() instanceof Player){
				if(isPlayerEqual((Player) e2.getDamager(), getPlayer())){
					MobsGames.getGame().setPlayerClass(new TagRunner(getPlayerName()));
					MobsGames.getGame().setPlayerClass(new TagIt(((Player) e2.getEntity()).getName()));
				}
			}
		}else if(event instanceof EntityDamageEvent){
			((EntityDamageEvent) event).setCancelled(true);
		}
	}
}
