package com.team.project.KakaoMapAPI

// 리사이클러 뷰 아이템 클래스
class MapListLayout(val name: String,      // 장소명
                 val road: String,      // 도로명 주소
                 val distance:String,    // 거리 차이
                 val url:String,        // 해당 url
                 val phone:String,     // 전화번호
                 val category:String, // 카테고리 정보
                 val x: Double,         // 경도(Longitude)
                 val y: Double)         // 위도(Latitude)
