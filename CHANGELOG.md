# Changelog

## v2.0.0

- Full rewrite. This release targets NeoForge 1.21.1 only; the Forge and Fabric 1.20.1 modules and the Fabric 1.21.1 module are parked, with ports planned separately
- Block overrides now work in both directions, raising or lowering a block's harvest requirement instead of only tightening it
- Tool overrides can empower a tool, not just restrict it, and bare hands are handled through a reserved `hand` tier instead of ad-hoc special-casing
- Enforcement moved to NeoForge's `HarvestCheck` event, removing all mixins from the mod
- Resolved rules now sync to clients on join and on reload, so multiplayer prediction and tooltips match the server
- Jade shows the modified harvest requirements (required-tool icon and harvestability check), including custom tier icons via `iconItem`
- Added `/bhl info`, `/bhl check`, and `/bhl reload` commands; reload re-resolves tag, mod, and regex overrides and re-syncs all clients, fixing the old reload bug

### Known limitations

- Block overrides cannot gate tools whose tier the mod cannot derive: modded tools that are not vanilla `TieredItem` and have no `tools.json` override fall back to vanilla behavior. Add an explicit `tools.json` override for that tool to work around this.

## v1.1.0

- Ores can no longer be mined with any sufficiently-tiered tool: vanilla's tool-type check (`mineable/*` tags) is preserved, BHL only tightens the tier requirement on top of it (#3)
- Tools that aren't vanilla `TieredItem` (Tinkers' Construct etc.) are no longer locked to "cannot mine" — they defer to vanilla unless given a tier in `tools.json` (#2)
- `tools.json` overrides now apply to any item, not just vanilla tiered tools
- The wrong-tool mining speed penalty no longer hits unknown tools or the empty hand, which should unstick fake-player automation like Workers (#4)
- The Jade tooltip checkmark now reflects actual harvestability instead of a tier-only estimate
- Tier names that aren't defined in `tiers.json` are logged once instead of silently allowing mining