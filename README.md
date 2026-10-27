# DepthTier

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green) ![Loader](https://img.shields.io/badge/NeoForge-21.1.256-orange) ![Version](https://img.shields.io/badge/version-2.1.1-blue) ![License](https://img.shields.io/badge/license-LGPL--3.0-lightgrey)

Config-driven mining level progression for modpacks. Override block and tool tiers up or down, sync rules for multiplayer, and show harvest requirements in Jade.

## About

DepthTier lets you define exactly which pickaxe tier is needed to mine each block — and which tier each tool provides — all through simple JSON files, no code changes.

While vanilla stops at seven mining levels, **DepthTier expands the progression to 19 tiers**: the seven vanilla ones (wood, gold, stone, copper, iron, diamond, netherite) plus modded ladders for **Mystical Agriculture** (inferium → awakened supremium), **Allthemodium** (allthemodium → unobtainium) and **Immersive Ores** (vibranium, vulpus, enderium).

## Features

- **Two-way overrides** — raise *or* lower mining requirements for blocks, and buff or nerf tools (pickaxes, paxels, hammers). Works both directions.
- **Full tier ladder** — define any number of tiers with custom levels, display names, colors, and representative items.
- **Multiplayer-safe** — resolved rules are synced to clients on join and on reload, so mining prediction is always correct.
- **Jade integration** *(optional)* — shows the modified harvest requirement when you look at a block.
- **Harvest Tier tooltip** — tools display their tier name in the tooltip, colored to match. Toggle with `toolTierTooltip` in `client.json`.
- **In-game commands** — inspect and debug your setup without leaving the game.

## Commands

| Command | Description |
|---|---|
| `/dt info` | List all configured tiers and their levels |
| `/dt check hand` | Show the tier of the tool you're holding |
| `/dt check block` | Show what's required to mine the block you're looking at |
| `/dt reload` | Reload configs from disk *(requires permission level 2)* |

## Configuration

Three JSON files in `config/depthtier/`:

- **`tiers.json`** — the tier ladder (level, name, color, icon item)
- **`tools.json`** — block and tool overrides
- **`client.json`** — client-side settings (tooltip toggle)

Configs from older versions (`betterharvestlevels/`) are migrated automatically on first launch.

### Example

```json
// tiers.json — add a custom tier
{ "name": "mythic", "level": 6, "displayName": "Mythic", "color": "#FF44FF",
  "iconItem": "example:mythic_pickaxe" }
```

## Compatibility

Ready-to-use tool overrides ship for:

| Mod | Tools mapped |
|---|---|
| Aether | 5 pickaxes |
| Allthemodium | 5 (incl. alloy paxel) |
| Cataclysm | 4 (pickaxes + forges) |
| DeeperDarker | 2 |
| Ice and Fire | 6 |
| Immersive Ores | 12 (pickaxe / paxel / hammer / excavator) |
| Just Dire Things | 6 (incl. paxels) |
| Mekanism Tools | 18 (pickaxes + paxels) |
| Mystical Agriculture | 6 |
| Ore Hammer | 1 |
| Relics – Thirteen Flames | 2 |
| Twilight Forest | 6 |

- **Jade** — optional, recommended (shows the required mining level in the block tooltip)
- Block overrides are supported for any mod via `tools.json`, even when no preset ships for it.

## Credits

- **Created by [Ity_](https://modrinth.com/team/ity_)**
- Originally released as *BetterHarvestLevels* by **TysonTheEmber**
- License: [LGPL-3.0](https://www.gnu.org/licenses/lgpl-3.0.html)

## Links

- **[Modrinth](https://modrinth.com/mod/depthtier)**
