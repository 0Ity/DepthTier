# Changelog

## v1.1.0

- Ores can no longer be mined with any sufficiently-tiered tool: vanilla's tool-type check (`mineable/*` tags) is preserved, BHL only tightens the tier requirement on top of it (#3)
- Tools that aren't vanilla `TieredItem` (Tinkers' Construct etc.) are no longer locked to "cannot mine" — they defer to vanilla unless given a tier in `tools.json` (#2)
- `tools.json` overrides now apply to any item, not just vanilla tiered tools
- The wrong-tool mining speed penalty no longer hits unknown tools or the empty hand, which should unstick fake-player automation like Workers (#4)
- The Jade tooltip checkmark now reflects actual harvestability instead of a tier-only estimate
- Tier names that aren't defined in `tiers.json` are logged once instead of silently allowing mining