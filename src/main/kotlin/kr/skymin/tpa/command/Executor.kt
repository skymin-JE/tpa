package kr.skymin.tpa.command

import kr.skymin.tpa.Loader
import kr.skymin.tpa.event.AcceptTeleportEvent
import kr.skymin.tpa.event.TeleportRequestEvent
import kr.skymin.tpa.extensions.scheduleSyncDelayedTask
import kr.skymin.tpa.session.SessionManager
import kr.skymin.tpa.session.TpaSession
import kr.skymin.tpa.utils.TpaFormat
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object Executor {

	fun onBase(player: Player, plugin: Loader) {
		val session: TpaSession = SessionManager.getSession(player)
		val rp: Player? = session.pop
		if (rp === null) {
			player.sendMessage(
				text(
					"§l========== 명령어 ==========§r\n" +
							"/tpa <player> - 해당 플레이어에rp 요청한다.\n" +
							"/tpaccept     - 가장 오래된 요청을 수락한다.\n" +
							"/tpadeny      - 가장 오래된 요청을 거절 한다.\n" +
							"/tpacancel    - 모든 요청들을 취소한다.\n" +
							"/tpalist      - 오래된 순으로 리스트를 확인한다."
				)
			)
		} else {
			val rpSession: TpaSession = SessionManager.getSession(rp)
			rpSession.canTeleport = true
			TpaFormat.msg(rp, "${player.name}님께서 순간이동을 수락하셨습니다. 3초 뒤 이동됩니다.")
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 60) {
				if (rp.isOnline && SessionManager.getSession(rp).canTeleport) {
					rpSession.canTeleport = false
					if (AcceptTeleportEvent(player, rp).callEvent()) {
						rp.teleport(player)
					}
				}
			}
			if (session.count > 0) {
				TpaFormat.msg(player, "남은 순간이동 요청: ${session.count}개")
			}
		}
	}

	fun onRequest(player: Player, target: Player) {
		if(player === target) {
			TpaFormat.msg(player, "자기 자신에게 요청 할 수 없습니다.")
			return
		}
		if (!TeleportRequestEvent(player, target).callEvent()) return
		val session: TpaSession = SessionManager.getSession(target)
		if(!session.request(player)) {
			TpaFormat.msg(player, "${target.name}님께는 이미 요청을 보냈습니다.")
		}
		TpaFormat.msg(player, "${target.name}님께 순간이동 요청을 보냈습니다.")
		TpaFormat.msg(target, "${player.name}님꼐 순간이동 요청을 받았습니다.")
		val msg: TextComponent = text("${TpaFormat.PREFIX} ")
			.append {
				text("§2§n/tpaccept")
					.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
			}
			.append(text(" 또는 "))
			.append {
				text("§c§n/tpadeny")
					.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"))
			}.append(text("으로 수락 또는 거절을 할 수 있습니다."))
		target.sendMessage(msg)
		TpaFormat.msg(target, "받은 순간이동 요청: ${session.count}개")
	}

	fun onAccept(player: Player, plugin: Loader) {
		val session: TpaSession = SessionManager.getSession(player)
		val rp: Player? = session.pop
		if (rp === null) {
			TpaFormat.msg(player, "받은 순간이동 요청 0개")
		} else {
			val rpSession: TpaSession = SessionManager.getSession(rp)
			rpSession.canTeleport = true
			TpaFormat.msg(player, "${rp.name}님의 요청을 수락하셨습니다.")
			TpaFormat.msg(rp, "${player.name}님께서 순간이동을 수락하셨습니다. 3초 뒤 이동됩니다.")
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, 60) {
				if (rp.isOnline && SessionManager.getSession(rp).canTeleport) {
					rpSession.canTeleport = false
					if (AcceptTeleportEvent(player, rp).callEvent()) {
						rp.teleport(player)
					}
				}
			}
			if (session.count > 0) {
				TpaFormat.msg(player, "남은 순간이동 요청: ${session.count}개")
			}
		}
	}

	fun onDeny(player: Player) {
		val session: TpaSession = SessionManager.getSession(player)
		val rp: Player? = session.pop
		if (rp === null) {
			TpaFormat.msg(player, "받은 순간이동 요청 0개")
		} else {
			TpaFormat.msg(player, "${rp.name}님의 요청을 거절 하였습니다.")
			TpaFormat.msg(rp, "${player.name}님께서 순간이동 요청을 거절 하셨습니다.")
			if (session.count > 0) {
				TpaFormat.msg(player, "남은 순간이동 요청: ${session.count}개")
			}
		}
	}

	fun onCancel(player: Player) {
		SessionManager.cancleAll(player)
		TpaFormat.msg(player, "모든 요청을 취소 하였습니다.")
	}

	fun onList(player: Player) {
		val list: MutableSet<Player> = SessionManager.getSession(player).queue
		if (list.isEmpty()) {
			TpaFormat.msg(player, "받은 순간이동 요청 0개")
			return
		}
		var text = "===== 대기 리스트 =====\n"
		var count = 1
		list.forEach {
			text += "$count. ${it.name} \n"
			if (count == 5) return@forEach
			count += 1
		}
		player.sendMessage(text(text))
	}
}