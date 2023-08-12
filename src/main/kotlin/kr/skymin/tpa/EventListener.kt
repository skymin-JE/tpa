package kr.skymin.tpa

import kr.skymin.tpa.session.SessionManager
import kr.skymin.tpa.session.TpaSession
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent

class EventListener : Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	fun onJoin(event: PlayerJoinEvent) {
		SessionManager.addSession(event.player)
	}

	@EventHandler(priority = EventPriority.MONITOR)
	fun onQuit(event: PlayerQuitEvent) {
		SessionManager.removeSession(event.player)
	}

	@EventHandler(priority = EventPriority.MONITOR)
	fun onTeleport(event: PlayerTeleportEvent) {
		SessionManager.cancleAll(event.player)
	}

	@EventHandler(priority = EventPriority.LOWEST)
	fun onMove(event: PlayerMoveEvent) {
		val session: TpaSession = SessionManager.getSession(event.player)
		if (event.hasChangedPosition() && session.canTeleport) {
			event.isCancelled = true
		}
	}
}