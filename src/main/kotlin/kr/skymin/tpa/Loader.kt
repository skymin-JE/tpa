package kr.skymin.tpa

import io.github.monun.kommand.kommand
import kr.skymin.tpa.command.Executor
import org.bukkit.plugin.java.JavaPlugin

class Loader : JavaPlugin() {

	override fun onEnable() {
		server.pluginManager.registerEvents(EventListener(), this)
		registerCommands()
	}

	private fun registerCommands() {
		kommand {
			register("tpa") {
				requires { isPlayer }
				executes { Executor.onBase(player, this@Loader) }
				val target = player()
				target.suggests {
					suggest(server.onlinePlayers.map { it.name }.toMutableSet())
				}
				then("target" to target) {
					executes { Executor.onRequest(player, it["target"]) }
				}
			}
			register("tpaccept") {
				requires { isPlayer }
				executes { Executor.onAccept(player, this@Loader) }
			}
			register("tpadeny") {
				requires { isPlayer }
				executes { Executor.onDeny(player) }
			}
			register("tpacancel") {
				requires { isPlayer }
				executes { Executor.onCancel(player) }
			}
			register("tpalist") {
				requires { isPlayer }
				executes { Executor.onList(player) }
			}
		}
	}
}