package plugin.activity.gnomecooking.bowls

import core.game.interaction.UseWithHandler
import core.game.content.ItemNames
import core.game.interaction.NodeUsageEvent
import core.game.node.item.Item
import core.plugin.InitializablePlugin
import core.plugin.Plugin

/**
 * Handles garnishing of gnomebowls
 * @author Ceikry
 */
@InitializablePlugin
class GnomeBowlGarnisher : UseWithHandler(9560,9562,9564) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(ItemNames.EQUA_LEAVES_2128, ITEM_TYPE,this)
        addHandler(ItemNames.POT_OF_CREAM_2130, ITEM_TYPE,this)
        addHandler(ItemNames.CHOCOLATE_DUST_1975, ITEM_TYPE,this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val player = event.player
        val used = event.used.asItem()
        val with = event.usedWith.asItem()
        var product = -1
        when(with.id){
            ItemNames.EQUA_LEAVES_2128 -> {
                when(used.id){
                    9562 -> product = ItemNames.VEG_BALL_2195
                    9564 -> product = ItemNames.WORM_HOLE_2191
                }
            }

            ItemNames.POT_OF_CREAM_2130,ItemNames.CHOCOLATE_DUST_1975 -> {
                when(used.id){
                    9560 -> product = ItemNames.CHOCOLATE_BOMB_2185
                }
            }
        }
        if(product == ItemNames.CHOCOLATE_BOMB_2185){
            val reqCream = Item(ItemNames.POT_OF_CREAM_2130,2)
            val reqChoc = Item(ItemNames.CHOCOLATE_DUST_1975)
            if(!player.inventory.containsItem(reqChoc)  || !player.inventory.containsItem(reqCream)){
                player.dialogueInterpreter.sendDialogue("You don't have enough ingredients to finish that.")
                return true
            }
            player.inventory.remove(reqCream)
            player.inventory.remove(reqChoc)
        }
        if(product == -1) return false
        player.inventory.remove(used)
        player.inventory.remove(with)
        player.inventory.add(Item(product))
        return true
    }
}