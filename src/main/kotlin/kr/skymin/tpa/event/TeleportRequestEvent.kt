package kr.skymin.tpa.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList

class TeleportRequestEvent(player: Player, target: Player) : TpaEvent(player, target) {

	companion object {
		@JvmStatic
		val handlerList = HandlerList()
	}

	override fun getHandlers(): HandlerList {
		return handlerList
	}
}