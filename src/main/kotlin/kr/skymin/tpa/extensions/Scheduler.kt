package kr.skymin.tpa.extensions

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler

fun BukkitScheduler.scheduleSyncDelayedTask(plugin: Plugin, delay: Long, task: Runnable) {
	this.scheduleSyncDelayedTask(plugin, task, delay)
}