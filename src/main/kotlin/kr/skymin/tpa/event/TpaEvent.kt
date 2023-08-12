package kr.skymin.tpa.event

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.player.PlayerEvent


abstract class TpaEvent(player: Player, val target: Player) : PlayerEvent(player), Cancellable {
	private var isCancelled = false

	override fun isCancelled(): Boolean {
		return isCancelled
	}

	fun cancel() {
		setCancelled(true)
	}

	override fun setCancelled(cancel: Boolean) {
		isCancelled = cancel
	}
}