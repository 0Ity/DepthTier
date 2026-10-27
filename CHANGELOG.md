# Changelog

## v2.1.1

### Features

- add modpack deploy and release tasks (e8d9a97)

### Bug Fixes

- keep NUL field separators when parsing changelog git log (dca7b7b)

## v2.1.0

- Added a `Harvest Tier` line to tool tooltips, using the tier's display name and color. Turn it off with `toolTierTooltip` in `config/betterharvestlevels/client.json`
- `/bhl check` now shows an estimated level floor for tools with an unknown tier, like `Unknown, level 3+`
