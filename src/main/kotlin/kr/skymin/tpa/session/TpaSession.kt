package kr.skymin.tpa.session

import org.bukkit.entity.Player

class TpaSession(val player: Player) {

	var canTeleport = false

	private val requestQueue = ArrayDeque<Player>()

	val queue: MutableSet<Player> get() = requestQueue.toMutableSet()

	val count: Int get() = requestQueue.size

	val pop: Player? get() = requestQueue.removeFirstOrNull()

	fun request(player: Player) : Boolean {
		if (requestQueue.contains(player)) {
			return false
		}
		requestQueue.add(player)
		return true
	}

	fun remove(player: Player) {
		requestQueue.remove(player)
	}
}