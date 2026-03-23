package org.example.btvnkotlin.Week6_03_17.data

import btvnkotlin.composeapp.generated.resources.*

object DataSource {
    val flavors = listOf(
        Res.string.vanilla,
        Res.string.chocolate,
        Res.string.red_velvet,
        Res.string.salted_caramel,
        Res.string.coffee
    )

    val quantityOptions = listOf(
        Pair(Res.string.one_cupcake, 1),
        Pair(Res.string.six_cupcakes, 6),
        Pair(Res.string.twelve_cupcakes, 12)
    )
}
