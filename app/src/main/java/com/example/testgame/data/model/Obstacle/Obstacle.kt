package com.example.testgame.data.model.Obstacle

import com.example.testgame.R
import java.util.UUID

open class Obstacle(
    val id: String = UUID.randomUUID().toString(),
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 64f,
    val speed: Float = 8f,
    val image: Int = R.drawable.ic_rocket,

) {


     open fun copy(
        id: String = this.id,
        x: Float = this.x,
        y: Float = this.y,
        size: Float = this.size,
        speed: Float = this.speed,
        image: Int = this.image,
    ): Obstacle {
        return Obstacle(id, x, y, size, speed, image)
    }

    override fun equals(other: Any?): Boolean {
        return other is Obstacle && other.id == this.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    open fun updatePosition(): Obstacle {


        return copy(y = this.y + this.speed)
    }
}




