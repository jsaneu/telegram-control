# Telegram Control

Control Minecraft from Telegram. First try with Forge.

## Minecraft versions
- [x] 1.16.1
- [x] 1.15.2
- [x] 1.12.2
- [ ] 1.14.4
- [ ] 1.13.2

## Broadcast
* Server start
* Server stop
* Player login
* Player logout
* Player death
* Player advancement
* Player chat
* Player whisper
* Player commands
* Player dimension change

## Commands
* `/status`: Show the server status (name, version, game type, difficulty, players, start time, JVM free memory, JVM max memory, JVM total memory).
* `/players`: Show the player list.
* `/cmd <command>`: Execute command.
* `/actions`: Show the configurable actions.

## Installation
* Create a telegram bot in Telegram with [@BotFather](https://telegram.me/BotFather "@BotFather")
* Use [@GetChatBot](https://telegram.me/GetChatBot "@GetChatBot") to get your chat Id
* Update the file `config/telegram-control/config.json`
* Set your token in `token` property
* Set your chat id in `chatIds` property

## Actions examples
### 1.12.2
```json
"actions": {
    "❤ Heal": "effect ${player} minecraft:instant_health",
    "💥 Damage": "effect ${player} minecraft:instant_damage",
    "🛸 Levitation": "effect ${player} minecraft:levitation 10",
    "🤢 Poison": "effect ${player} minecraft:poison 10",
    "⚡ Lightning": "execute ${player} ~0 ~0 ~0 /summon lightning_bolt @p",
    "🙈 Blindness": "effect ${player} minecraft:blindness 10",
    "☁ Go up": "tp ${player} ~ ~150 ~",
    "😵 Nausea": "effect ${player} minecraft:nausea 10",
    "🩲 Clear inv.": "clear ${player}",
    "☠ Kill": "kill ${player}",
    "💤 Jail": "spawnpoint ${player} -1022 129 55;tp ${player} -1022 129 55",
    "🦶 Kick": "kick ${player}",
    "🔒 Ban": "ban ${player}"
  }
```
### 1.15.2
```json
"actions": {
    "❤ Heal": "effect give ${player} minecraft:instant_health",
    "💥 Damage": "effect give ${player} minecraft:instant_damage",
    "🛸 Levitation": "effect give ${player} minecraft:levitation 10",
    "🤢 Poison": "effect give ${player} minecraft:poison 10",
    "⚡ Lightning": "execute positioned as ${player} run summon minecraft:lightning_bolt",
    "🙈 Blindness": "effect give ${player} minecraft:blindness 10",
    "☁ Go up": "tp ${player} ~ ~150 ~",
    "😵 Nausea": "effect give ${player} minecraft:nausea 10",
    "🩲 Clear inv.": "clear ${player}",
    "☠ Kill": "kill ${player}",
    "💤 Jail": "spawnpoint ${player} -1022 129 55;tp ${player} -1022 129 55",
    "🦶 Kick": "kick ${player}",
    "🔒 Ban": "ban ${player}"
  }
```
## Misc
* [Config examples](https://github.com/jsaneu/telegram-control/tree/master/assets/config-examples)
* [Screenshots](https://github.com/jsaneu/telegram-control/tree/master/assets/images/screenshots)
* To change the language on server-side, you can use the [Server Localization](https://www.curseforge.com/minecraft/mc-mods/server-localization) mod. [Source](https://github.com/glitchless/MinecraftServerLocalization)

