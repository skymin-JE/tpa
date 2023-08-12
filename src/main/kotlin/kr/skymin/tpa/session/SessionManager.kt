package kr.skymin.tpa.session

import kr.skymin.tpa.utils.TpaFormat
import org.bukkit.entity.Player

object SessionManager {

	private val sessions: MutableMap<Int, TpaSession> = mutableMapOf()

	fun getSession(player: Player): TpaSession {
		var session: TpaSession? = sessions[player.entityId]
		if (session === null) {
			session = TpaSession(player)
			sessions[player.entityId] = session
		}
		return session
	}

	fun addSession(player: Player) {
		sessions[player.entityId] = TpaSession(player)
	}

	fun removeSession(player: Player) {
		cancleAll(player)
		sessions.remove(player.entityId)
	}

	fun cancleAll(player: Player) {
		sessions.forEach { (_, session) ->
			TpaFormat.msg(session.player, "${player.name}님의 요청이 취소되었습니다")
			session.remove(player)
		}
	}
}