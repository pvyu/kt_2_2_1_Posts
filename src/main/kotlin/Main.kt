import ru.netology.posts.Post
import ru.netology.posts.WallService

fun main() {
    val post : Post = Post(text = "first post")
    val addedPost = WallService.add(post).copy(text = "first post edited")

    var updateResult : Boolean = false
    updateResult = WallService.update(addedPost)
    println("Known post updating: $updateResult")
    updateResult = WallService.update(post)
    println("Unknown post updating: $updateResult")
}