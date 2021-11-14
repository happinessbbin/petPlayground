package com.team.project.board

data class BoardModel (
    var title : String = "",
    var content : String = "",
    var uid : String = "",
    var time : String = "",
    var image: String = "",
    var boardUid: String ="",
    var favoriteCount : Int = 0,
    var favorites: MutableMap<String, Boolean> = HashMap()){
    data class Comment( var uid : String = "")
}
