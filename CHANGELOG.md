# Hollow 2.0.0

This update is a major overhaul of Hollow. It's focus has changed from adding scrapped content to touching up the vanilla experience (with a focus on nature).

In the process of this massive overhaul, its quite likely we will have forgotten to add things to the changelog, but we will try our best to get everything!

### Changes
* Updated to 26.2
* Removed Fireflies
  * This feature was added to Vanilla in the form of the Firefly Bush
* Removed several decorative plants
  * Most biomes did not need more flowers, the addition of these added too much visual clutter. These may return in a future update if I can find a way to integrate them better.
* Removed Hollow's fallen trees
    * These have been replaced by an almost identical feature in vanilla.
* Removed all languages other than `en_US`
	* All of these were extremely outdated and would have been a nightmare to update with the amount of changes in this update.
      *  If you want them back, please make a PR! We are always happy to see new languages. From now on we will provide translation changelogs to make updates easier.
* Renamed "Firefly Jar" to "Jar of Fireflies"
* Renamed all "Hollow Stripped `<name>` Log" to "Stripped Hollow `<name>` Log"
* Changed the Sculk Jaw to have a shorter collision box (slightly taller than a slab)
* Disabled the player's step up when inside a Sculk Jaw
* Changed the default value of the Delay Copper Bulbs Game Rule from `true` -> `false`
	* This Game Rule while useful for new redstone contraptions has the potential to break some that work in vanilla.

### Features
* Added Switchgrass, a version of the Firefly Bush without any fireflies
	* Switchgrass can be obtained by using an empty jar on a firefly bush to collect its fireflies.
    * Switchgrass will turn back into a Firefly Bush under the right conditions.

### Bug Fixes
* Probably a lot. Many parts of the codebase were entirely rewritten.
    * I have gotten a lot better at programming since I first wrote Hollow.
* Removed Obabo
