package kr.skymin.tpa.utils

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

object TpaFormat {

	const val PREFIX = "§l§5《SYSTEM》§r"

	fun msg(player: Player, msg: String) {
		player.sendMessage(Component.text("$PREFIX $msg"))
	}
}